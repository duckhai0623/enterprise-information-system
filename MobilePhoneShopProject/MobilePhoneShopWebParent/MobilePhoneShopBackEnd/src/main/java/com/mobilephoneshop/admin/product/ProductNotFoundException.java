package com.mobilephoneshop.admin.product;

public class ProductNotFoundException extends Exception
{
	private static final long serialVersionUID = -8429184972502675354L;
	
	public ProductNotFoundException(String message)
	{
		super(message);
	}
}
