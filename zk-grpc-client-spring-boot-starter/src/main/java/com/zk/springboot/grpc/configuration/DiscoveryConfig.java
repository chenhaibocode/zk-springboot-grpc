package com.zk.springboot.grpc.configuration;

import com.zk.springboot.grpc.discovery.ServiceDiscovery;
import com.zk.springboot.grpc.discovery.impl.ServiceDiscoveryImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: chenhaibo
 * @Date: 2018/11/22 17:51
 * @Description:
 */
@Configuration
@ConfigurationProperties(prefix = "registry")
public class DiscoveryConfig {
    private String linkerdAddress;

    @Bean
    public ServiceDiscovery serviceDiscovery() {
        return new ServiceDiscoveryImpl(linkerdAddress) ;
    }

    public void setLinkerdAddress(String linkerdAddress) {
        this.linkerdAddress = linkerdAddress;
    }
}
