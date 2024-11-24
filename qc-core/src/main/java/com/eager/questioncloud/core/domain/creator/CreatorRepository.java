package com.eager.questioncloud.core.domain.creator;

public interface CreatorRepository {
    Boolean existsById(Long creatorId);

    CreatorInformation getCreatorInformation(Long creatorId);

    Creator save(Creator creator);
}
