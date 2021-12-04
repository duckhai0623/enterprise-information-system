package com.mobilephoneshop.admin.category;

public class CategoryNotFoundException extends Exception
{
	private static final long serialVersionUID = 6081310899352583463L;
	
	public CategoryNotFoundException(String message)
	{
		super(message);
	}

}
