package com.eager.questioncloud.application.api.hub;

import static org.assertj.core.api.Assertions.assertThat;

import com.eager.questioncloud.application.business.review.implement.HubReviewRegister;
import com.eager.questioncloud.core.domain.question.infrastructure.entity.QuestionEntity;
import com.eager.questioncloud.core.domain.question.infrastructure.repository.QuestionJpaRepository;
import com.eager.questioncloud.core.domain.question.model.Question;
import com.eager.questioncloud.core.domain.question.model.QuestionContent;
import com.eager.questioncloud.core.domain.review.infrastructure.repository.QuestionReviewJpaRepository;
import com.eager.questioncloud.core.domain.review.model.QuestionReview;
import com.eager.questioncloud.core.domain.userquestion.infrastructure.entity.UserQuestionEntity;
import com.eager.questioncloud.core.domain.userquestion.infrastructure.repository.UserQuestionJpaRepository;
import com.eager.questioncloud.core.domain.userquestion.model.UserQuestion;
import java.time.LocalDateTime;
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
class HubReviewRegisterTest {
    @Autowired
    private HubReviewRegister hubReviewRegister;

    @Autowired
    private QuestionJpaRepository questionJpaRepository;

    @Autowired
    private QuestionReviewJpaRepository questionReviewJpaRepository;

    @Autowired
    private UserQuestionJpaRepository userQuestionJpaRepository;

    @AfterEach
    void tearDown() {
        questionJpaRepository.deleteAllInBatch();
        questionReviewJpaRepository.deleteAllInBatch();
        userQuestionJpaRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("리뷰 중복 작성 동시성 테스트")
    void concurrencyTestWhenRegisterReview() throws InterruptedException {
        //given
        Long reviewerId = 1L;
        Question question = questionJpaRepository.save(QuestionEntity.from(Question.create(1L, QuestionContent.builder().build()))).toModel();
        QuestionReview questionReview = QuestionReview.create(question.getId(), reviewerId, "comment", 5);
        userQuestionJpaRepository.save(UserQuestionEntity.from(new UserQuestion(null, reviewerId, question.getId(), false, LocalDateTime.now())));

        //when
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    hubReviewRegister.register(questionReview);
                } catch (Exception ignored) {
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        //then
        int reviewCount = (int) questionReviewJpaRepository.count();
        assertThat(reviewCount).isEqualTo(1);
    }
}