package com.eager.questioncloud.application.creator;

import com.eager.questioncloud.domain.creator.CreatorInformation;
import com.eager.questioncloud.domain.creator.CreatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatorReader {
    private final CreatorRepository creatorRepository;

    public CreatorInformation getCreatorInformation(Long creatorId) {
        return creatorRepository.getCreatorInformation(creatorId);
    }

    public Boolean isExistsCreator(Long creatorId) {
        return creatorRepository.existsById(creatorId);
    }
}
