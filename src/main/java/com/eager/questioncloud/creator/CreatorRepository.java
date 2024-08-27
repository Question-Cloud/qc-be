package com.eager.questioncloud.creator;

public interface CreatorRepository {
    Creator append(Creator creator);

    Boolean existsByUserId(Long userId);
}
