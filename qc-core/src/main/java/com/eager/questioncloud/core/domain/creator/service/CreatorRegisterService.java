package com.eager.questioncloud.core.domain.creator.service;

import com.eager.questioncloud.core.domain.creator.implement.CreatorRegister;
import com.eager.questioncloud.core.domain.creator.model.Creator;
import com.eager.questioncloud.core.domain.creator.vo.CreatorProfile;
import com.eager.questioncloud.core.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatorRegisterService {
    private final CreatorRegister creatorRegister;

    public Creator register(User user, CreatorProfile creatorProfile) {
        return creatorRegister.register(user, creatorProfile);
    }
}