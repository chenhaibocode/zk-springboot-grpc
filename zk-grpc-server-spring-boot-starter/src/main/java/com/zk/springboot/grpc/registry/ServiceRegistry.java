package com.zk.springboot.grpc.registry;

/**
 * @Auther: chenhaibo
 * @Date: 2018/11/20 23:07
 * @Description:
 */
public interface ServiceRegistry {

    /**
     * 注册服务信息
     *
     * @param serviceAddress 服务ip
     * @param serverPort grpc服务端口
     * @param gatewayServerPort grpcGateway服务端口
     */
    void register(String serviceAddress, int serverPort, int gatewayServerPort) throws Exception;
}