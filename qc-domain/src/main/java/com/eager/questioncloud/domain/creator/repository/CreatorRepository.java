package com.eager.questioncloud.domain.creator.repository;

import com.eager.questioncloud.domain.creator.dto.CreatorInformation;
import com.eager.questioncloud.domain.creator.model.Creator;

public interface CreatorRepository {
    Boolean existsById(Long creatorId);

    CreatorInformation getCreatorInformation(Long creatorId);

    Creator save(Creator creator);
}
