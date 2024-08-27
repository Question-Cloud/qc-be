package com.eager.questioncloud.creator;

import com.eager.questioncloud.question.Subject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatorService {
    private final CreatorRegister creatorRegister;

    public Creator register(Long userId, Subject mainSubject, String introduction) {
        return creatorRegister.register(userId, mainSubject, introduction);
    }
}
