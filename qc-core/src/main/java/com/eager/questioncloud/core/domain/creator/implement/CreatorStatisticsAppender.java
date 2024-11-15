package com.eager.questioncloud.core.domain.creator.implement;

import com.eager.questioncloud.core.domain.creator.event.RegisteredCreatorEvent;
import com.eager.questioncloud.core.domain.creator.model.CreatorStatistics;
import com.eager.questioncloud.core.domain.creator.repository.CreatorStatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatorStatisticsAppender {
    private final CreatorStatisticsRepository creatorStatisticsRepository;

    @EventListener
    public void appendCreatorStatistics(RegisteredCreatorEvent event) {
        creatorStatisticsRepository.save(CreatorStatistics.create(event.getCreator().getId()));
    }
}
