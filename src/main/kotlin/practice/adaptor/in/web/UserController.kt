package practice.adaptor.`in`.web

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import practice.common.dto.UserRequest
import practice.user.application.`in`.UserFacade

@RestController
@RequestMapping("/users")
class UserController(
    private val userFacade: UserFacade
) {

    @PostMapping
    fun createUser(@RequestBody user: @Valid UserRequest) {
        userFacade.createAccount(user)
    }
}
