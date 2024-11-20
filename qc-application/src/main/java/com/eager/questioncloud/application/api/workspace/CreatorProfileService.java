package com.eager.questioncloud.application.api.workspace;

import com.eager.questioncloud.domain.creator.Creator;
import com.eager.questioncloud.domain.creator.CreatorProfile;
import com.eager.questioncloud.domain.creator.CreatorRepository;
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
