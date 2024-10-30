package com.eager.questioncloud.core.domain.creator.implement;

import com.eager.questioncloud.core.domain.creator.model.Creator;
import com.eager.questioncloud.core.domain.creator.repository.CreatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatorAppender {
    private final CreatorRepository creatorRepository;

    public Creator append(Creator creator) {
        return creatorRepository.save(creator);
    }
}
