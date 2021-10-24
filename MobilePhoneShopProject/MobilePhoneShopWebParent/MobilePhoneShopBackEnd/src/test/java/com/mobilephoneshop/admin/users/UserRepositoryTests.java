package com.mobilephoneshop.admin.users;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.mobilephoneshop.admin.user.UserRepository;
import com.mobilephoneshop.common.entity.Role;
import com.mobilephoneshop.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests
{
	@Autowired
	UserRepository userRepository;

	@Autowired
	private TestEntityManager testEntityManager;

	@Test
	public void testCreateUser()
	{
		Role roleAdmin = testEntityManager.find(Role.class, 1);
		User userPhamDucKhai = new User("duckhai0623@gmail.com", "123456", "Đức Khải", "Phạm");
		userPhamDucKhai.addRole(roleAdmin);
		User savedUser = userRepository.save(userPhamDucKhai);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
}
