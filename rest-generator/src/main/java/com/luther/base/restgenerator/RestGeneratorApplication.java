package com.luther.base.restgenerator;

import com.luther.base.intf.IdService;
import com.luther.base.restgenerator.config.GeneratorConfig;
import com.luther.base.service.factory.IdServiceFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableConfigurationProperties
public class RestGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestGeneratorApplication.class, args);
    }

    @Bean(name = "idServiceFactoryBean", initMethod = "init")
    public IdServiceFactoryBean initIdServiceFactory(GeneratorConfig generatorConfig) {
        IdServiceFactoryBean factoryBean = new IdServiceFactoryBean();
        factoryBean.setProviderType(IdServiceFactoryBean.Type.valueOf(generatorConfig.getType()));
        factoryBean.setGenMethod(generatorConfig.getGenMethd());
        factoryBean.setMachineId(generatorConfig.getMachine());
        return factoryBean;
    }

    @Bean(name = "idService")
    public IdService initIdService(IdServiceFactoryBean idServiceFactoryBean) {
        return idServiceFactoryBean.getObject();
    }

}
