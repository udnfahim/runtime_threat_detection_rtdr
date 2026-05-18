package com.systemgrade.rtdr.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.systemgrade.rtdr.repository")
public class JpaConfig {
}