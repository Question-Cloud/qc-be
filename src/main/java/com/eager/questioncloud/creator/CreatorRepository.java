package com.eager.questioncloud.creator;

import com.eager.questioncloud.creator.CreatorDto.CreatorInformation;

public interface CreatorRepository {
    Creator append(Creator creator);

    Boolean existsByUserId(Long userId);

    CreatorInformation getCreatorInformation(Long creatorId);
}
