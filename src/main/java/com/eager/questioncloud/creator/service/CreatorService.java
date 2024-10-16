package com.eager.questioncloud.creator.service;

import com.eager.questioncloud.creator.domain.Creator;
import com.eager.questioncloud.creator.dto.CreatorDto.CreatorInformation;
import com.eager.questioncloud.creator.dto.CreatorDto.MyCreatorInformation;
import com.eager.questioncloud.creator.implement.CreatorAppender;
import com.eager.questioncloud.creator.implement.CreatorReader;
import com.eager.questioncloud.creator.implement.CreatorUpdater;
import com.eager.questioncloud.question.model.Subject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatorService {
    private final CreatorAppender creatorAppender;
    private final CreatorReader creatorReader;
    private final CreatorUpdater creatorUpdater;

    public Creator register(Long userId, Subject mainSubject, String introduction) {
        return creatorAppender.append(Creator.create(userId, mainSubject, introduction));
    }

    public CreatorInformation getCreatorInformation(Long creatorId) {
        return creatorReader.getCreatorInformation(creatorId);
    }

    public MyCreatorInformation getMyCreatorInformation(Long userId) {
        return creatorReader.getMyCreatorInformation(userId);
    }

    public void updateMyCreatorInformation(Long userId, Subject mainSubject, String introduction) {
        Creator creator = creatorReader.getByUserId(userId);
        creatorUpdater.update(creator, mainSubject, introduction);
    }
}
