package com.mobilephoneshop.admin.product.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mobilephoneshop.admin.FileUploadUtil;
import com.mobilephoneshop.admin.brand.BrandService;
import com.mobilephoneshop.admin.category.CategoryService;
import com.mobilephoneshop.admin.product.ProductNotFoundException;
import com.mobilephoneshop.admin.product.ProductSaveHelper;
import com.mobilephoneshop.admin.product.ProductService;
import com.mobilephoneshop.admin.security.MobilePhoneShopUserDetails;
import com.mobilephoneshop.common.entity.Brand;
import com.mobilephoneshop.common.entity.Category;
import com.mobilephoneshop.common.entity.Product;

@Controller
public class ProductController
{
	@Autowired
	private ProductService productService;

	@Autowired
	private BrandService brandService;

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/products")
	public String listFirstPage(Model model)
	{
		return listByPage(1, model, "name", "asc", null, 0);
	}

	@GetMapping("/products/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDirection") String sortDirection,
			@Param("keyword") String keyword, @Param("categoryId") Integer categoryId)
	{
		Page<Product> page = productService.listByPage(pageNum, sortField, sortDirection, keyword, categoryId);
		List<Product> listProducts = page.getContent();

		List<Category> listCategories = categoryService.listCategoriesUsedInForm();
		
		long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
 		long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
		if (endCount > page.getTotalElements())
			endCount = page.getTotalElements();

		String reverseSortDirection = sortDirection.equals("asc") ? "desc" : "asc";
			
		if(categoryId != null )
			model.addAttribute("categoryId", categoryId);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDirection", sortDirection);
		model.addAttribute("reverseSortDirection", reverseSortDirection);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("listProducts", listProducts);
		model.addAttribute("listCategories", listCategories);
		

		return "products/products";
	}

	@GetMapping("/products/new")
	public String newProduct(Model model)
	{
		List<Brand> listBrands = brandService.listAll();

		Product product = new Product();

		product.setEnabled(true);
		product.setInStock(true);

		model.addAttribute("product", product);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("pageTitle", "Thêm sản phẩm");
		model.addAttribute("numberOfExistingExtraImages", 0);

		return "products/product_form";
	}

	@PostMapping("/products/save")
	public String saveProduct(Product product, RedirectAttributes redirectAttributes,
			@RequestParam(value = "fileImage", required = false) MultipartFile mainImageMultipart,
			@RequestParam(value = "extraImage", required = false) MultipartFile[] extraImageMultipart,
			@RequestParam(name = "detailIds", required = false) String[] detailIds,
			@RequestParam(name = "detailNames", required = false) String[] detailNames,
			@RequestParam(name = "detailValues", required = false) String[] detailValues,
			@RequestParam(name = "imageIds", required = false) String[] imageIds,
			@RequestParam(name = "imageNames", required = false) String[] imageNames,
			@AuthenticationPrincipal MobilePhoneShopUserDetails loggedUser
			) throws IOException
	{
		if(loggedUser.hasRole("Nhân viên bán hàng"))
		{
			productService.saveProductPrice(product);
			redirectAttributes.addFlashAttribute("message", "Lưu sản phẩm thành công");
			return "redirect:/products";
		}
		
		ProductSaveHelper.setMainImageName(mainImageMultipart, product);
		ProductSaveHelper.setExistingExtraImageNames(imageIds, imageNames, product);
		ProductSaveHelper.setNewExtraImageNames(extraImageMultipart, product);
		ProductSaveHelper.setProductDetails(detailIds, detailNames, detailValues, product);

		Product savedProduct = productService.save(product);

		ProductSaveHelper.saveUploadedImages(mainImageMultipart, extraImageMultipart, savedProduct);

		ProductSaveHelper.deleteExtraImagesWereRemoveOnForm(product);

		redirectAttributes.addFlashAttribute("message", "Lưu sản phẩm thành công");
		return "redirect:/products";
	}

	@GetMapping("/products/{id}/enabled/{status}")
	public String updateUserEnableStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean enabled,
			RedirectAttributes redirectAttributes)
	{
		productService.updateProductEnableStatus(id, enabled);
		String status = enabled ? "được phép bán" : "tạm dừng bán";
		String message = "Sản phẩm có mã sản phẩm = " + id + " " + status;
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/products";
	}

	@GetMapping("/products/delete/{id}")
	public String deleteProduct(@PathVariable(name = "id") Integer id, Model model,
			RedirectAttributes redirectAttributes)
	{
		try
		{
			productService.delete(id);
			String productExtraImagesDir = "../product-images/" + id + "/extras";
			String productImagesDir = "../product-images/" + id;

			FileUploadUtil.removeDir(productExtraImagesDir);
			FileUploadUtil.removeDir(productImagesDir);

			redirectAttributes.addFlashAttribute("message", "Xoá thành công sản phẩm có mã sản phẩm = " + id);
		} catch (ProductNotFoundException ex)
		{
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		return "redirect:/products";
	}

	@GetMapping("/products/edit/{id}")
	public String editProduct(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes)
	{
		try
		{
			Product product = productService.get(id);
			List<Brand> listBrands = brandService.listAll();
			Integer numberOfExistingExtraImages = product.getImages().size();

			model.addAttribute("product", product);
			model.addAttribute("listBrands", listBrands);
			model.addAttribute("pageTitle", "Chỉnh sửa sản phẩm");
			model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);

			return "products/product_form";
		} catch (ProductNotFoundException e)
		{
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/products";
		}
	}

	@GetMapping("/products/detail/{id}")
	public String viewProductDetails(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes)
	{
		try
		{
			Product product = productService.get(id);

			model.addAttribute("product", product);

			return "products/product_detail_modal";
		} catch (ProductNotFoundException e)
		{
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/products";
		}
	}
}
