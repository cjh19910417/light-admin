package org.lightadmin.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

/**
 * Runs an embedded Redis instance. This is only necessary since we do not want
 * users to have to setup a Redis instance. In a production environment, this
 * would not be used since a Redis Server would be setup.
 *
 * @author Rob Winch
 */
@Configuration
@EnableRedisHttpSession
public class RedisHttpSessionConfiguration {
    @Bean
    public JedisConnectionFactory connectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setPort(6379);
        jedisConnectionFactory.setHostName("127.0.0.1");
        return jedisConnectionFactory;
    }
    /**
     * HttpSessionStrategy
     *
     * @return
     */
    /*@Bean
    public HttpSessionStrategy httpSessionStrategy() {
        return new HeaderHttpSessionStrategy();
    }*/
}