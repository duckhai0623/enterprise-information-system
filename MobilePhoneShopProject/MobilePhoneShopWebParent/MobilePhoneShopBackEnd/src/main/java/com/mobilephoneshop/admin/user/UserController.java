package com.mobilephoneshop.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mobilephoneshop.common.entity.Role;
import com.mobilephoneshop.common.entity.User;

@Controller
public class UserController
{
	@Autowired
	private UserService userService;

	@GetMapping("/users")
	public String listAll(Model model)
	{
		List<User> listUsers = userService.listAll();
		model.addAttribute("listUsers", listUsers);
		return "users";
	}

	@GetMapping("/users/new")
	public String newUser(Model model)
	{
		List<Role> listRoles = userService.listRoles();
		User user = new User();
		user.setEnabled(true);
		model.addAttribute("user", user);
		model.addAttribute("listRoles", listRoles);
		model.addAttribute("pageTitle", "Thêm nhân viên");

		return "user_form";
	}

	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes)
	{
		userService.save(user);
		redirectAttributes.addFlashAttribute("message", "Lưu nhân viên thành công!");
		return "redirect:/users";
	}

	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes)
	{
		try
		{
			User user = userService.get(id);
			List<Role> listRoles = userService.listRoles();
			model.addAttribute("user", user);
			model.addAttribute("listRoles", listRoles);
			model.addAttribute("pageTitle", "Chỉnh sửa thông tin");
			return "user_form";
		} catch (UserNotFoundException e)
		{
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/users";
		}
	}

	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes)
	{
		try
		{
			userService.delete(id);
			redirectAttributes.addFlashAttribute("message", "Xoá nhân viên thành công!");
		} catch (UserNotFoundException e)
		{
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/users";
	}
}
