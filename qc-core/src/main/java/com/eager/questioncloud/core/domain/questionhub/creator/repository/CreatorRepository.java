package com.eager.questioncloud.core.domain.questionhub.creator.repository;

import com.eager.questioncloud.core.domain.questionhub.creator.dto.CreatorDto.CreatorInformation;
import com.eager.questioncloud.core.domain.questionhub.creator.model.Creator;

public interface CreatorRepository {
    Boolean existsById(Long creatorId);

    CreatorInformation getCreatorInformation(Long creatorId);

    Creator save(Creator creator);
}
