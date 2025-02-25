package practice.user.application.in;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.adaptor.in.web.dto.UserRequest;
import practice.account.application.in.CreateAccountUseCase;
import practice.user.domain.User;

@Service
@RequiredArgsConstructor
public class UserFacade {

  private final CreateAccountUseCase createAccountUseCase;
  private final RegisterUserUseCase registerUserUseCase;

  @Transactional
  public void createAccount(UserRequest userRequest) {
    String name = userRequest.getName();
    String email = userRequest.getEmail();

    if (registerUserUseCase.alreadyExists(name, email)) {
      return;
    }

    User user = registerUserUseCase.register(name, email);
    createAccountUseCase.createMainAccount(user.getId());
    createAccountUseCase.registerExternalAccount(user.getId(), userRequest.getExternalBank(), userRequest.getExternalAccountNumber());
  }

}
