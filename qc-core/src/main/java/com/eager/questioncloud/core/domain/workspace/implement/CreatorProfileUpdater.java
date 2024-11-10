package com.eager.questioncloud.core.domain.workspace.implement;

import com.eager.questioncloud.core.domain.creator.model.Creator;
import com.eager.questioncloud.core.domain.creator.repository.CreatorRepository;
import com.eager.questioncloud.core.domain.creator.vo.CreatorProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatorProfileUpdater {
    private final CreatorRepository creatorRepository;

    public void updateCreatorProfile(Creator creator, CreatorProfile creatorProfile) {
        creator.updateProfile(creatorProfile);
        creatorRepository.save(creator);
    }
}
