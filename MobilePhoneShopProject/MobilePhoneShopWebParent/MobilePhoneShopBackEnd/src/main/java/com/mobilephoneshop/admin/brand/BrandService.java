package com.mobilephoneshop.admin.brand;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.mobilephoneshop.common.entity.Brand;

@Service
public class BrandService
{
	public static final int BRANDS_PER_PAGE = 10;

	@Autowired
	private BrandRepository repository;

	public List<Brand> listAll()
	{
		return (List<Brand>) repository.findAll();
	}

	public Page<Brand> listByPage(int pageNum, String sortField, String sortDir, String keyword)
	{
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, BRANDS_PER_PAGE, sort);

		if (keyword != null)
			return repository.findAll(keyword, pageable);
		return repository.findAll(pageable);
	}

	public Brand save(Brand brand)
	{
		return repository.save(brand);
	}

	public Brand get(Integer id) throws BrandNotFoundException
	{
		try
		{
			return repository.findById(id).get();
		} catch (NoSuchElementException ex)
		{
			throw new BrandNotFoundException("Không tìm thấy hãng có id = " + id);
		}
	}

	public void delete(Integer id) throws BrandNotFoundException
	{
		Long countById = repository.countById(id);

		if (countById == null || countById == 0)
			throw new BrandNotFoundException("Không tìm thấy hãng có id = " + id);
		repository.deleteById(id);
	}

	public String checkUnique(Integer id, String name)
	{
		boolean isCreatingNew = (id == null || id == 0);
		Brand brandByName = repository.findByName(name);
		if (isCreatingNew)
		{
			if (brandByName != null)
				return "Duplicate";
		} else
		{
			if (brandByName != null && brandByName.getId() != id)
			{
				return "Duplicate";
			}
		}
		return "OK";
	}
}
