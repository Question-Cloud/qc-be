package com.eager.questioncloud.core.domain.creator.service;

import com.eager.questioncloud.core.domain.creator.dto.CreatorInformation;
import com.eager.questioncloud.core.domain.creator.implement.CreatorReader;
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
