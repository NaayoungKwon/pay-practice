package practice.account.application.in;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import practice.account.domain.AccountType;
import practice.adaptor.out.AccountJpaRepository;
import practice.adaptor.out.entity.UserEntity;
import practice.common.util.RandomGenerator;

@SpringBootTest
class AccountServiceTest {

  @Autowired
  AccountService accountService;

  @MockitoBean
  RandomGenerator randomGenerator;

  @Autowired
  AccountJpaRepository accountJpaRepository;


  @Test
  void concurrencyTest() throws InterruptedException {
    // 처음 잔고 10000원 + 1000원씩 7번 입금 = 17000원 기대
    int numberOfThreads = 7;
    BigDecimal initBalance = accountJpaRepository.findByUserAndType(new UserEntity(1L),
        AccountType.MAIN.name()).getBalance();
    BigDecimal depositAmount = BigDecimal.valueOf(1000);

    when(randomGenerator.nextInt(10, 100)).thenReturn(2,7,4,1,2,3);
    when(randomGenerator.nextInt(0, 6)).thenReturn(0);

    ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
    CountDownLatch latch = new CountDownLatch(numberOfThreads);

    for (int i = 0; i < numberOfThreads; i++) {
      executorService.submit(() -> {
        try {
          accountService.depositToMainAccount(1L, depositAmount);
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await();
    executorService.shutdown();

    assertThat(accountJpaRepository.findByUserAndType(new UserEntity(1L), AccountType.MAIN.name()).getBalance())
        .isEqualTo(initBalance.add(depositAmount.multiply(BigDecimal.valueOf(numberOfThreads))));

  }
}
