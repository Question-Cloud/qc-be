package com.eager.questioncloud.creator.implement;

import com.eager.questioncloud.creator.domain.Creator;
import com.eager.questioncloud.creator.repository.CreatorRepository;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatorAppender {
    private final CreatorRepository creatorRepository;

    public Creator append(Creator creator) {
        if (creatorRepository.existsByUserId(creator.getUserId())) {
            throw new CustomException(Error.ALREADY_REGISTER_CREATOR);
        }
        return creatorRepository.save(creator);
    }
}
