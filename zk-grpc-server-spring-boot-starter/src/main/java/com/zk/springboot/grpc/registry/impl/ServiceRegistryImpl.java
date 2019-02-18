package com.zk.springboot.grpc.registry.impl;

import com.zk.springboot.grpc.registry.ServiceRegistry;
import com.zk.springboot.grpc.util.ServiceDetail;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceInstanceBuilder;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.springframework.stereotype.Component;

/**
 * @Auther: chenhaibo
 * @Date: 2018/11/20 23:08
 * @Description:
 */
@Slf4j
@Component
public class ServiceRegistryImpl implements ServiceRegistry {

    private String zkServers;

    public ServiceRegistryImpl() {
    }

    public ServiceRegistryImpl(String zkServers) {
        this.zkServers = zkServers;
    }

    @Override
    public void register(String serviceAddress, int serverPort, int gatewayServerPort) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(zkServers, new ExponentialBackoffRetry(1000, 3));
        client.start();
        client.blockUntilConnected();

        ServiceInstanceBuilder<ServiceDetail> sibGrpc = ServiceInstance.builder();
        sibGrpc.address(serviceAddress);
        sibGrpc.port(serverPort);
        sibGrpc.name("grpc");

        ServiceInstance<ServiceDetail> instanceGrpc = sibGrpc.build();

        ServiceInstanceBuilder<ServiceDetail> sibHttp = ServiceInstance.builder();
        sibHttp.address(serviceAddress);
        sibHttp.port(gatewayServerPort);
        sibHttp.name("http");

        ServiceInstance<ServiceDetail> instanceHttp = sibHttp.build();

        ServiceDiscovery<ServiceDetail> serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceDetail.class)
                .client(client)
                .serializer(new JsonInstanceSerializer<>(ServiceDetail.class))
                .basePath(ServiceDetail.REGISTER_ROOT_PATH)
                .build();
        //服务注册
        serviceDiscovery.registerService(instanceGrpc);
        serviceDiscovery.registerService(instanceHttp);
    }

    public String getZkServers() {
        return zkServers;
    }

    public void setZkServers(String zkServers) {
        this.zkServers = zkServers;
    }
}