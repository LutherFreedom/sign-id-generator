package com.luther.base.restgenerator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "generator.factory")
public class GeneratorConfig {

        private Long genMethd;
        private String type;
        private Long machine;

    public Long getGenMethd() {
        return genMethd;
    }

    public void setGenMethd(Long genMethd) {
        this.genMethd = genMethd;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getMachine() {
        return machine;
    }

    public void setMachine(Long machine) {
        this.machine = machine;
    }
}
