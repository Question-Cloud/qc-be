package com.eager.questioncloud.core.domain.creator;

import static org.assertj.core.api.Assertions.assertThat;

import com.eager.questioncloud.core.domain.creator.implement.CreatorStatisticsProcessor;
import com.eager.questioncloud.core.domain.creator.infrastructure.CreatorRepository;
import com.eager.questioncloud.core.domain.creator.infrastructure.CreatorStatisticsRepository;
import com.eager.questioncloud.core.domain.creator.model.Creator;
import com.eager.questioncloud.core.domain.creator.model.CreatorStatistics;
import com.eager.questioncloud.core.domain.question.QuestionBuilder;
import com.eager.questioncloud.core.domain.question.infrastructure.QuestionRepository;
import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.domain.review.DeletedReviewEvent;
import com.eager.questioncloud.core.domain.review.ModifiedReviewEvent;
import com.eager.questioncloud.core.domain.review.RegisteredReviewEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class CreatorStatisticsProcessorTest {
    @Autowired
    private CreatorStatisticsProcessor creatorStatisticsProcessor;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private CreatorStatisticsRepository creatorStatisticsRepository;

    @Autowired
    private CreatorRepository creatorRepository;

    @AfterEach
    void tearDown() {
        questionRepository.deleteAllInBatch();
        creatorRepository.deleteAllInBatch();
        creatorStatisticsRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("리뷰 등록 이벤트 크리에이터 평점 통계 업데이트 동시성 이슈 테스트")
    void creatorStatisticsConcurrencyTestWhenRegisteredReviewEvent() throws InterruptedException {
        //given
        Creator creator = creatorRepository.save(CreatorBuilder.builder().build().toCreator());
        creatorStatisticsRepository.save(
            CreatorStatisticsBuilder
                .builder()
                .creatorId(creator.getId())
                .build()
                .toCreatorStatistics()
        );

        Question question = questionRepository.save(
            QuestionBuilder
                .builder()
                .creatorId(creator.getId())
                .build()
                .toQuestion()
        );

        RegisteredReviewEvent event = RegisteredReviewEvent.create(question.getId(), 4);

        //when
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    creatorStatisticsProcessor.updateCreatorReviewStatistics(event);
                } catch (Exception ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        //then
        CreatorStatistics creatorStatistics = creatorStatisticsRepository.findByCreatorId(creator.getId());

        assertThat(creatorStatistics.getTotalReviewRate()).isEqualTo(400);
        assertThat(creatorStatistics.getAverageRateOfReview()).isEqualTo(4.0);
    }

    @Test
    @DisplayName("리뷰 수정 이벤트 크리에이터 평점 통계 업데이트 동시성 이슈 테스트")
    void creatorStatisticsConcurrencyTestWhenModifiedReviewEvent() throws InterruptedException {
        Creator creator = creatorRepository.save(CreatorBuilder.builder().build().toCreator());
        creatorStatisticsRepository.save(
            CreatorStatisticsBuilder
                .builder()
                .creatorId(creator.getId())
                .reviewCount(100)
                .build()
                .toCreatorStatistics()
        );

        Question question = questionRepository.save(
            QuestionBuilder
                .builder()
                .creatorId(creator.getId())
                .build()
                .toQuestion()
        );

        ModifiedReviewEvent event = ModifiedReviewEvent.create(question.getId(), 3);

        //when
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    creatorStatisticsProcessor.updateCreatorReviewStatistics(event);
                } catch (Exception ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        //then
        CreatorStatistics creatorStatistics = creatorStatisticsRepository.findByCreatorId(creator.getId());

        assertThat(creatorStatistics.getTotalReviewRate()).isEqualTo(300);
        assertThat(creatorStatistics.getAverageRateOfReview()).isEqualTo(3.0);
    }

    @Test
    @DisplayName("리뷰 삭제 이벤트 크리에이터 평점 통계 업데이트 동시성 이슈 테스트")
    void creatorStatisticsConcurrencyTestWhenDeletedReviewEvent() throws InterruptedException {
        Creator creator = creatorRepository.save(CreatorBuilder.builder().build().toCreator());
        creatorStatisticsRepository.save(
            CreatorStatisticsBuilder
                .builder()
                .creatorId(creator.getId())
                .reviewCount(100)
                .totalReviewRate(400)
                .build()
                .toCreatorStatistics()
        );

        Question question = questionRepository.save(
            QuestionBuilder
                .builder()
                .creatorId(creator.getId())
                .build()
                .toQuestion()
        );

        DeletedReviewEvent event = DeletedReviewEvent.create(question.getId(), 4);

        //when
        int threadCount = 80;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    creatorStatisticsProcessor.updateCreatorReviewStatistics(event);
                } catch (Exception ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        //then
        CreatorStatistics creatorStatistics = creatorStatisticsRepository.findByCreatorId(creator.getId());

        assertThat(creatorStatistics.getTotalReviewRate()).isEqualTo(80);
        assertThat(creatorStatistics.getReviewCount()).isEqualTo(20);
        assertThat(creatorStatistics.getAverageRateOfReview()).isEqualTo(4.0);
    }

    @Test
    @DisplayName("리뷰 추가, 수정, 삭제 이벤트가 동시에 일어나는 경우 크리에이터 통계 업데이트 동시성 테스트")
    void creatorStatisticsConcurrencyTestWhenMultipleEvent() throws InterruptedException {
        //given
        Creator creator = creatorRepository.save(CreatorBuilder.builder().build().toCreator());
        creatorStatisticsRepository.save(
            CreatorStatisticsBuilder
                .builder()
                .creatorId(creator.getId())
                .reviewCount(100)
                .totalReviewRate(100)
                .averageRateOfReview(1.0)
                .build()
                .toCreatorStatistics()
        );

        Question question = questionRepository.save(
            QuestionBuilder
                .builder()
                .creatorId(creator.getId())
                .build()
                .toQuestion()
        );

        RegisteredReviewEvent registeredReviewEvent = RegisteredReviewEvent.create(question.getId(), 1);
        ModifiedReviewEvent modifiedReviewEvent = ModifiedReviewEvent.create(question.getId(), 1);
        DeletedReviewEvent deletedReviewEvent = DeletedReviewEvent.create(question.getId(), 1);

        //when
        int threadCount = 120;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < 40; i++) {
            executorService.execute(() -> {
                try {
                    creatorStatisticsProcessor.updateCreatorReviewStatistics(registeredReviewEvent);
                } catch (Exception ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }

        for (int i = 0; i < 40; i++) {
            executorService.execute(() -> {
                try {
                    creatorStatisticsProcessor.updateCreatorReviewStatistics(modifiedReviewEvent);
                } catch (Exception ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }

        for (int i = 0; i < 40; i++) {
            executorService.execute(() -> {
                try {
                    creatorStatisticsProcessor.updateCreatorReviewStatistics(deletedReviewEvent);
                } catch (Exception ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        //then
        CreatorStatistics creatorStatistics = creatorStatisticsRepository.findByCreatorId(creator.getId());
        assertThat(creatorStatistics.getTotalReviewRate()).isEqualTo(140);
        assertThat(creatorStatistics.getReviewCount()).isEqualTo(100);
        assertThat(creatorStatistics.getAverageRateOfReview()).isEqualTo(1.4);
    }
}