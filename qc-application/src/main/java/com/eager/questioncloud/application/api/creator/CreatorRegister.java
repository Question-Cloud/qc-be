package com.eager.questioncloud.application.api.creator;

import com.eager.questioncloud.core.domain.creator.infrastructure.CreatorRepository;
import com.eager.questioncloud.core.domain.creator.model.Creator;
import com.eager.questioncloud.core.domain.creator.model.CreatorProfile;
import com.eager.questioncloud.core.domain.user.User;
import com.eager.questioncloud.core.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreatorRegister {
    private final CreatorRepository creatorRepository;
    private final UserRepository userRepository;

    @Transactional
    public Creator register(User user, CreatorProfile creatorProfile) {
        setCreator(user);
        return creatorRepository.save(Creator.create(user.getUid(), creatorProfile));
    }

    private void setCreator(User user) {
        user.setCreator();
        userRepository.save(user);
    }
}
