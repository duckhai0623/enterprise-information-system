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

	@Test
	public void testUpdateEnabled()
	{
		User userPhamDucKhai = userRepository.findById(1).get();
		userPhamDucKhai.setEnabled(true);
		userRepository.save(userPhamDucKhai);
	}

	@Test
	public void testCreateNewUserWithTwoRoles()
	{
		User test = new User("test@gmail.com", "123456", "test", "test");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		test.addRole(roleEditor);
		test.addRole(roleAssistant);
		User savedUser = userRepository.save(test);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	public void testListAllUsers()
	{
		Iterable<User> listUsers = userRepository.findAll();
		listUsers.forEach(user -> System.out.println(user));
	}

	@Test
	public void testGetUserById()
	{
		User userPhamDucKhai = userRepository.findById(1).get();
		System.out.println(userPhamDucKhai);
		assertThat(userPhamDucKhai).isNotNull();
	}

	@Test
	public void testUpdateUserDetails()
	{
		User userPhamDucKhai = userRepository.findById(1).get();
		userPhamDucKhai.setEnabled(true);
		userPhamDucKhai.setEmail("duckhai@gmail.com");
		userRepository.save(userPhamDucKhai);
	}

	@Test
	public void testUpdateUserRoles()
	{
		User userTest = userRepository.findById(2).get();
		Role roleAssistant = new Role(5);
		Role roleShipping = new Role(4);
		userTest.getRoles().remove(roleAssistant);
		userTest.addRole(roleShipping);
		userRepository.save(userTest);
	}

	@Test
	public void testDeleteUserById()
	{
		Integer id = 8;
		userRepository.deleteById(id);
	}
	
	@Test
	public void testGetUserByEmail()
	{
		String email = "duckhai0623@gmail.com";
		User user = userRepository.getUserByEmail(email);
		assertThat(user).isNotNull();		
	}
}
