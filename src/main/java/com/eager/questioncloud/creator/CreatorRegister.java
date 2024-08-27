package com.eager.questioncloud.creator;

import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.question.Subject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatorRegister {
    private final CreatorRepository creatorRepository;

    public Creator register(Long userId, Subject mainSubject, String introduction) {
        if (creatorRepository.existsByUserId(userId)) {
            throw new CustomException(Error.ALREADY_REGISTER_CREATOR);
        }
        return creatorRepository.append(Creator.create(userId, mainSubject, introduction));
    }
}
