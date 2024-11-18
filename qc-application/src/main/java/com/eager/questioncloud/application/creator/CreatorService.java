package com.eager.questioncloud.application.creator;

import com.eager.questioncloud.domain.creator.CreatorInformation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatorService {
    private final CreatorReader creatorReader;

    public CreatorInformation getCreatorInformation(Long creatorId) {
        return creatorReader.getCreatorInformation(creatorId);
    }
}
