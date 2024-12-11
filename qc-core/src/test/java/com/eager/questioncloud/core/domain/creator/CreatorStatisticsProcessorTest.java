package com.eager.questioncloud.core.domain.creator;

import static org.assertj.core.api.Assertions.assertThat;

import com.eager.questioncloud.core.domain.question.Question;
import com.eager.questioncloud.core.domain.question.QuestionContent;
import com.eager.questioncloud.core.domain.question.QuestionEntity;
import com.eager.questioncloud.core.domain.question.QuestionJpaRepository;
import com.eager.questioncloud.core.domain.question.Subject;
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
    private QuestionJpaRepository questionJpaRepository;

    @Autowired
    private CreatorStatisticsJpaRepository creatorStatisticsJpaRepository;

    @Autowired
    private CreatorJpaRepository creatorJpaRepository;

    @AfterEach
    void tearDown() {
        questionJpaRepository.deleteAllInBatch();
        creatorJpaRepository.deleteAllInBatch();
        creatorStatisticsJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("리뷰 등록 시 크리에이터 평점 통계 업데이트 동시성 이슈 테스트")
    void creatorStatisticsConcurrencyTestWhenRegisteredReview() throws InterruptedException {
        //given
        Creator creator = creatorJpaRepository.save(
            CreatorEntity.from(Creator.create(1L, CreatorProfile.create(Subject.Mathematics, "tt")))
        ).toModel();

        creatorStatisticsJpaRepository.save(
            CreatorStatisticsEntity.from(new CreatorStatistics(creator.getId(), 0, 0, 0, 0, 0.0))
        ).toModel();

        Question question = questionJpaRepository.save(
            QuestionEntity.from(Question.create(creator.getId(), QuestionContent.builder().build()))
        ).toModel();

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
        CreatorStatistics creatorStatistics = creatorStatisticsJpaRepository.findByCreatorId(creator.getId())
            .get().toModel();

        assertThat(creatorStatistics.getTotalReviewRate()).isEqualTo(400);
        assertThat(creatorStatistics.getAverageRateOfReview()).isEqualTo(4.0);
    }

    @Test
    @DisplayName("리뷰 수정 시 크리에이터 평점 통계 업데이트 동시성 이슈 테스트")
    void creatorStatisticsConcurrencyTestWhenModifiedReview() throws InterruptedException {
        Creator creator = creatorJpaRepository.save(
            CreatorEntity.from(Creator.create(1L, CreatorProfile.create(Subject.Mathematics, "tt")))
        ).toModel();

        creatorStatisticsJpaRepository.save(
            CreatorStatisticsEntity.from(new CreatorStatistics(creator.getId(), 0, 0, 100, 0, 0.0))
        ).toModel();

        Question question = questionJpaRepository.save(
            QuestionEntity.from(Question.create(creator.getId(), QuestionContent.builder().build()))
        ).toModel();

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
        CreatorStatistics creatorStatistics = creatorStatisticsJpaRepository.findByCreatorId(creator.getId())
            .get().toModel();

        assertThat(creatorStatistics.getTotalReviewRate()).isEqualTo(300);
        assertThat(creatorStatistics.getAverageRateOfReview()).isEqualTo(3.0);
    }

    @Test
    @DisplayName("리뷰 삭제 시 크리에이터 평점 통계 업데이트 동시성 이슈 테스트")
    void creatorStatisticsConcurrencyTestWhenDeletedReview() throws InterruptedException {
        Creator creator = creatorJpaRepository.save(
            CreatorEntity.from(Creator.create(1L, CreatorProfile.create(Subject.Mathematics, "tt")))
        ).toModel();

        creatorStatisticsJpaRepository.save(
            CreatorStatisticsEntity.from(new CreatorStatistics(creator.getId(), 0, 0, 100, 400, 4.0))
        ).toModel();

        Question question = questionJpaRepository.save(
            QuestionEntity.from(Question.create(creator.getId(), QuestionContent.builder().build()))
        ).toModel();

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
        CreatorStatistics creatorStatistics = creatorStatisticsJpaRepository.findByCreatorId(creator.getId())
            .get().toModel();

        assertThat(creatorStatistics.getTotalReviewRate()).isEqualTo(80);
        assertThat(creatorStatistics.getReviewCount()).isEqualTo(20);
        assertThat(creatorStatistics.getAverageRateOfReview()).isEqualTo(4.0);
    }

    @Test
    @DisplayName("리뷰 추가, 수정, 삭제가 동시에 일어나는 경우 크리에이터 통계 업데이트 동시성 테스트")
    void creatorStatisticsConcurrencyTestWhenMultipleEvent() throws InterruptedException {
        //given
        Creator creator = creatorJpaRepository.save(
            CreatorEntity.from(Creator.create(1L, CreatorProfile.create(Subject.Mathematics, "tt")))
        ).toModel();

        creatorStatisticsJpaRepository.save(
            CreatorStatisticsEntity.from(new CreatorStatistics(creator.getId(), 0, 0, 100, 100, 1.0))
        ).toModel();

        Question question = questionJpaRepository.save(
            QuestionEntity.from(Question.create(creator.getId(), QuestionContent.builder().build()))
        ).toModel();

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
        CreatorStatistics creatorStatistics = creatorStatisticsJpaRepository.findByCreatorId(creator.getId()).get().toModel();
        assertThat(creatorStatistics.getTotalReviewRate()).isEqualTo(140);
        assertThat(creatorStatistics.getReviewCount()).isEqualTo(100);
        assertThat(creatorStatistics.getAverageRateOfReview()).isEqualTo(1.4);
    }
}