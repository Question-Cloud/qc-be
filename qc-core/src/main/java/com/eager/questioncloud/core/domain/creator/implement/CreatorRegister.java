package com.eager.questioncloud.core.domain.creator.implement;

import com.eager.questioncloud.core.domain.creator.model.Creator;
import com.eager.questioncloud.core.domain.creator.repository.CreatorRepository;
import com.eager.questioncloud.core.domain.creator.vo.CreatorProfile;
import com.eager.questioncloud.core.domain.user.implement.UserUpdater;
import com.eager.questioncloud.core.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CreatorRegister {
    private final CreatorRepository creatorRepository;
    private final UserUpdater userUpdater;

    @Transactional
    public Creator register(User user, CreatorProfile creatorProfile) {
        userUpdater.setCreator(user);
        return creatorRepository.save(Creator.create(user.getUid(), creatorProfile));
    }
}
