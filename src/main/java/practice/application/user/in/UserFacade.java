package practice.application.user.in;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.application.account.in.CreateAccountUseCase;

@Service
@RequiredArgsConstructor
public class UserFacade {

  private final CreateAccountUseCase createAccountUseCase;
//  private final RegisterUserUseCase registerUserUseCase;

}
