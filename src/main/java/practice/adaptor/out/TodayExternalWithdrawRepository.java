package practice.adaptor.out;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TodayExternalWithdrawRepository {

  private final RedisTemplate<String, Integer> redisTemplate;

  public Integer get(Long accountId) {
    String key = getKey(accountId);
    return Optional.ofNullable(redisTemplate.opsForValue().get(key))
        .orElse(0);
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
