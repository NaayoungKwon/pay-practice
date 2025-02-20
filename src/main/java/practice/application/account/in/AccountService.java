package practice.application.account.in;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.application.account.out.LoadAccountPort;

@Service
@RequiredArgsConstructor
public class AccountService implements CreateAccountUseCase{

  private final LoadAccountPort loadAccountPort;


}
