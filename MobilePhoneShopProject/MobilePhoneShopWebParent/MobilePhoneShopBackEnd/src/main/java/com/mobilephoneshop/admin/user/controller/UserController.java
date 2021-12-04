package com.mobilephoneshop.admin.user.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mobilephoneshop.admin.FileUploadUtil;
import com.mobilephoneshop.admin.user.UserNotFoundException;
import com.mobilephoneshop.admin.user.UserService;
import com.mobilephoneshop.admin.user.export.UserCsvExporter;
import com.mobilephoneshop.admin.user.export.UserExcelExporter;
import com.mobilephoneshop.admin.user.export.UserPdfExporter;
import com.mobilephoneshop.common.entity.Role;
import com.mobilephoneshop.common.entity.User;

@Controller
public class UserController
{
	@Autowired
	private UserService userService;

	@GetMapping("/users")
	public String listFirstPage(Model model)
	{
		return listByPage(1, model, "id", "asc", null);
	}

	@GetMapping("/users/page/{pageNumber}")
	public String listByPage(@PathVariable(name = "pageNumber") int pageNumber, Model model,
			@Param("sortField") String sortField, @Param("sortDirection") String sortDirection,
			@Param("keyword") String keyword)
	{
		Page<User> page = userService.listByPage(pageNumber, sortField, sortDirection, keyword);
		List<User> listUsers = page.getContent();
		long startCount = (pageNumber - 1) * UserService.USERS_PER_PAGE + 1;
		long endCount = startCount + UserService.USERS_PER_PAGE - 1;
		if (endCount > page.getTotalElements())
			endCount = page.getTotalElements();
		String reverseSortDirection = sortDirection.equals("asc") ? "decs" : "asc";
		model.addAttribute("currentPage", pageNumber);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("listUsers", listUsers);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDirection", sortDirection);
		model.addAttribute("reverseSortDirection", reverseSortDirection);
		model.addAttribute("keyword", keyword);
		return "users/users";
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

		return "users/user_form";
	}

	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile) throws IOException
	{
		if (!multipartFile.isEmpty())
		{
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser = userService.save(user);
			String uploadDirectory = "user-photos/" + savedUser.getId();
			FileUploadUtil.cleanDirectory(uploadDirectory);
			FileUploadUtil.saveFile(uploadDirectory, fileName, multipartFile);
		} else
		{
			if (user.getPhotos().isEmpty())
				user.setPhotos(null);
			userService.save(user);
		}
		redirectAttributes.addFlashAttribute("message", "Lưu nhân viên thành công!");
		return getRedirectURLtoAffectedUser(user);
	}

	private String getRedirectURLtoAffectedUser(User user)
	{
		String firstPartOfEmail = user.getEmail().split("@")[0];
		return "redirect:/users/page/1?sortField=id&sortDirection=asc&keyword=" + firstPartOfEmail;
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
			return "users/user_form";
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

	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnableStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes)
	{
		userService.updateUserEnableStatus(id, enabled);
		String status = enabled ? "được phép hoạt động" : "tạm dừng hoạt động";
		String message = "Nhân viên có mã nhân viên = " + id + " " + status;
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/users";
	}
	
	@GetMapping("/users/export/csv")
	public void exportToCSV(HttpServletResponse httpServletResponse) throws IOException
	{
		List<User> listUsers = userService.listAll();
		UserCsvExporter userCsvExporter = new UserCsvExporter();
		userCsvExporter.export(listUsers, httpServletResponse);
	}
	
	@GetMapping("/users/export/excel")
	public void exportToExcel(HttpServletResponse httpServletResponse) throws IOException
	{
		List<User> listUsers = userService.listAll();
		UserExcelExporter userCsvExporter = new UserExcelExporter();
		userCsvExporter.export(listUsers, httpServletResponse);
	}
	
	@GetMapping("/users/export/pdf")
	public void exportToPdf(HttpServletResponse httpServletResponse) throws IOException
	{
		List<User> listUsers = userService.listAll();
		UserPdfExporter userPdfExporter = new UserPdfExporter();
		userPdfExporter.export(listUsers, httpServletResponse);
	}
}
