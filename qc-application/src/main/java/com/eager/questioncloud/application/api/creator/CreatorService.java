package com.eager.questioncloud.application.api.creator;

import com.eager.questioncloud.core.domain.creator.CreatorInformation;
import com.eager.questioncloud.core.domain.creator.CreatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatorService {
    private final CreatorRepository creatorRepository;

    public CreatorInformation getCreatorInformation(Long creatorId) {
        return creatorRepository.getCreatorInformation(creatorId);
    }
}
