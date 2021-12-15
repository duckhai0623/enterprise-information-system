package com.mobilephoneshop.admin.brands;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.mobilephoneshop.admin.brand.BrandRepository;
import com.mobilephoneshop.common.entity.Brand;
import com.mobilephoneshop.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class BrandRepositoryTests
{
	@Autowired
	private BrandRepository repository;
	
	@Test
	public void testCreateBrand1()
	{
		Category dienThoaiThongMinh = new Category(4);
		Brand samsung = new Brand("Samsung");
		samsung.getCategories().add(dienThoaiThongMinh);
		Brand savedBrand = repository.save(samsung);
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateBrand2()
	{
		Category dienThoaiThongMinh = new Category(4);
		Category phuKienDiDong = new Category(3);
		
		Brand apple = new Brand("Apple");
		apple.getCategories().add(dienThoaiThongMinh);
		apple.getCategories().add(phuKienDiDong);
		
		Brand savedBrand = repository.save(apple);
		
		assertThat(savedBrand).isNotNull();
		assertThat(savedBrand.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testFindAll()
	{
		Iterable<Brand> brands = repository.findAll();
		brands.forEach(System.out::println);
		assertThat(brands).isNotEmpty();
	}
	
	@Test
	public void testGetById()
	{
		Brand brand = repository.findById(1).get();
		assertThat(brand.getName()).isEqualTo("Samsung");
	}
	
	
}
