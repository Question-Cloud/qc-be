package com.eager.questioncloud.creator.repository;

import com.eager.questioncloud.creator.dto.CreatorDto.CreatorInformation;
import com.eager.questioncloud.creator.model.Creator;

public interface CreatorRepository {
    Boolean existsById(Long creatorId);

    CreatorInformation getCreatorInformation(Long creatorId);

    Creator save(Creator creator);
}
