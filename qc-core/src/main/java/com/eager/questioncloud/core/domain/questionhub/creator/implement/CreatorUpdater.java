package com.eager.questioncloud.core.domain.questionhub.creator.implement;

import com.eager.questioncloud.core.domain.questionhub.creator.model.Creator;
import com.eager.questioncloud.core.domain.questionhub.creator.repository.CreatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatorUpdater {
    private final CreatorRepository creatorRepository;

    public void update(Creator creator) {
        creatorRepository.save(creator);
    }
}
