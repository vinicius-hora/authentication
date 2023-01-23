package com.rasmoo.client.financescontroll.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class DatabaseConfiguration  {

    private final Environment environment;

    @Bean
    @Primary
    public DataSource fcDatasource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(environment.getProperty("spring.datasource-fc.driver-class-name"));
        dataSource.setUrl(environment.getProperty("spring.datasource-fc.url"));
        dataSource.setUsername(environment.getProperty("spring.datasource-fc.username"));
        dataSource.setPassword(environment.getProperty("spring.datasource-fc.password"));

        return dataSource;
    }

    @Bean(name = "dsOauth")
    public DataSource oAuthDatasource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(environment.getProperty("spring.datasource-oauth.driver-class-name"));
        dataSource.setUrl(environment.getProperty("spring.datasource-oauth.url"));
        dataSource.setUsername(environment.getProperty("spring.datasource-oauth.username"));
        dataSource.setPassword(environment.getProperty("spring.datasource-oauth.password"));

        return dataSource;
    }
}
