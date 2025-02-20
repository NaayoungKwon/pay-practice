package practice.user.application.in;

import practice.user.domain.User;

public interface RegisterUserUseCase {

  boolean alreadyExists(String name, String email);
  User register(String name, String email);

}
