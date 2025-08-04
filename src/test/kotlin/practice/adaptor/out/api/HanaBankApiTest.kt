package practice.adaptor.out.api


import org.assertj.core.api.ThrowableAssert
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import practice.account.application.out.SaveTransactionPort
import practice.account.domain.ExternalAccount
import practice.common.util.RandomGenerator
import java.math.BigDecimal
import kotlin.test.Test

@ExtendWith(MockitoExtension::class)
class HanaBankApiTest {
    @InjectMocks
    lateinit var hanaBankApi: HanaBankApi

    @Mock
    lateinit var saveTransactionPortMock: SaveTransactionPort

    @Mock
    lateinit var randomGenerator: RandomGenerator

    @Test
    @DisplayName("정상적인 출금 테스트")
    fun normalExternalBankWithdraw() {
        val externalAccount: ExternalAccount = ExternalAccount()

        Mockito.`when`<Int>(randomGenerator.nextInt(10, 100)).thenReturn(2)
        Mockito.`when`<Int>(randomGenerator.nextInt(0, 6)).thenReturn(0)

        org.assertj.core.api.Assertions.assertThatNoException()
            .isThrownBy(
                ThrowableAssert.ThrowingCallable { hanaBankApi.withdraw(externalAccount, 1L, BigDecimal.TEN) })
    }

    @Test
    @DisplayName("출금 시 4xx 에러 발생")
    fun httpErrorWhenExternalBankWithdraw() {
        val externalAccount: ExternalAccount = ExternalAccount()

        Mockito.`when`<Int>(randomGenerator.nextInt(10, 100)).thenReturn(2)
        Mockito.`when`<Int>(randomGenerator.nextInt(0, 6)).thenReturn(4)

        org.assertj.core.api.Assertions.assertThatThrownBy(ThrowableAssert.ThrowingCallable {
            hanaBankApi.withdraw(
                externalAccount,
                1L,
                BigDecimal.TEN
            )
        })
            .isInstanceOf(java.lang.Exception::class.java)
    }
}
