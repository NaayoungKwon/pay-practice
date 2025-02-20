package practice.user.application.in;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.adaptor.out.entity.UserEntity;
import practice.user.application.out.LoadUserPort;
import practice.user.application.out.RegisterUserPort;
import practice.user.domain.User;

@Service
@RequiredArgsConstructor
public class UserService implements RegisterUserUseCase {

  private final LoadUserPort loadUserPort;
  private final RegisterUserPort registerUserPort;


  @Override
  public boolean alreadyExists(String name, String email) {
    return loadUserPort.hasUser(name, email);
  }

  @Override
  public User register(String name, String email) {
    return registerUserPort.register(name, email);
  }
}
