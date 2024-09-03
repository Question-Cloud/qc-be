package com.eager.questioncloud.user;

import com.eager.questioncloud.security.UserPrincipal;
import com.eager.questioncloud.user.Response.MyInformationResponse;
import com.eager.questioncloud.user.UserDto.MyInformation;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @GetMapping("/me")
    public MyInformationResponse getMyInformation(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return new MyInformationResponse(MyInformation.of(userPrincipal.getUser()));
    }
}
