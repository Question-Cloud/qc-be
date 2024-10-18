package com.eager.questioncloud.creator.implement;

import com.eager.questioncloud.creator.model.Creator;
import com.eager.questioncloud.creator.repository.CreatorRepository;
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
