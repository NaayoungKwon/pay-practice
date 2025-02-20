package practice.user.application.out;

import practice.user.domain.User;

public interface LoadUserPort {

  boolean hasUser(String name, String email);


}
