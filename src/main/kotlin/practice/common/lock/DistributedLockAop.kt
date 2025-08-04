package practice.common.lock

import io.github.oshai.kotlinlogging.KotlinLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RedissonClient
import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.stereotype.Component
import practice.common.tx.TransactionAoP

private val log = KotlinLogging.logger { }

@Aspect
@Component
class DistributedLockAop(
    private val redissonClient: RedissonClient,
    private val transactionAoP: TransactionAoP
) {

    @Around("@annotation(practice.common.lock.DistributedLock)")
    @Throws(Throwable::class)
    fun lock(joinPoint: ProceedingJoinPoint): Any? {
        val signature = joinPoint.signature as MethodSignature
        val method = signature.method
        val distributedLock = method.getAnnotation(
            DistributedLock::class.java
        )

        val key = getKey(distributedLock, signature.parameterNames, joinPoint.args)
        val rLock = redissonClient.getLock(key)

        try {
            val locked = rLock!!.tryLock(
                distributedLock.waitTime, distributedLock.leaseTime,
                distributedLock.timeUnit
            )
            if (!locked) {
                log.error { "Failed to acquire lock" }
                return null
            }

            return transactionAoP.proceed(joinPoint)
        } catch (e: InterruptedException) {
            throw RuntimeException("락 획득 중 인터럽트 발생", e)
        } finally {
            if (rLock != null && rLock.isHeldByCurrentThread) {
                rLock.unlock()
            }
        }
    }

    private fun getKey(distributedLock: DistributedLock, parameterNames: Array<String>, args: Array<Any>): String {
        return "$REDISSON_LOCK_PREFIX:${distributedLock.key}:${parseKey(parameterNames, args, distributedLock.value)}"
    }

    private fun parseKey(parameterNames: Array<String>, args: Array<Any>, key: String): Any? {
        val parser: ExpressionParser = SpelExpressionParser()
        val context = StandardEvaluationContext()

        for (i in parameterNames.indices) {
            context.setVariable(parameterNames[i], args[i])
        }

        return parser.parseExpression(key).getValue(context, Any::class.java)
    }

    companion object {
        private const val REDISSON_LOCK_PREFIX = "LOCK"
    }
}
