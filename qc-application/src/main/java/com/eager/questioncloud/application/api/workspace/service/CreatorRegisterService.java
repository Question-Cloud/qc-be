package com.eager.questioncloud.application.api.workspace.service;

import com.eager.questioncloud.application.api.workspace.implement.CreatorRegister;
import com.eager.questioncloud.core.domain.creator.implement.CreatorStatisticsProcessor;
import com.eager.questioncloud.core.domain.creator.model.Creator;
import com.eager.questioncloud.core.domain.creator.model.CreatorProfile;
import com.eager.questioncloud.core.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatorRegisterService {
    private final CreatorRegister creatorRegister;
    private final CreatorStatisticsProcessor creatorStatisticsProcessor;

    public Creator register(User user, CreatorProfile creatorProfile) {
        Creator creator = creatorRegister.register(user, creatorProfile);
        creatorStatisticsProcessor.initializeCreatorStatistics(creator.getId());
        return creator;
    }
}
