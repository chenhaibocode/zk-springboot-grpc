package com.zk.springboot.grpc.discovery.impl;

import com.zk.springboot.grpc.discovery.ServiceDiscovery;
import com.zk.springboot.grpc.factory.Endpoint;
import com.zk.springboot.grpc.factory.GrpcChannelFactory;
import io.grpc.ManagedChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.Random;

/**
 * @Auther: chenhaibo
 * @Date: 2018/11/22 17:49
 * @Description:
 */
@Slf4j
@Component
public class ServiceDiscoveryImpl implements ServiceDiscovery {

    private String linkerdAddress;

    @Autowired
    private GrpcChannelFactory grpcChannelFactory;

    public ServiceDiscoveryImpl() {
    }

    public ServiceDiscoveryImpl(String linkerdAddress) {
        this.linkerdAddress = linkerdAddress;
    }

    /**
     * 随机获取一个可用的服务地址，作负载均衡使用
     *
     * @return
     */
    private Optional<String> getServer() {
        if (StringUtils.isEmpty(linkerdAddress)) {
            return Optional.ofNullable(null);
        }
        String[] strtArrays = linkerdAddress.split(";");
        int size = strtArrays.length;
        if (size == 0) {
            log.error("does not have available server.");
            return Optional.ofNullable(null);
        }
        int rand = new Random().nextInt(size);
        //log.info("size=" + size + ",rand=" + rand);
        String server = strtArrays[rand];
        return Optional.ofNullable(server);
    }

    @Override
    public ManagedChannel getManagedChannel() {
        Optional<String> server = getServer();
        if (server.isPresent()) {
            String[] array = server.get().split(":");
            //log.info("server host=" + array[0] + ",port=" + array[1]);
            return grpcChannelFactory.getChannel(new Endpoint(array[0], Integer.parseInt(array[1])));
        } else {
            log.error("not find avaliable server====");
        }
        return null;
    }
}