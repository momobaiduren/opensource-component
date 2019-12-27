package com.opensource.component.dinging.configuration;

import com.opensource.component.dinging.DindingProvider;
import javax.annotation.Resource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author: chejiangyi
 * @version: 2019-07-23 13:48
 **/
@Configuration
@EnableConfigurationProperties(DingingProperties.class)
@ConditionalOnProperty(name = "opensource.component.dinging.enable", havingValue = "true")
public class DindingConfiguration implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
    }
    private RestTemplate restTemplate;
    @Bean
    public RestTemplate restTemplate(){
        restTemplate = new RestTemplate();
        return restTemplate;
    }
    @Resource
    private DingingProperties dingingProperties;

    @Bean
    public DindingProvider dindingProvider() {
        return new DindingProvider(restTemplate, dingingProperties);
    }
}
