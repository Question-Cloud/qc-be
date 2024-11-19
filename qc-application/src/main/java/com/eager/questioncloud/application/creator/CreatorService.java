package com.eager.questioncloud.application.creator;

import com.eager.questioncloud.domain.creator.CreatorInformation;
import com.eager.questioncloud.domain.creator.CreatorRepository;
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
