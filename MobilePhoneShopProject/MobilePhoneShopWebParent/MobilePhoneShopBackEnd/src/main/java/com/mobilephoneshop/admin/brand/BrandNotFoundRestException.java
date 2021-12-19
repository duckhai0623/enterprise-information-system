package com.mobilephoneshop.admin.brand;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Không tìm thấy hãng")
public class BrandNotFoundRestException extends Exception
{
	private static final long serialVersionUID = -3490110338873031950L;
	
}
