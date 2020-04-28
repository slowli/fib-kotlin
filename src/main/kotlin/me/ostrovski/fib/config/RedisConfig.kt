package me.ostrovski.fib.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories

/**
 * Database configuration of the app.
 */
@Configuration
@EnableRedisRepositories
class RedisConfig
