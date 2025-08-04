package practice.adaptor.out

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

@Component
class TodayExternalWithdrawRepository(
    private val redisTemplate: RedisTemplate<String, Int>
) {

    operator fun get(accountId: Long): Int {
        val key = getKey(accountId)

        val value = Optional.ofNullable(redisTemplate.opsForValue().get(key))
        return value.orElseGet {
            redisTemplate.opsForValue().set(key, 0, TTL_HOURS.toLong(), TimeUnit.HOURS)
            0
        }
    }

    private fun getKey(accountId: Long): String {
        val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        return "daily-external-total:$accountId:$today"
    }

    fun increment(accountId: Long, amount: BigDecimal) {
        val key = getKey(accountId)
        redisTemplate.opsForValue().increment(key, amount.toInt().toLong())
    }

    fun decrement(accountId: Long, amount: BigDecimal) {
        val key = getKey(accountId)
        redisTemplate.opsForValue().decrement(key, amount.toInt().toLong())
    }

    companion object {
        private const val TTL_HOURS = 24
    }
}
