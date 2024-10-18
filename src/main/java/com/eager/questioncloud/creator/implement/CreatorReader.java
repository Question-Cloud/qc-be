package com.eager.questioncloud.creator.implement;

import com.eager.questioncloud.creator.dto.CreatorDto.CreatorInformation;
import com.eager.questioncloud.creator.model.Creator;
import com.eager.questioncloud.creator.repository.CreatorRepository;
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

    public Creator getByUserId(Long userId) {
        return creatorRepository.findByUserId(userId);
    }

    public Creator getById(Long creatorId) {
        return creatorRepository.findById(creatorId);
    }
}
