package practice.adaptor.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import practice.account.application.in.CreateAccountUseCase;
import practice.account.application.in.TransactionUseCase;
import practice.common.dto.CreateAccountRequest;
import practice.common.dto.TransactionRequest;


@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

  private final CreateAccountUseCase createAccountUseCase;
  private final TransactionUseCase transactionUseCase;

  @PostMapping("/saving")
  public void createAccount(@RequestBody CreateAccountRequest createAccountRequest) {
    createAccountUseCase.createSavingAccount(createAccountRequest.getUserId());
  }

  @PatchMapping("/saving/deposit")
  public void depositToSavingAccount(@RequestBody TransactionRequest transactionRequest) {
    transactionUseCase.depositToSavingAccount(transactionRequest.getUserId(), transactionRequest.getAmount());
  }
}
