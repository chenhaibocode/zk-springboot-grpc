package com.zk.springboot.grpc.configuration;

import com.zk.springboot.grpc.registry.ServiceRegistry;
import com.zk.springboot.grpc.registry.impl.ServiceRegistryImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: chenhaibo
 * @Date: 2018/11/20 23:10
 * @Description:
 */
@Configuration
@ConfigurationProperties(prefix = "registry")
public class RegistryConfig {
    private String zkServers;

    @Bean
    public ServiceRegistry serviceRegistry() {
        return new ServiceRegistryImpl(zkServers);
    }

    public void setZkServers(String zkServers) {
        this.zkServers = zkServers;
    }
}
