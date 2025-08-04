package practice.common.idempotency

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

@SpringBootTest
@Import(IdempotencyAoPTest.TestController::class)
internal class IdempotencyAoPTest {
    @Autowired
    private val testController: TestController? = null

    @MockitoBean
    private val redisTemplate: StringRedisTemplate? = null

    @Mock
    private val valueOperations: ValueOperations<String, String>? = null

    @Test
    @Throws(InterruptedException::class)
    fun IdempotencyDuplicateCheck2() {
        val IDEMPOTENCY_KEY = "test-key"
        val THREAD_COUNT = 4

        Mockito.`when`<ValueOperations<String, String>>(redisTemplate?.opsForValue()).thenReturn(valueOperations)
        Mockito.`when`<Boolean>(
            valueOperations?.setIfAbsent(
                "IDEMPOTENCY:$IDEMPOTENCY_KEY",
                "EXISTS",
                60L,
                TimeUnit.SECONDS
            )
        )
            .thenReturn(true)
            .thenReturn(false)
            .thenReturn(false)
            .thenReturn(false)

        val executorService: ExecutorService = Executors.newFixedThreadPool(THREAD_COUNT)
        val latch: CountDownLatch = CountDownLatch(THREAD_COUNT)
        val successCount: AtomicInteger = AtomicInteger()
        val conflictCount: AtomicInteger = AtomicInteger()

        for (i in 0..<THREAD_COUNT) {
            executorService.submit(Runnable {
                try {
                    val statusCode: HttpStatusCode = testController!!.test(IDEMPOTENCY_KEY).statusCode
                    if (statusCode === HttpStatus.OK) {
                        successCount.incrementAndGet()
                    } else if (statusCode === HttpStatus.CONFLICT) {
                        conflictCount.incrementAndGet()
                    }
                } finally {
                    latch.countDown()
                }
            })
        }

        latch.await()
        executorService.shutdown()

        Assertions.assertEquals(1, successCount.get())
        Assertions.assertEquals(3, conflictCount.get())
    }


    @RestController
    class TestController {
        @GetMapping("/test")
        @IdempotencyCheck("#idempotencyKey")
        fun test(@RequestHeader("Idempotency-Key") idempotencyKey: String?): ResponseEntity<*> {
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            }
            return ResponseEntity.status(HttpStatus.OK).build<Any>()
        }
    }
}
