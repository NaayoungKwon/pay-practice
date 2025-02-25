package practice.common.lock;

import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import practice.common.tx.TransactionAoP;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAop {

  private static final String REDISSON_LOCK_PREFIX = "LOCK";
  private final RedissonClient redissonClient;
  private final TransactionAoP transactionAoP;

  @Around("@annotation(DistributedLock)")
  public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

    String key = getKey(distributedLock, signature.getParameterNames(), joinPoint.getArgs());
    RLock rLock = redissonClient.getLock(key);

    try {
      boolean locked = rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(),
          distributedLock.timeUnit());
      if (!locked) {
        log.error("Failed to acquire lock");
        return null;
      }

      return transactionAoP.proceed(joinPoint);
    } catch (InterruptedException e) {
      throw new RuntimeException("락 획득 중 인터럽트 발생", e);
    } finally {
      if (rLock != null && rLock.isHeldByCurrentThread()) {
        rLock.unlock();
      }
    }
  }

  private String getKey(DistributedLock distributedLock, String[] parameterNames, Object[] args) {
    return String.format("%s:%s:%s", REDISSON_LOCK_PREFIX, distributedLock.key(),
        parseKey(parameterNames, args, distributedLock.value()));
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
