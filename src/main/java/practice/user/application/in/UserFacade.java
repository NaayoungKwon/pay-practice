package practice.user.application.in;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.adaptor.in.web.dto.UserRequest;
import practice.account.application.in.CreateAccountUseCase;
import practice.user.domain.User;

@Service
@RequiredArgsConstructor
public class UserFacade {

  private final CreateAccountUseCase createAccountUseCase;
  private final RegisterUserUseCase registerUserUseCase;

  public void createAccount(UserRequest userRequest) {
    String name = userRequest.getName();
    String email = userRequest.getEmail();

    if (registerUserUseCase.alreadyExists(name, email)) {
      return;
    }

    User user = registerUserUseCase.register(name, email);
    createAccountUseCase.createMainAccount(user.getId());
  }

}
