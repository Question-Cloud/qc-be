package com.eager.questioncloud.library;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserQuestionLibraryRepositoryImpl implements UserQuestionLibraryRepository {
    private final UserQuestionLibraryJpaRepository userQuestionLibraryJpaRepository;

    @Override
    public List<UserQuestionLibrary> append(List<UserQuestionLibrary> userQuestionLibraries) {
        return UserQuestionLibraryEntity.toModel(userQuestionLibraryJpaRepository.saveAll(UserQuestionLibrary.toEntity(userQuestionLibraries)));
    }
}
