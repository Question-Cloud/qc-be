package com.eager.questioncloud.creator;

import com.eager.questioncloud.creator.CreatorDto.CreatorInformation;
import com.eager.questioncloud.creator.CreatorDto.MyCreatorInformation;
import com.eager.questioncloud.question.Subject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatorService {
    private final CreatorRegister creatorRegister;
    private final CreatorReader creatorReader;
    private final CreatorUpdater creatorUpdater;

    public Creator register(Long userId, Subject mainSubject, String introduction) {
        return creatorRegister.register(userId, mainSubject, introduction);
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
