package com.mobilephoneshop.admin.category.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.mobilephoneshop.admin.category.CategoryNotFoundException;
import com.mobilephoneshop.admin.category.CategoryService;
import com.mobilephoneshop.common.entity.Category;

@Controller
public class CategoryController
{
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/categories")
	public String listAll(Model model)
	{
		List<Category> listCategories = categoryService.listAll();
		model.addAttribute("listCategories", listCategories);
		return "categories/categories";
	}

	@GetMapping("/categories/new")
	public String newCategory(Model model)
	{
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		model.addAttribute("category", new Category());
		model.addAttribute("listCategories", listCategories);
		model.addAttribute("pageTitle", "Thêm loại hàng");
		return "categories/category_form";
	}

	@PostMapping("/categories/save")
	public String saveCategory(Category category, @RequestParam("fileImage") MultipartFile multipartFile,
			RedirectAttributes redirectAttributes) throws IOException
	{
		if(!multipartFile.isEmpty())
		{
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			category.setImage(fileName);
			Category savedCategory = categoryService.save(category);
			String uploadDir = "../category-images/" + savedCategory.getId();
			FileUploadUtil.cleanDirectory(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else 
		{
			categoryService.save(category);
		}
		
		
		redirectAttributes.addFlashAttribute("message", "Lưu loại hàng thành công");
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes)
	{
		try
		{
			Category category = categoryService.get(id);
			List<Category> listCategories = categoryService.listCategoriesUsedInForm();
			model.addAttribute("category", category);
			model.addAttribute("listCategories", listCategories);
			model.addAttribute("pageTitle", "Chỉnh sửa loại hàng (id: " + id + ")");
			return "categories/category_form";
		} catch (CategoryNotFoundException e)
		{
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/categories";
		}
	}
}
