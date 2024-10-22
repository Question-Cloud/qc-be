package com.eager.questioncloud.domain.creator.implement;

import com.eager.questioncloud.domain.creator.model.Creator;
import com.eager.questioncloud.domain.creator.repository.CreatorRepository;
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
