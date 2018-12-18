package com.zk.springboot.grpc.discovery.impl;

import com.zk.springboot.grpc.discovery.ServiceDiscovery;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
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

    private String linkerdServers;

    public ServiceDiscoveryImpl(){}

    public ServiceDiscoveryImpl(String linkerdServers){
        this.linkerdServers = linkerdServers;
    }

    /**
     * 随机获取一个可用的服务地址，作负载均衡使用
     * @return
     */
    private Optional<String> getServer(){
        if(StringUtils.isEmpty(linkerdServers)) {
            return Optional.ofNullable(null);
        }
        String[] strtArrays = linkerdServers.split(";");
        int size = strtArrays.length;
        if (size == 0) {
            log.error("does not have available server.");
            return Optional.ofNullable(null);
        }
        int rand = new Random().nextInt(size);
        log.info("size=" + size + ",rand=" + rand);;
        String server = strtArrays[rand];
        return Optional.ofNullable(server);
    }

    @Override
    public ManagedChannel getManagedChannel() {
        Optional<String> server = getServer();
        if (server.isPresent()) {
            String[] array = server.get().split(":");
            log.info("server host=" + array[0] + ",port=" + array[1]);
            ManagedChannel managedChannel = ManagedChannelBuilder
                    .forAddress(array[0], Integer.parseInt(array[1])).usePlaintext().build();
            return managedChannel;
        }else {
            log.error("not find avaliable server====");
        }
        return null;
    }
}