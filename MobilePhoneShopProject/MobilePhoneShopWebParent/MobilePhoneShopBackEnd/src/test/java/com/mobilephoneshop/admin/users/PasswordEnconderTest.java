package com.mobilephoneshop.admin.users;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEnconderTest
{
	@Test
	public void testEncodePassword()
	{
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String rawPassword = "123456";
		String encodePassword = bCryptPasswordEncoder.encode(rawPassword);
		
		System.out.println(encodePassword);
		boolean matches = bCryptPasswordEncoder.matches(rawPassword, encodePassword);
		assertThat(matches).isTrue();
	}
}
