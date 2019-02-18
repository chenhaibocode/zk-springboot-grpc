package com.zk.springboot.grpc.listener;

import com.zk.springboot.grpc.registry.ServiceRegistry;
import com.zk.springboot.grpc.util.NetUtils;
import lombok.extern.slf4j.Slf4j;
import net.devh.springboot.autoconfigure.grpc.server.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Map;

/**
 * @Auther: chenhaibo
 * @Date: 2018/11/20 23:12
 * @Description:
 */
@Slf4j
@Component
public class RegistryListener implements ServletContextListener {

    @Value("${grpc.server.port}")
    private int serverPort;

    @Value("${gateway.server.port}")
    private int gatewayServerPort;

    @Autowired
    private ServiceRegistry serviceRegistry;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(GrpcService.class);
        for (Object bean : beans.values()) {
            if(null != bean) {
                try {
                    serviceRegistry.register(NetUtils.getLocalAddress().getHostAddress(), serverPort, gatewayServerPort);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
