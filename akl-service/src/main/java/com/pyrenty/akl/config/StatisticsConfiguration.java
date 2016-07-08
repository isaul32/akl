package com.pyrenty.akl.config;

import com.pyrenty.akl.service.UdpService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.ip.udp.UnicastReceivingChannelAdapter;

@Configuration
public class StatisticsConfiguration {

    @Value("${akl.statistics.port}")
    private Integer port;

    @Bean
    public UdpService udpService() {
        return new UdpService();
    }

    @Bean
    public IntegrationFlow processUdpMessage() {
        return IntegrationFlows
                .from(new UnicastReceivingChannelAdapter(port))
                .handle("udpService", "receive")
                .get();
    }
}
