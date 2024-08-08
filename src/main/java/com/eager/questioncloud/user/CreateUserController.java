package com.eager.questioncloud.user;

import com.eager.questioncloud.user.Request.CreateUserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class CreateUserController {
    private final UserService userService;

    @PostMapping
    public void createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        createUserRequest.validate();
        userService.create(User.create(createUserRequest));
    }
}
