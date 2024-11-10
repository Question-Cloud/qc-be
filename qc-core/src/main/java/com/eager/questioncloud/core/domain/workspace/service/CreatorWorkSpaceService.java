package com.eager.questioncloud.core.domain.workspace.service;

import com.eager.questioncloud.core.domain.creator.implement.CreatorUpdater;
import com.eager.questioncloud.core.domain.creator.model.Creator;
import com.eager.questioncloud.core.domain.creator.vo.CreatorProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatorWorkSpaceService {
    private final CreatorUpdater creatorUpdater;

    public void updateCreatorProfile(Creator creator, CreatorProfile creatorProfile) {
        creatorUpdater.updateCreatorProfile(creator, creatorProfile);
    }
}
