package com.eager.questioncloud.creator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CreatorRepositoryImpl implements CreatorRepository {
    private final CreatorJpaRepository creatorJpaRepository;

    @Override
    public Creator append(Creator creator) {
        return creatorJpaRepository.save(creator.toEntity()).toModel();
    }

    @Override
    public Boolean existsByUserId(Long userId) {
        return creatorJpaRepository.existsByUserId(userId);
    }
}
