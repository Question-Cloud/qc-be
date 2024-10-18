package com.eager.questioncloud.creator.implement;

import com.eager.questioncloud.creator.model.Creator;
import com.eager.questioncloud.creator.repository.CreatorRepository;
import com.eager.questioncloud.question.vo.Subject;
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
