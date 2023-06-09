package com.matsko.configuration;

import com.matsko.utils.CryptoTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Class for connect {@link CryptoTool}.
 */
@Configuration
public class RestServiceConfiguration {

    /**
     * Field that contains the salt.
     */
    @Value("${salt}")
    private String salt;

    /**
     * Bean.
     *
     * @return an object of the desired class.
     */
    @Bean
    public CryptoTool getCryptoTool(){
        return new CryptoTool(salt);
    }
}
