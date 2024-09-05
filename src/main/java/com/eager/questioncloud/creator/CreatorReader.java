package com.eager.questioncloud.creator;

import com.eager.questioncloud.creator.CreatorDto.CreatorInformation;
import com.eager.questioncloud.creator.CreatorDto.MyCreatorInformation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatorReader {
    private final CreatorRepository creatorRepository;

    public MyCreatorInformation getMyCreatorInformation(Long userId) {
        Creator me = creatorRepository.findByUserId(userId);
        return new MyCreatorInformation(me.getMainSubject(), me.getIntroduction());
    }

    public CreatorInformation getCreatorInformation(Long creatorId) {
        return creatorRepository.getCreatorInformation(creatorId);
    }

    public Boolean isExistsCreator(Long creatorId) {
        return creatorRepository.existsById(creatorId);
    }

    public Creator getByUserId(Long userId) {
        return creatorRepository.findByUserId(userId);
    }
}
