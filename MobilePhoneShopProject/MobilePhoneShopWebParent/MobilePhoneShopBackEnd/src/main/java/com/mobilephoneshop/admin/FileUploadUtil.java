package com.mobilephoneshop.admin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil
{
	private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(FileUploadUtil.class);
	
	public static void saveFile(String uploadDirectory, String fileName, MultipartFile multipartFile) throws IOException
	{
		
		Path uploadPath = Paths.get(uploadDirectory);
		if (!Files.exists(uploadPath))
		{
			Files.createDirectories(uploadPath);
		}

		try (InputStream inputStream = multipartFile.getInputStream())
		{
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e)
		{
			throw new IOException("Không thể lưu ảnh: " + fileName, e);
		}
	}

	public static void cleanDirectory(String directory)
	{
		Path directoryPath = Paths.get(directory);
		try
		{
			Files.list(directoryPath).forEach(file ->
			{
				if (!Files.isDirectory(file))
				{
					try
					{
						Files.delete(file);
					} catch (IOException e)
					{
						LOGGER.error("Không thể xoá file: " + file);
					}
				}
			});
		} catch (IOException e2)
		{
			LOGGER.error("Không thể liệt kê đường dẫn: " + directoryPath);
		}
	}
	
	public static void removeDir(String directory)
	{
		cleanDirectory(directory);
		try
		{
			Files.delete(Paths.get(directory));
			
		} catch (IOException e)
		{
			LOGGER.error("Không thể xoá đường dẫn: " + directory);
		}
	}
}
