package com.mobilephoneshop.admin.category.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.mobilephoneshop.admin.category.CategoryNotFoundException;
import com.mobilephoneshop.admin.category.CategoryPageInfo;
import com.mobilephoneshop.admin.category.CategoryService;
import com.mobilephoneshop.admin.category.export.CategoryCsvExporter;
import com.mobilephoneshop.admin.category.export.CategoryExcelExporter;
import com.mobilephoneshop.common.entity.Category;

@Controller
public class CategoryController
{
	@Autowired
	private CategoryService categoryService;

	@GetMapping("/categories")
	public String listFirstPage(@Param("sortDir") String sortDir, Model model)
	{
		return listByPage(1, sortDir, null, model);
	}

	@GetMapping("/categories/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, @Param("sortDirection") String sortDirection,
			@Param("keyword") String keyword, Model model)
	{
		if (sortDirection == null || sortDirection.isEmpty())
			sortDirection = "asc";
		CategoryPageInfo pageInfo = new CategoryPageInfo();
		List<Category> listCategories = categoryService.listByPage(pageInfo, pageNum, sortDirection, keyword);
		long startCount = (pageNum - 1) * CategoryService.ROOT_CATEGORIES_PER_PAGE + 1;
		long endCount = startCount + CategoryService.ROOT_CATEGORIES_PER_PAGE - 1;
		if (endCount > pageInfo.getTotalElements())
			endCount = pageInfo.getTotalElements();
		String reverseSortDirection = sortDirection.equals("asc") ? "desc" : "asc";
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalPages", pageInfo.getTotalPages());
		model.addAttribute("totalItems", pageInfo.getTotalElements());
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", "name");
		model.addAttribute("sortDirection", sortDirection);
		model.addAttribute("reverseSortDirection", reverseSortDirection);
		model.addAttribute("keyword", keyword);

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
		if (!multipartFile.isEmpty())
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

	@GetMapping("/categories/{id}/enabled/{status}")
	public String updateCategoryEnabledStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes)
	{
		categoryService.updateCategoryEnabledStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message;
		if (status.equals("enabled"))
			message = "Loại hàng có mã loại hàng = " + id + " được phép hiển thị";
		else
			message = "Loại hàng có mã loại hàng = " + id + " tạm dừng hiển thị";
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/categories";
	}

	@GetMapping("/categories/delete/{id}")
	public String deleteCategory(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes)
	{
		try
		{
			categoryService.delete(id);
			String categoryDir = "../category-images/" + id;
			FileUploadUtil.removeDir(categoryDir);
			redirectAttributes.addFlashAttribute("message", "Xoá thành công loại hàng có mã loại hàng = " + id);
		} catch (CategoryNotFoundException ex)
		{
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/export/csv")
	public void exportToCSV(HttpServletResponse response) throws IOException
	{
		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		CategoryCsvExporter exporter = new CategoryCsvExporter();
		exporter.export(listCategories, response);
	}
	
	@GetMapping("/categories/export/excel")
	public void exportToExcel(HttpServletResponse httpServletResponse) throws IOException
	{
		List<Category> listCategories = categoryService.listAll();
		CategoryExcelExporter exporter = new CategoryExcelExporter();
		exporter.export(listCategories, httpServletResponse);
	}
}
