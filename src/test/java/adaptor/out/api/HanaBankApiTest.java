package adaptor.out.api;


import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import practice.account.application.out.SaveTransactionPort;
import practice.account.domain.ExternalAccount;
import practice.adaptor.out.api.HanaBankApi;
import practice.common.util.RandomGenerator;

@ExtendWith(MockitoExtension.class)
public class HanaBankApiTest {

  @InjectMocks
  HanaBankApi hanaBankApi;

  @Mock
  SaveTransactionPort saveTransactionPortMock;

  @Mock
  RandomGenerator randomGenerator;

  @Test
  @DisplayName("정상적인 출금 테스트")
  void normalExternalBankWithdraw() {
    ExternalAccount externalAccount = ExternalAccount.builder().build();

    when(randomGenerator.nextInt(10, 100)).thenReturn(2);
    when(randomGenerator.nextInt(0, 6)).thenReturn(0);

    assertThatNoException()
        .isThrownBy(
            () -> hanaBankApi.withdraw(externalAccount, 1L, BigDecimal.TEN));
  }

  @Test
  @DisplayName("출금 시 4xx 에러 발생")
  void httpErrorWhenExternalBankWithdraw() {
    ExternalAccount externalAccount = ExternalAccount.builder().build();

    when(randomGenerator.nextInt(10, 100)).thenReturn(2);
    when(randomGenerator.nextInt(0, 6)).thenReturn(4);

    assertThatThrownBy(() -> hanaBankApi.withdraw(externalAccount, 1L, BigDecimal.TEN))
        .isInstanceOf(Exception.class);

  }


}
