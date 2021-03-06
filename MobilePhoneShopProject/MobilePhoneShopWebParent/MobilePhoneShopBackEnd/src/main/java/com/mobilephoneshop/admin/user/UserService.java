package com.mobilephoneshop.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mobilephoneshop.common.entity.Role;
import com.mobilephoneshop.common.entity.User;

@Service
@Transactional
public class UserService
{
	public static final int USERS_PER_PAGE = 4;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public User getByEmail(String email)
	{
		return userRepository.getUserByEmail(email);
	}

	public List<User> listAll()
	{
		return (List<User>) userRepository.findAll(Sort.by("id").ascending());
	}

	public List<Role> listRoles()
	{
		return (List<Role>) roleRepository.findAll();
	}

	public Page<User> listByPage(int pageNumber, String sortField, String sortDirection, String keyword)
	{
		Sort sort = Sort.by(sortField);
		sort = sortDirection.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNumber - 1, USERS_PER_PAGE, sort);
		if (keyword != null)
			return userRepository.findAll(keyword, pageable);
		return userRepository.findAll(pageable);
	}

	public User save(User user)
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
		return userRepository.save(user);
	}

	public User updateAccount(User userInForm)
	{
		User userInDB = userRepository.findById(userInForm.getId()).get();
		if (!userInForm.getPassword().isEmpty())
		{
			userInDB.setPassword(userInForm.getPassword());
			encodePassword(userInDB);
		}
		if(userInForm.getPhotos() != null)
		{
			userInDB.setPhotos(userInForm.getPhotos());
		}
		userInDB.setLastName(userInForm.getLastName());
		userInDB.setFirstName(userInForm.getFirstName());
		return userRepository.save(userInDB);
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
			throw new UserNotFoundException("Kh??ng t??m th???y nh??n vi??n c?? m?? nh??n vi??n = " + id);
		}
	}

	public void delete(Integer id) throws UserNotFoundException
	{
		Long countById = userRepository.countById(id);
		if (countById == null || countById == 0)
		{
			throw new UserNotFoundException("Kh??ng t??m th???y nh??n vi??n c?? id = " + id);
		}
		userRepository.deleteById(id);
	}

	public void updateUserEnableStatus(Integer id, boolean enabled)
	{
		userRepository.updateEnableStatus(id, enabled);
	}

}
