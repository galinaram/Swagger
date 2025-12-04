package com.example.Swagger;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public GreenMail greenMail() {
        return new GreenMail(new ServerSetup(3025, null, "smtp"))
                .withConfiguration(GreenMailConfiguration.aConfig()
                        .withUser("test", "test")
                );
    }
}