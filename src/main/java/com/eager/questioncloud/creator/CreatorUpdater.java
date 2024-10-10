package com.eager.questioncloud.creator;

import com.eager.questioncloud.question.domain.Subject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatorUpdater {
    private final CreatorRepository creatorRepository;

    public void update(Creator creator, Subject mainSubject, String introduction) {
        creator.update(mainSubject, introduction);
        creatorRepository.save(creator);
    }
}
