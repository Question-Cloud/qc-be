package com.eager.questioncloud.core.domain.workspace.service;

import com.eager.questioncloud.core.domain.creator.model.Creator;
import com.eager.questioncloud.core.domain.creator.vo.CreatorProfile;
import com.eager.questioncloud.core.domain.workspace.implement.CreatorProfileUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatorProfileService {
    private final CreatorProfileUpdater creatorProfileUpdater;

    public void updateCreatorProfile(Creator creator, CreatorProfile creatorProfile) {
        creatorProfileUpdater.updateCreatorProfile(creator, creatorProfile);
    }
}
