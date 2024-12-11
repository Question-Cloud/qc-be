package com.eager.questioncloud.application.api.workspace;

import com.eager.questioncloud.core.domain.creator.infrastructure.CreatorRepository;
import com.eager.questioncloud.core.domain.creator.model.Creator;
import com.eager.questioncloud.core.domain.creator.model.CreatorProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatorProfileService {
    private final CreatorRepository creatorRepository;

    public void updateCreatorProfile(Creator creator, CreatorProfile creatorProfile) {
        creator.updateProfile(creatorProfile);
        creatorRepository.save(creator);
    }
}
