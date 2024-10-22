package com.eager.questioncloud.domain.creator.implement;

import com.eager.questioncloud.domain.creator.dto.CreatorDto.CreatorInformation;
import com.eager.questioncloud.domain.creator.repository.CreatorRepository;
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
