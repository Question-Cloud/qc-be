package com.eager.questioncloud.creator;

import com.eager.questioncloud.creator.CreatorDto.CreatorInformation;

public interface CreatorRepository {
    Creator append(Creator creator);

    Boolean existsByUserId(Long userId);

    Boolean existsById(Long creatorId);

    CreatorInformation getCreatorInformation(Long creatorId);

    Creator findByUserId(Long userId);
}
