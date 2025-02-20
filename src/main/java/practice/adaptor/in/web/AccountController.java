package practice.adaptor.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import practice.application.account.in.CreateAccountUseCase;
import practice.application.user.in.UserFacade;


@RestController
@RequiredArgsConstructor
public class AccountController {

  private final CreateAccountUseCase createAccountUseCase;

  @GetMapping("/account")
  public void createAccount() {
//    userFacade.
  }
}
