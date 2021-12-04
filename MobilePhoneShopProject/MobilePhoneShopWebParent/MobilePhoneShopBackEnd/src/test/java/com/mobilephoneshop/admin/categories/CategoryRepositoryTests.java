package com.mobilephoneshop.admin.categories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.mobilephoneshop.admin.category.CategoryRepository;
import com.mobilephoneshop.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTests
{
	@Autowired
	private CategoryRepository repository;

	@Test
	public void testCreateRootCategory()
	{
		Category category = new Category("Phụ kiện");
		Category savedCategory = repository.save(category);
		assertThat(savedCategory.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateSubCategory()
	{
		Category parent = new Category(2);
		Category subCategory = new Category("Phụ kiện di động", parent);
		Category savedCategory = repository.save(subCategory);
		assertThat(savedCategory.getId()).isGreaterThan(0);
	}

	@Test
	public void testGetCategory()
	{
		Category category = repository.findById(2).get();
		System.out.println(category.getName());
		Set<Category> children = category.getChildren();
		for (Category subCategory : children)
		{
			System.out.println(subCategory.getName());
		}
		assertThat(children.size()).isGreaterThan(0);
	}

	@Test
	public void testPrintHierarchicalCategories()
	{
		Iterable<Category> categories = repository.findAll();
		for (Category category : categories)
			if (category.getParent() == null)
			{
				System.out.println(category.getName());
				Set<Category> children = category.getChildren();
				for (Category subCategory : children)
				{
					System.out.println("--" + subCategory.getName());
					printChildren(subCategory, 1);
				}
			}
	}

	private void printChildren (Category parent, int subLevel)
	{
		int newSubLevel = subLevel + 1;
		Set<Category> children = parent.getChildren();
		for (Category subCategory : children)
		{
			for(int i=0;i<newSubLevel;i++)
				System.out.println("--");
			System.out.println(subCategory.getName());
		}
	}
	
	@Test
	public void testListRootCategory()
	{
		List<Category> rootCategories = repository.findRootCategories();
		rootCategories.forEach(category -> {System.out.println(category.getName());});
	}
}
