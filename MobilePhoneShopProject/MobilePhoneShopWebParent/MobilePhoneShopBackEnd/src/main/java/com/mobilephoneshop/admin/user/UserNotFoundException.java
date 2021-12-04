package com.mobilephoneshop.admin.user;

public class UserNotFoundException extends Exception
{
	private static final long serialVersionUID = 6475235779214482451L;

	public UserNotFoundException(String message)
	{
		super(message);
	}
}
