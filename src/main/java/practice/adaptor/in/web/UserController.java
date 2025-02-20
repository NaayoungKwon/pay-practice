package practice.adaptor.in.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import practice.adaptor.in.web.dto.UserRequest;
import practice.user.application.in.UserFacade;


@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserFacade userFacade;

  @PostMapping("/user")
  public void createUser(@Valid UserRequest user) {
    userFacade.createAccount(user);
  }
}
