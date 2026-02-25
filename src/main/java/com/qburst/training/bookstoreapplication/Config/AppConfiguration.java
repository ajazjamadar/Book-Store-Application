package com.qburst.training.bookstoreapplication.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Singleton Pattern: Spring-managed singleton for application configuration.
 * 
 * Binds properties from application.properties with prefix "app".
 * All services share the same instance.
 */
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfiguration {
    
    private String name = "BookStore Application";
    private String version = "1.0.0";
    private String description = "Online Book Store with Payment Processing";
    
    public String getName() {
        return name;
    }
    
    public String getVersion() {
        return version;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
}
