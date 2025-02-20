package practice.adaptor.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import practice.account.application.in.CreateAccountUseCase;


@RestController
@RequiredArgsConstructor
public class AccountController {

  private final CreateAccountUseCase createAccountUseCase;

  @GetMapping("/account")
  public void createAccount() {
//    userFacade.
  }
}
