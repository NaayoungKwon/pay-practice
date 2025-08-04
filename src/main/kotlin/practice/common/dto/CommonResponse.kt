package practice.common.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CommonResponse<T>(
    val code: String = ResponseCode.Success.name,
    val message: String = ResponseCode.Success.message,
    val detail: T? = null
)
