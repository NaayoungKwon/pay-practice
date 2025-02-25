package practice.adaptor.in.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import practice.common.dto.UserRequest;
import practice.user.application.in.UserFacade;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

  private final UserFacade userFacade;

  @PostMapping()
  public void createUser(@Valid @RequestBody UserRequest user) {
    userFacade.createAccount(user);
  }
}
