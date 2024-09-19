package com.eager.questioncloud.creator;

import com.eager.questioncloud.creator.CreatorDto.CreatorInformation;

public interface CreatorRepository {
    Boolean existsByUserId(Long userId);

    Boolean existsById(Long creatorId);

    CreatorInformation getCreatorInformation(Long creatorId);

    Creator findByUserId(Long userId);

    Creator save(Creator creator);
}
