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
    private String servers;
    private String nodepath;
    private String username;
    private String password;

    @Bean
    public ServiceDiscovery serviceDiscovery() {
        return new ServiceDiscoveryImpl(servers, nodepath, username, password) ;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public void setNodepath(String nodepath) {
        this.nodepath = nodepath;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
