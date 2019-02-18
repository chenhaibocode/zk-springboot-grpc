package com.zk.springboot.grpc.factory;

import lombok.Data;

@Data
public class Endpoint {

    private String ip;

    private Integer port;

    public Endpoint(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }
}
