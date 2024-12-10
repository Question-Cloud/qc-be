package com.eager.questioncloud.core.domain.review;

import static org.assertj.core.api.Assertions.assertThat;

import com.eager.questioncloud.core.domain.question.Question;
import com.eager.questioncloud.core.domain.question.QuestionContent;
import com.eager.questioncloud.core.domain.question.QuestionEntity;
import com.eager.questioncloud.core.domain.question.QuestionJpaRepository;
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
class QuestionReviewStatisticsUpdaterTest {
    @Autowired
    private QuestionReviewStatisticsUpdater questionReviewStatisticsUpdater;

    @Autowired
    private QuestionJpaRepository questionJpaRepository;

    @Autowired
    private QuestionReviewStatisticsJpaRepository questionReviewStatisticsJpaRepository;

    @AfterEach
    void tearDown() {
        questionJpaRepository.deleteAllInBatch();
        questionReviewStatisticsJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("리뷰 작성 시 평점 통계 업데이트 동시성 테스트")
    void concurrencyTestWhenRegisteredReview() throws InterruptedException {
        //given
        Question question = questionJpaRepository.save(QuestionEntity.from(Question.create(1L, QuestionContent.builder().build()))).toModel();
        questionReviewStatisticsJpaRepository.save(QuestionReviewStatisticsEntity.from(QuestionReviewStatistics.create(question.getId())));
        RegisteredReviewEvent event = RegisteredReviewEvent.create(question.getId(), 4);

        //when
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    questionReviewStatisticsUpdater.updateByRegisteredReview(event);
                } catch (Exception ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        //then
        QuestionReviewStatistics questionReviewStatistics = questionReviewStatisticsJpaRepository.findById(question.getId()).get().toModel();
        assertThat(questionReviewStatistics.getReviewCount()).isEqualTo(100);
        assertThat(questionReviewStatistics.getTotalRate()).isEqualTo(400);
    }

    @Test
    @DisplayName("리뷰 수정 시 평점 통계 업데이트 동시성 테스트")
    void concurrencyTestWhenModifiedReview() throws InterruptedException {
        //given
        Question question = questionJpaRepository.save(QuestionEntity.from(Question.create(1L, QuestionContent.builder().build()))).toModel();
        questionReviewStatisticsJpaRepository.save(
            QuestionReviewStatisticsEntity.from(new QuestionReviewStatistics(question.getId(), 100, 0, 0.0))
        );
        ModifiedReviewEvent event = ModifiedReviewEvent.create(question.getId(), 3);

        //when
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    questionReviewStatisticsUpdater.updateByModifiedReview(event);
                } catch (Exception ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        //then
        QuestionReviewStatistics questionReviewStatistics = questionReviewStatisticsJpaRepository.findById(question.getId()).get().toModel();
        assertThat(questionReviewStatistics.getTotalRate()).isEqualTo(300);
        assertThat(questionReviewStatistics.getReviewCount()).isEqualTo(100);
        assertThat(questionReviewStatistics.getAverageRate()).isEqualTo(3.0);
    }
}