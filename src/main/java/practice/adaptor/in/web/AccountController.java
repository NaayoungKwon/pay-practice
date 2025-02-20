package practice.adaptor.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import practice.account.application.in.CreateAccountUseCase;


@RestController
@RequiredArgsConstructor
public class AccountController {

  private final CreateAccountUseCase createAccountUseCase;

  @PostMapping("/account/saving")
  public void createAccount(@RequestParam("userId") Long userId) {
    createAccountUseCase.createSavingAccount(userId);
  }
}
