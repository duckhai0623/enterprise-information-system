package com.mobilephoneshop.admin.product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.mobilephoneshop.admin.FileUploadUtil;
import com.mobilephoneshop.common.entity.Product;
import com.mobilephoneshop.common.entity.ProductImage;

public class ProductSaveHelper
{
	private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ProductSaveHelper.class);
	
	public static void deleteExtraImagesWereRemoveOnForm(Product product)
	{
		String extraImageDirectory = "/product-images/" + product.getId() + "/extras/";
		Path directoryPath = Paths.get(extraImageDirectory);

		try
		{
			Files.list(directoryPath).forEach(file ->
			{
				String fileName = file.toFile().getName();

				if (!product.containImageName(fileName))
				{
					try
					{
						Files.delete(file);
						LOGGER.info("Xoá ảnh phụ: " + fileName);
					} catch (IOException e)
					{
						LOGGER.error("Không thể xoá ảnh phụ: " + fileName);
					}
				}
			});
		} catch (IOException e)
		{
			LOGGER.error("Không thể liệt kê đường dẫn: " + directoryPath);
		}
	}

	public static void setExistingExtraImageNames(String[] imageIds, String[] imageNames, Product product)
	{
		if (imageIds == null || imageIds.length == 0)
			return;

		Set<ProductImage> images = new HashSet<>();

		for (int count = 0; count < imageIds.length; count++)
		{
			Integer id = Integer.parseInt(imageIds[count]);
			String name = imageNames[count];

			images.add(new ProductImage(id, name, product));
		}
		System.out.println("id = " + imageIds + " and name = " + imageNames);

		product.setImages(images);
	}

	public static void setProductDetails(String[] detailIds, String[] detailNames, String[] detailValues, Product product)
	{
		if (detailNames == null || detailNames.length == 0)
			return;

		for (int count = 0; count < detailNames.length; count++)
		{
			String name = detailNames[count];
			String value = detailValues[count];

			Integer id = Integer.parseInt(detailIds[count]);
			if (id != 0)
				product.addDetail(id, name, value);
			else if (!name.isEmpty() && !value.isEmpty())
				product.addDetail(name, value);
		}
	}

	public static void saveUploadedImages(MultipartFile mainImageMultipart, MultipartFile[] extraImageMultipart,
			Product savedProduct) throws IOException
	{
		if (!mainImageMultipart.isEmpty())
		{
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			String uploadDir = "../product-images/" + savedProduct.getId();

			FileUploadUtil.cleanDirectory(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
		}

		if (extraImageMultipart.length > 0)
		{
			String uploadDir = "../product-images/" + savedProduct.getId() + "/extras";

			for (MultipartFile multipartFile : extraImageMultipart)
			{
				if (multipartFile.isEmpty())
					continue;

				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			}
		}
	}

	public static void setNewExtraImageNames(MultipartFile[] extraImageMultipart, Product product)
	{
		if (extraImageMultipart.length > 0)
			for (MultipartFile multipartFile : extraImageMultipart)
				if (!multipartFile.isEmpty())
				{
					String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

					if (!product.containImageName(fileName))
						product.addExtraImage(fileName);
				}
	}

	public static void setMainImageName(MultipartFile mainImageMultipart, Product product)
	{
		if (!mainImageMultipart.isEmpty())
		{
			String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
			product.setMainImage(fileName);
		}
	}
}
