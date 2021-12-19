package com.mobilephoneshop.admin.product;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mobilephoneshop.common.entity.Product;

@Service
@Transactional
public class ProductService
{
	public static final int PRODUCTS_PER_PAGE = 5;

	@Autowired
	private ProductRepository repository;

	public List<Product> listAll()
	{
		return (List<Product>) repository.findAll();
	}

	public Page<Product> listByPage(int pageNum, String sortField, String sortDirection, String keyword,
			Integer categoryId)
	{
		Sort sort = Sort.by(sortField);
		sort = sortDirection.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE, sort);

		if (keyword != null && !keyword.isEmpty())
		{
			if(categoryId != null && categoryId > 0)
			{
				String categoryIdMatch = "-" + String.valueOf(categoryId) + "-"; 
				return repository.searchInCategory(categoryId, categoryIdMatch, keyword, pageable); 
			}
			
			return repository.findAll(keyword, pageable);
		}
		
		if(categoryId != null && categoryId > 0)
		{
			String categoryIdMatch = "-" + String.valueOf(categoryId) + "-";
			return repository.findAllInCategory(categoryId, categoryIdMatch, pageable);
		}
		
		return repository.findAll(pageable);
	}

	public Product save(Product product)
	{
		if (product.getId() == null)
			product.setCreatedTime(new Date());

		if (product.getAlias() == null || product.getAlias().isEmpty())
		{
			String defaultAlias = product.getName().replaceAll(" ", "-");
			product.setAlias(defaultAlias);
		} else
			product.setAlias(product.getAlias().replaceAll(" ", "-"));

		product.setUpdatedTime(new Date());
		return repository.save(product);
	}
	
	public void saveProductPrice(Product productInForm)
	{
		Product productInDatabase = repository.findById(productInForm.getId()).get();
		productInDatabase.setCost(productInForm.getCost());
		productInDatabase.setPrice(productInForm.getPrice());
		productInDatabase.setDiscountPercent(productInForm.getDiscountPercent());
		
		repository.save(productInDatabase);
	}

	public String checkUnique(Integer id, String name)
	{
		boolean isCreatingNew = (id == null || id == 0);
		Product productByName = repository.findByName(name);
		if (isCreatingNew)
		{
			if (productByName != null)
				return "Duplicate";
		} else
		{
			if (productByName != null && productByName.getId() != id)
			{
				return "Duplicate";
			}
		}
		return "OK";
	}

	public void updateProductEnableStatus(Integer id, boolean enabled)
	{
		repository.updateEnableStatus(id, enabled);
	}

	public void delete(Integer id) throws ProductNotFoundException
	{
		Long countById = repository.countById(id);
		if (countById == null || countById == 0)
		{
			throw new ProductNotFoundException("Không tìm thấy sản phẩm có id = " + id);
		}
		repository.deleteById(id);
	}

	public Product get(Integer id) throws ProductNotFoundException
	{
		try
		{
			return repository.findById(id).get();
		} catch (NoSuchElementException e)
		{
			throw new ProductNotFoundException("Không thể tìm thấy sản phẩm có id = " + id);
		}
	}
}
