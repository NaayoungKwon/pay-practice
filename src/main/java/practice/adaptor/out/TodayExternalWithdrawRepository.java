package practice.adaptor.out;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TodayExternalWithdrawRepository {

  private final RedisTemplate<String, Integer> redisTemplate;
  private static final Integer TTL_HOURS = 24;

  public Integer get(Long accountId) {
    String key = getKey(accountId);

    Optional<Integer> value = Optional.ofNullable(redisTemplate.opsForValue().get(key));
    return value.orElseGet(() -> {
      redisTemplate.opsForValue().set(key, 0, TTL_HOURS, TimeUnit.HOURS);
      return 0;
    });
  }

  private String getKey(Long accountId){
    String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    return String.format("daily-external-total:%d:%s", accountId, today);
  }

  public void increment(Long accountId, BigDecimal amount) {
    String key = getKey(accountId);
    redisTemplate.opsForValue().increment(key, amount.intValue());
  }

  public void decrement(Long accountId, BigDecimal amount) {
    String key = getKey(accountId);
    redisTemplate.opsForValue().decrement(key, amount.intValue());
  }
}
