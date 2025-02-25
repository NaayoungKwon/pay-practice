package practice.adaptor.in.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import practice.adaptor.in.web.dto.CommonResponse;
import practice.adaptor.in.web.dto.ResponseCode;
import practice.common.exception.ExternalAccountErrorException;
import practice.common.exception.ExternalAccountLimitExceededException;

@Slf4j
@RestControllerAdvice(basePackages = "practice.adaptor.in.web")
public class ControllerAdvice {

  @ExceptionHandler(ExternalAccountLimitExceededException.class)
  @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
  public CommonResponse<?> handleExternalLimitException(ExternalAccountLimitExceededException e) {
    return buildCommonResponse(ResponseCode.NotEnoughAccountBalance);
  }

  @ExceptionHandler(ExternalAccountErrorException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public CommonResponse<?> handleExternalLimitException(ExternalAccountErrorException e) {
    return buildCommonResponse(ResponseCode.BankMaintenance);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public CommonResponse<?> handleException(Exception e) {
    log.error("{}", e.getMessage(), e);
    return buildCommonResponse(ResponseCode.Fail, "서비스 중 에러가 발생했습니다.");
  }

  private CommonResponse<?> buildCommonResponse(ResponseCode responseCode) {
    return CommonResponse.builder()
        .code(responseCode.name())
        .message(responseCode.getMessage())
        .build();
  }

  private CommonResponse<?> buildCommonResponse(ResponseCode responseCode, String message) {
    return CommonResponse.builder()
        .code(responseCode.name())
        .message(message)
        .build();
  }


}
