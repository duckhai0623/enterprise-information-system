package com.mobilephoneshop.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(
{ "com.mobilephoneshop.common.entity", "com.mobilephoneshop.admin.user" })
public class MobilePhoneShopBackEndApplication
{

	public static void main(String[] args)
	{
		SpringApplication.run(MobilePhoneShopBackEndApplication.class, args);
	}

}
