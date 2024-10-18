package com.eager.questioncloud.creator.implement;

import com.eager.questioncloud.creator.model.Creator;
import com.eager.questioncloud.creator.repository.CreatorRepository;
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
