package io.bootify.ecommerce_app.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EntityScan("io.bootify.ecommerce_app.domain")
@EnableJpaRepositories("io.bootify.ecommerce_app.repos")
@EnableTransactionManagement
public class DomainConfig {
}
