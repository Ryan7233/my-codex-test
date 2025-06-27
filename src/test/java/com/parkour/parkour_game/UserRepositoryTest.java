package com.parkour.parkour_game.repository;

import com.parkour.parkour_game.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {
	@Autowired
	private UserRepository userRepository;

	@Test
	@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
	public void testSaveAndFindUser() {
		User user = new User();
		user.setUsername("player1");
		user.setPassword("password123");
		user.setEmail("player1@example.com");

		userRepository.save(user);
		User foundUser = userRepository.findByUsername("player1").orElse(null);

		assertThat(foundUser).isNotNull();
		assertThat(foundUser.getUsername()).isEqualTo("player1");
	}
}