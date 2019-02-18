package com.zk.springboot.grpc.factory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.zk.springboot.grpc.factory.enums.AttemptOperations;
import com.zk.springboot.grpc.factory.enums.StopStrategies;
import com.zk.springboot.grpc.factory.enums.WaitTimeStrategies;
import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class GrpcChannelFactory {

    private static final ExecutorService THREAD_EXECUTOR =
            ThreadPoolUtil.getArrayBlockingQueuePoolExecutorRejectDiscard(
                    2,
                    2,
                    1,
                    TimeUnit.MINUTES,
                    5000,
                    "grpc-channelFactory",
                    new ThreadPoolUtil.DiscardRejectedExecutionHandler("application-grpc-channelFactory")
            );
    private LoadingCache<Endpoint, ManagedChannel> channelCache;

    public GrpcChannelFactory() {
        channelCache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterAccess(20, TimeUnit.MINUTES)
                .recordStats()
                .weakValues()
                .removalListener(removalNotification -> {
                    Endpoint endpoint = (Endpoint) removalNotification.getKey();
                    ManagedChannel managedChannel = (ManagedChannel) removalNotification.getValue();

                    /*log.info("[COMMON][CHANNEL][REMOVE] removed for ip: {}, port: {}. reason: {}",
                            endpoint.getIp(),
                            endpoint.getPort(),
                            removalNotification.getCause());*/
                    if (managedChannel != null) {
                        /*log.info("[COMMON][CHANNEL][REMOVE] removed channel state: isShutdown: {}, isTerminated: {}",
                                managedChannel.isShutdown(), managedChannel.isTerminated());*/
                        managedChannel.shutdown();

                        try {
                            boolean awaitResult = managedChannel.awaitTermination(10, TimeUnit.MILLISECONDS);
                            if (!awaitResult) {
                                log.warn("[COMMON][CHANNEL][REMOVE] channel await termination timeout");
                            }
                        } catch (InterruptedException e) {
                            log.warn("[COMMON][CHANNEL][REMOVE] channel await termination interrupted");
                            Thread.currentThread().interrupt();
                        }
                    } else {
                        log.info("[COMMON][CHANNEL][REMOVE] channel is null");
                    }
                })
                .build(new CacheLoader<Endpoint, ManagedChannel>() {
                           @Override
                           public ManagedChannel load(Endpoint endpoint) throws Exception {
                               Preconditions.checkNotNull(endpoint);
                               /*log.info("[COMMON][CHANNEL][CREATE] creating channel for endpoint: ip: {}, port: {}",
                                       endpoint.getIp(), endpoint.getPort());*/
                               return createChannel(endpoint);
                           }
                       }
                );
    }

    public ManagedChannel getChannel(final Endpoint endpoint) {
        ManagedChannel result = null;

        Retryer<ManagedChannel> retryer = RetryerBuilder.<ManagedChannel>newBuilder()
                .withWaitTimeStrategy(WaitTimeStrategies.fixedWaitTime(1000))
                .withStopStrategy(StopStrategies.stopAfterMaxAttempt(10))
                .withAttemptOperation(AttemptOperations.<ManagedChannel>fixedTimeLimit(3, TimeUnit.SECONDS))
                .retryIfAnyException()
                .build();

        final Callable<ManagedChannel> getUsableChannel = () -> getChannelInternal(endpoint);

        try {
            result = retryer.call(getUsableChannel);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error("[COMMON][CHANNEL][ERROR] Error getting ManagedChannel after retries");
        }

        return result;
    }

    private ManagedChannel getChannelInternal(Endpoint endpoint) {
        ManagedChannel result = null;
        try {
            result = channelCache.get(endpoint);
            ConnectivityState state = result.getState(true);

            if (result.isShutdown() || result.isTerminated()) {
                channelCache.invalidate(result);
                result = channelCache.get(endpoint);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    private ManagedChannel createChannel(Endpoint endpoint) {
        String target = endpoint.getIp();
        if (Strings.isNullOrEmpty(target)) {
            return null;
        }

        NettyChannelBuilder builder = NettyChannelBuilder
                .forAddress(target, endpoint.getPort())
                .executor(THREAD_EXECUTOR)
                .keepAliveTime(6, TimeUnit.MINUTES)
                .keepAliveTimeout(24, TimeUnit.HOURS)
                .keepAliveWithoutCalls(true)
                .idleTimeout(1, TimeUnit.HOURS)
                .perRpcBufferLimit(128 << 20)
                .flowControlWindow(32 << 20)
                .maxInboundMessageSize(32 << 20)
                .enableRetry()
                .retryBufferSize(16 << 20)
                .maxRetryAttempts(20);

        builder.negotiationType(NegotiationType.PLAINTEXT)
                .usePlaintext();

        ManagedChannel managedChannel = builder
                .build();

        return managedChannel;
    }

}
