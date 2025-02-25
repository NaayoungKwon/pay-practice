package practice.common.idempotency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


@SpringBootTest
@Import(IdempotencyAoPTest.TestController.class)
class IdempotencyAoPTest {

  @Autowired
  private TestController testController;

  @MockitoBean
  private StringRedisTemplate redisTemplate;

  @Mock
  private ValueOperations<String, String> valueOperations;

  @Test
  void IdempotencyDuplicateCheck2() throws InterruptedException {
    String IDEMPOTENCY_KEY = "test-key";
    Integer THREAD_COUNT = 4;

    when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    when(valueOperations.setIfAbsent("IDEMPOTENCY:" + IDEMPOTENCY_KEY, "EXISTS", 60L, TimeUnit.SECONDS))
        .thenReturn(true)
        .thenReturn(false)
        .thenReturn(false)
        .thenReturn(false);

    ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
    CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
    AtomicInteger successCount = new AtomicInteger();
    AtomicInteger conflictCount = new AtomicInteger();

    for (int i = 0; i < THREAD_COUNT; i++) {
      executorService.submit(() -> {
        try {
          HttpStatusCode statusCode = testController.test(IDEMPOTENCY_KEY).getStatusCode();
          if (statusCode == HttpStatus.OK) {
            successCount.incrementAndGet();
          } else if (statusCode == HttpStatus.CONFLICT) {
            conflictCount.incrementAndGet();
          }
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await();
    executorService.shutdown();

    assertEquals(1, successCount.get());
    assertEquals(3, conflictCount.get());

  }


  @RestController
  public static class TestController {

    @GetMapping("/test")
    @IdempotencyCheck("#idempotencyKey")
    public ResponseEntity<?> test(@RequestHeader("Idempotency-Key") String idempotencyKey) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      return ResponseEntity.status(HttpStatus.OK).build();
    }
  }
}
