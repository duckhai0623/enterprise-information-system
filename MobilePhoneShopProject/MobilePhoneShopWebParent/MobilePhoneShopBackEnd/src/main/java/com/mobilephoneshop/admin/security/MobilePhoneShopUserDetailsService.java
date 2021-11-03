package com.mobilephoneshop.admin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.mobilephoneshop.admin.user.UserRepository;
import com.mobilephoneshop.common.entity.User;

public class MobilePhoneShopUserDetailsService implements UserDetailsService
{

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
	{
		User user = userRepository.getUserByEmail(email);
		if (user != null)
		{
			return new MobilePhoneShopUserDetails(user);
		}
		throw new UsernameNotFoundException("Email hoặc mật khẩu không đúng!");
	}

}
