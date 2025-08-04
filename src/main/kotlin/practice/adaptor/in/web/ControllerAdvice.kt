package practice.adaptor.`in`.web

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import practice.common.dto.CommonResponse
import practice.common.dto.ResponseCode
import practice.common.exception.ExternalAccountErrorException
import practice.common.exception.ExternalAccountLimitExceededException

private val log = KotlinLogging.logger { }

@RestControllerAdvice(basePackages = ["practice.adaptor.in.web"])
class ControllerAdvice {

    @ExceptionHandler(ExternalAccountLimitExceededException::class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    fun handleExternalLimitException(e: ExternalAccountLimitExceededException?): CommonResponse<*> {
        return buildCommonResponse(ResponseCode.NotEnoughAccountBalance)
    }

    @ExceptionHandler(ExternalAccountErrorException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleExternalLimitException(e: ExternalAccountErrorException?): CommonResponse<*> {
        return buildCommonResponse(ResponseCode.BankMaintenance)
    }

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleException(e: Exception): CommonResponse<*> {
        log.error(e) { e.message }
        return buildCommonResponse(ResponseCode.Fail, "서비스 중 에러가 발생했습니다.")
    }

    private fun buildCommonResponse(responseCode: ResponseCode): CommonResponse<*> {
        return CommonResponse<Any>(
            code = responseCode.name,
            message = responseCode.message
        )
    }

    private fun buildCommonResponse(responseCode: ResponseCode, message: String): CommonResponse<*> {
        return CommonResponse<Any>(
            code = responseCode.name,
            message = message
        )
    }
}
