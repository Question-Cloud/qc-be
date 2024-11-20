package com.eager.questioncloud.application.api.creator;

import com.eager.questioncloud.domain.creator.CreatorStatistics;
import com.eager.questioncloud.domain.creator.CreatorStatisticsRepository;
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
