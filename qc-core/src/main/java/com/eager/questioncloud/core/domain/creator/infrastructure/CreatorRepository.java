package com.eager.questioncloud.core.domain.creator.infrastructure;

import com.eager.questioncloud.core.domain.creator.dto.CreatorInformation;
import com.eager.questioncloud.core.domain.creator.model.Creator;

public interface CreatorRepository {
    Boolean existsById(Long creatorId);

    CreatorInformation getCreatorInformation(Long creatorId);

    Creator save(Creator creator);

    void deleteAllInBatch();
}
