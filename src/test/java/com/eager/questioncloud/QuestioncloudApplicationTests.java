package com.eager.questioncloud;

import com.eager.questioncloud.portone.implement.PortoneAPI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QuestioncloudApplicationTests {

    @Autowired
    private PortoneAPI portoneAPI;

    @Test
    void contextLoads() {
        portoneAPI.cancel("testm0c4na60");
    }

}
