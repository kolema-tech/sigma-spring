//package com.sigma.security.oauth2;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @author huston.peng
// * @version 1.0.0
// * date-time: 2019/5/16-22:39
// * desc:
// **/
//@Configuration
//@ConditionalOnClass(StarterService.class)
//@EnableConfigurationProperties(StarterServiceProperties.class)
//public class StarterAutoConfigure {
//
//    @Autowired
//    private StarterServiceProperties properties;
//
//    @Bean
//    @ConditionalOnMissingBean
//    @ConditionalOnProperty(prefix = "example.service", value = "enabled", havingValue = "true")
//    StarterService starterService (){
//        return new StarterService(properties.getConfig());
//    }
//}
