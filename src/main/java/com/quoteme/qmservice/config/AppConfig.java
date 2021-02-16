package com.quoteme.qmservice.config;

import com.quoteme.qmservice.mapper.QuoteMapper;
import com.quoteme.qmservice.mapper.UserMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public UserMapper userMapper() {
        return UserMapper.INSTANCE;
    }

    @Bean
    public QuoteMapper quoteMapper() {
        return QuoteMapper.INSTANCE;
    }

}
