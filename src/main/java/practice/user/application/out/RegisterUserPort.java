package practice.user.application.out;

import practice.user.domain.User;

public interface RegisterUserPort {
  User register(String name, String email);

}
