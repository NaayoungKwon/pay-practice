package practice.common.dto

import jakarta.validation.constraints.NotBlank

data class UserRequest(
    @field:NotBlank
    var name: String,

    @field:NotBlank
    var email: String,

    @field:NotBlank
    var externalBank: String,

    @field:NotBlank
    var externalAccountNumber: String
)
