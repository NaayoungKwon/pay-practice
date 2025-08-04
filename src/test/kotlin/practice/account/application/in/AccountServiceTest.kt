package practice.account.application.`in`

import org.junit.jupiter.api.Assertions
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import practice.account.domain.AccountType
import practice.adaptor.out.AccountJpaRepository
import practice.adaptor.out.entity.UserEntity
import practice.common.util.RandomGenerator
import java.math.BigDecimal
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.test.Test

@SpringBootTest
internal class AccountServiceTest {
    @Autowired
    lateinit var accountService: AccountService

    @MockitoBean
    lateinit var randomGenerator: RandomGenerator

    @Autowired
    lateinit var accountJpaRepository: AccountJpaRepository


    @Test
    @Throws(InterruptedException::class)
    fun concurrencyTest() {
        // 처음 잔고 10000원 + 1000원씩 7번 입금 = 17000원 기대
        val numberOfThreads = 7
        val initBalance: BigDecimal = accountJpaRepository.findByUserAndType(
            UserEntity(1L),
            AccountType.MAIN.name
        ).balance!!
        val depositAmount: BigDecimal = BigDecimal.valueOf(1000)

        Mockito.`when`<Int>(randomGenerator.nextInt(10, 100)).thenReturn(2, 7, 4, 1, 2, 3)
        Mockito.`when`<Int>(randomGenerator.nextInt(0, 6)).thenReturn(0)

        val executorService: ExecutorService = Executors.newFixedThreadPool(numberOfThreads)
        val latch = CountDownLatch(numberOfThreads)

        for (i in 0..<numberOfThreads) {
            executorService.submit({
                try {
                    accountService.depositToMainAccount(1L, depositAmount)
                } catch (e: Exception) {
                    println(e.message)
                } finally {
                    latch.countDown()
                }

            })
        }

        latch.await()
        executorService.shutdown()

        val balance = accountJpaRepository.findByUserAndType(
            UserEntity(1L),
            AccountType.MAIN.name
        ).balance

        Assertions.assertEquals(
            balance, initBalance.add(depositAmount.multiply(BigDecimal.valueOf(numberOfThreads.toLong())))
        )
    }
}
