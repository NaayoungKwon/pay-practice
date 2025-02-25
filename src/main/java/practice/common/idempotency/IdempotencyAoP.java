package practice.common.idempotency;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class IdempotencyAoP {

  private static final String REDISSON_CHECK_PREFIX = "IDEMPOTENCY";
  private static final long EXPIRE_TIME = 60L;
  private final StringRedisTemplate redisTemplate;

  @Around("@annotation(IdempotencyCheck) && within(@org.springframework.web.bind.annotation.RestController *)")
  public Object checkIdempotency(ProceedingJoinPoint joinPoint) throws Throwable {
    String key = getKey(joinPoint);

    if (checkAndSet(key)) {
      Object result = joinPoint.proceed();
      removeKey(key);
      return result;
    }

    return ResponseEntity.status(HttpStatus.CONFLICT).build();
  }

  private String getKey(ProceedingJoinPoint joinPoint) {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    IdempotencyCheck idempotencyCheck = method.getAnnotation(IdempotencyCheck.class);
    return getKey(idempotencyCheck, signature.getParameterNames(), joinPoint.getArgs());
  }

  public boolean checkAndSet(String idempotencyKey) {
    Boolean success = redisTemplate.opsForValue()
        .setIfAbsent(idempotencyKey, "EXISTS", EXPIRE_TIME, TimeUnit.SECONDS);
    return Boolean.TRUE.equals(success);
  }

  public void removeKey(String idempotencyKey) {
    redisTemplate.delete(idempotencyKey);
  }

  private String getKey(IdempotencyCheck idempotencyCheck, String[] parameterNames, Object[] args) {
    return String.format("%s:%s", REDISSON_CHECK_PREFIX,
        parseKey(parameterNames, args, idempotencyCheck.value()));
  }

  private Object parseKey(String[] parameterNames, Object[] args, String key) {
    ExpressionParser parser = new SpelExpressionParser();
    StandardEvaluationContext context = new StandardEvaluationContext();

    for (int i = 0; i < parameterNames.length; i++) {
      context.setVariable(parameterNames[i], args[i]);
    }

    return parser.parseExpression(key).getValue(context, Object.class);
  }
}
