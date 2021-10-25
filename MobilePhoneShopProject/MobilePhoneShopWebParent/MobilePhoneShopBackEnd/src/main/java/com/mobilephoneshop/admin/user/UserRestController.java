package com.mobilephoneshop.admin.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController
{
	@Autowired
	private UserService userService;
	
	@PostMapping("/users/check_email")
	public String checkDuplicatedEmail(@Param("email") String email)
	{
		return userService.isEmailUnique(email) ? "OK" : "Duplicated";
	}
}
