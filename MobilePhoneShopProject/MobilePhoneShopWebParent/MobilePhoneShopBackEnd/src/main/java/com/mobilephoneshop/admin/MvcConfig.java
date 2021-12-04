package com.mobilephoneshop.admin;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer
{
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry)
	{
		String directoryName = "user-photos";
		Path userPhotoDirectory = Paths.get(directoryName);
		String userPhotosPath = userPhotoDirectory.toFile().getAbsolutePath();
		registry.addResourceHandler("/" + directoryName + "/**").addResourceLocations("file:/" + userPhotosPath + "/");
		
		String categoryImagesDirectoryName = "../category-images";
		Path categoryImagesDirectory = Paths.get(categoryImagesDirectoryName);
		String categoryImagesPath = categoryImagesDirectory.toFile().getAbsolutePath();
		registry.addResourceHandler("/category-images/**").addResourceLocations("file:/" + categoryImagesPath + "/");
	}
}
