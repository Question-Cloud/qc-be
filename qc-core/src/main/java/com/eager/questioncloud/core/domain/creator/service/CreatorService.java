package com.eager.questioncloud.core.domain.creator.service;

import com.eager.questioncloud.core.domain.creator.dto.CreatorDto.CreatorInformation;
import com.eager.questioncloud.core.domain.creator.implement.CreatorAppender;
import com.eager.questioncloud.core.domain.creator.implement.CreatorReader;
import com.eager.questioncloud.core.domain.creator.implement.CreatorUpdater;
import com.eager.questioncloud.core.domain.creator.model.Creator;
import com.eager.questioncloud.core.domain.creator.vo.CreatorProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatorService {
    private final CreatorAppender creatorAppender;
    private final CreatorReader creatorReader;
    private final CreatorUpdater creatorUpdater;

    public Creator register(Long userId, CreatorProfile creatorProfile) {
        return creatorAppender.append(Creator.create(userId, creatorProfile));
    }

    public CreatorInformation getCreatorInformation(Long creatorId) {
        return creatorReader.getCreatorInformation(creatorId);
    }

    public void updateCreatorProfile(Creator creator, CreatorProfile creatorProfile) {
        creator.updateProfile(creatorProfile);
        creatorUpdater.update(creator);
    }
}
