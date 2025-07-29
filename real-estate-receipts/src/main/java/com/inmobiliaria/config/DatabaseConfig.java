package com.inmobiliaria.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class DatabaseConfig {
    // Configuración de base de datos manejada por application.properties
}
