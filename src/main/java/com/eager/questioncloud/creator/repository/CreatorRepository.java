package com.eager.questioncloud.creator.repository;

import com.eager.questioncloud.creator.domain.Creator;
import com.eager.questioncloud.creator.dto.CreatorDto.CreatorInformation;

public interface CreatorRepository {
    Boolean existsByUserId(Long userId);

    Boolean existsById(Long creatorId);

    CreatorInformation getCreatorInformation(Long creatorId);

    Creator findByUserId(Long userId);

    Creator save(Creator creator);
}