package com.eager.questioncloud.core.domain.review;

import static org.assertj.core.api.Assertions.assertThat;

import com.eager.questioncloud.core.domain.question.QuestionBuilder;
import com.eager.questioncloud.core.domain.question.infrastructure.QuestionRepository;
import com.eager.questioncloud.core.domain.question.model.Question;
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
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionReviewStatisticsRepository questionReviewStatisticsRepository;

    @AfterEach
    void tearDown() {
        questionRepository.deleteAllInBatch();
        questionReviewStatisticsRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("리뷰 작성 이벤트 평점 통계 업데이트 동시성 테스트")
    void reviewStatisticsConcurrencyTestWhenRegisteredReviewEvent() throws InterruptedException {
        //given
        Question question = questionRepository.save(QuestionBuilder.builder().build().toQuestion());
        questionReviewStatisticsRepository.save(
            QuestionReviewStatisticsBuilder
                .builder()
                .questionId(question.getId())
                .build()
                .toQuestionReviewStatistics()
        );

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
        QuestionReviewStatistics questionReviewStatistics = questionReviewStatisticsRepository.get(question.getId());
        assertThat(questionReviewStatistics.getReviewCount()).isEqualTo(100);
        assertThat(questionReviewStatistics.getTotalRate()).isEqualTo(400);
    }

    @Test
    @DisplayName("리뷰 수정 이벤트 평점 통계 업데이트 동시성 테스트")
    void reviewStatisticsConcurrencyTestWhenModifiedReviewEvent() throws InterruptedException {
        //given
        Question question = questionRepository.save(QuestionBuilder.builder().build().toQuestion());
        questionReviewStatisticsRepository.save(
            QuestionReviewStatisticsBuilder
                .builder()
                .questionId(question.getId())
                .reviewCount(100)
                .build()
                .toQuestionReviewStatistics()
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
        QuestionReviewStatistics questionReviewStatistics = questionReviewStatisticsRepository.get(question.getId());
        assertThat(questionReviewStatistics.getTotalRate()).isEqualTo(300);
        assertThat(questionReviewStatistics.getReviewCount()).isEqualTo(100);
        assertThat(questionReviewStatistics.getAverageRate()).isEqualTo(3.0);
    }

    @Test
    @DisplayName("리뷰 삭제 이벤트 평점 통계 업데이트 동시성 테스트")
    void reviewStatisticsConcurrencyTestWhenDeletedReviewEvent() throws InterruptedException {
        //given
        Question question = questionRepository.save(QuestionBuilder.builder().build().toQuestion());
        questionReviewStatisticsRepository.save(
            QuestionReviewStatisticsBuilder
                .builder()
                .questionId(question.getId())
                .reviewCount(100)
                .totalRate(400)
                .averageRate(4.0)
                .build()
                .toQuestionReviewStatistics()
        );
        DeletedReviewEvent event = DeletedReviewEvent.create(question.getId(), 4);

        //when
        int threadCount = 80;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    questionReviewStatisticsUpdater.updateByDeletedReview(event);
                } catch (Exception ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        //then
        QuestionReviewStatistics questionReviewStatistics = questionReviewStatisticsRepository.get(question.getId());
        assertThat(questionReviewStatistics.getTotalRate()).isEqualTo(80);
        assertThat(questionReviewStatistics.getReviewCount()).isEqualTo(20);
        assertThat(questionReviewStatistics.getAverageRate()).isEqualTo(4.0);
    }

    @Test
    @DisplayName("리뷰 추가, 수정, 삭제 이벤트가 동시에 일어나는 경우 리뷰 통계 평점 업데이트 동시성 테스트")
    void reviewStatisticsConcurrencyTestWhenMultipleEvent() throws InterruptedException {
        //given
        Question question = questionRepository.save(QuestionBuilder.builder().build().toQuestion());
        questionReviewStatisticsRepository.save(
            QuestionReviewStatisticsBuilder
                .builder()
                .questionId(question.getId())
                .totalRate(100)
                .reviewCount(100)
                .averageRate(1.0)
                .build()
                .toQuestionReviewStatistics()
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
                    questionReviewStatisticsUpdater.updateByRegisteredReview(registeredReviewEvent);
                } catch (Exception ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }

        for (int i = 0; i < 40; i++) {
            executorService.execute(() -> {
                try {
                    questionReviewStatisticsUpdater.updateByModifiedReview(modifiedReviewEvent);
                } catch (Exception ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }

        for (int i = 0; i < 40; i++) {
            executorService.execute(() -> {
                try {
                    questionReviewStatisticsUpdater.updateByDeletedReview(deletedReviewEvent);
                } catch (Exception ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        //then
        QuestionReviewStatistics questionReviewStatistics = questionReviewStatisticsRepository.get(question.getId());
        assertThat(questionReviewStatistics.getTotalRate()).isEqualTo(140);
        assertThat(questionReviewStatistics.getReviewCount()).isEqualTo(100);
        assertThat(questionReviewStatistics.getAverageRate()).isEqualTo(1.4);
    }
}