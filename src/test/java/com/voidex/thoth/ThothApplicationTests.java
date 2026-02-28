package com.voidex.thoth;

import com.voidex.thoth.application.ThothApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = ThothApplication.class)
class ThothApplicationTests {

	@Test
	void contextLoads() {
	}

}
