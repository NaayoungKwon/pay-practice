package practice.infrastructure

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.redisson.spring.data.connection.RedissonConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericToStringSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {
    @Value("\${spring.data.redis.host}")
    lateinit var redisHost: String

    @Value("\${spring.data.redis.port}")
    lateinit var redisPort: String

    @Bean
    fun redissonClient(): RedissonClient {
        var redisson: RedissonClient? = null
        val config = Config()
        config.useSingleServer().setAddress(REDISSON_HOST_PREFIX + redisHost + ":" + redisPort)
        redisson = Redisson.create(config)

        return redisson
    }

    @Bean
    fun redisConnectionFactory(redissonClient: RedissonClient): RedissonConnectionFactory {
        return RedissonConnectionFactory(redissonClient)
    }

    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, Int> {
        val redisTemplate = RedisTemplate<String, Int>()

        redisTemplate.connectionFactory = redisConnectionFactory

        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.valueSerializer = GenericToStringSerializer(Int::class.java)


        return redisTemplate
    }

    companion object {
        private const val REDISSON_HOST_PREFIX = "redis://"
    }
}
