package practice.common.idempotency

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Aspect
@Component
class IdempotencyAoP(
    private val redisTemplate: StringRedisTemplate
) {

    @Around("@annotation(practice.common.idempotency.IdempotencyCheck) && within(@org.springframework.web.bind.annotation.RestController *)")
    fun checkIdempotency(joinPoint: ProceedingJoinPoint): Any {
        val key = getKey(joinPoint)

        if (checkAndSet(key)) {
            return joinPoint.proceed()
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).build<Any>()
    }

    private fun getKey(joinPoint: ProceedingJoinPoint): String {
        val signature = joinPoint.signature as MethodSignature
        val method = signature.method
        val idempotencyCheck = method.getAnnotation(
            IdempotencyCheck::class.java
        )
        return getKey(idempotencyCheck, signature.parameterNames, joinPoint.args)
    }

    fun checkAndSet(idempotencyKey: String): Boolean {
        val success = redisTemplate.opsForValue()
            .setIfAbsent(idempotencyKey, "EXISTS", EXPIRE_TIME, TimeUnit.SECONDS)
        return success ?: false
    }

    private fun getKey(idempotencyCheck: IdempotencyCheck, parameterNames: Array<String>, args: Array<Any>): String {
        return String.format(
            "%s:%s", REDISSON_CHECK_PREFIX,
            parseKey(parameterNames, args, idempotencyCheck.value)
        )
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
        private const val REDISSON_CHECK_PREFIX = "IDEMPOTENCY"
        private const val EXPIRE_TIME = 60L
    }
}
