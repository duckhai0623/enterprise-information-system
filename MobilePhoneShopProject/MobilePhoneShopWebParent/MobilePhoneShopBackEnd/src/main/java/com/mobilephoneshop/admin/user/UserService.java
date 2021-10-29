package com.mobilephoneshop.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mobilephoneshop.common.entity.Role;
import com.mobilephoneshop.common.entity.User;

@Service
public class UserService
{
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<User> listAll()
	{
		return (List<User>) userRepository.findAll();
	}

	public List<Role> listRoles()
	{
		return (List<Role>) roleRepository.findAll();
	}

	public void save(User user)
	{
		boolean isUpdatingUser = (user.getId() != null);
		if (isUpdatingUser)
		{
			User existingUser = userRepository.findById(user.getId()).get();
			if (user.getPassword().isEmpty())
			{
				user.setPassword(existingUser.getPassword());
			} else
			{
				encodePassword(user);
			}
		} else
		{
			encodePassword(user);
		}
		userRepository.save(user);
	}

	private void encodePassword(User user)
	{
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}

	public boolean isEmailUnique(Integer id, String email)
	{
		User userByEmail = userRepository.getUserByEmail(email);
		if (userByEmail == null)
			return true;
		boolean isCreatingNew = (id == null);
		if (isCreatingNew)
		{
			if (userByEmail != null)
				return false;
		} else
		{
			if (userByEmail.getId() != id)
				return false;
		}
		return true;
	}

	public User get(Integer id) throws UserNotFoundException
	{
		try
		{
			return userRepository.findById(id).get();
		} catch (NoSuchElementException e)
		{
			throw new UserNotFoundException("Không tìm thấy nhân viên có mã nhân viên = " + id);
		}
	}

	public void delete(Integer id) throws UserNotFoundException
	{
		Long countById = userRepository.countById(id);
		if (countById == null || countById == 0)
		{
			throw new UserNotFoundException("Không tìm thấy nhân viên có id = " + id);
		}
		userRepository.deleteById(id);
	}
}
