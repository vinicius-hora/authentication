package com.rasmoo.client.financescontroll;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class FinancescontrollApplicationTests {

	@Autowired
	private PasswordEncoder pass;

	@Test
	void contextLoads() {
		System.out.println(pass.encode("1234"));
		System.out.println(pass.matches("1234", "$2a$10$ltScZCv9O6PvD4TJCl0TIOWA.B73UNYrzHc86yU0OCCV8ld1Vgmx."));
	}

}
