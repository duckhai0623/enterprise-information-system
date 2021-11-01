package com.mobilephoneshop.admin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil
{
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
						Files.delete(directoryPath);
					} catch (IOException e)
					{
						System.out.println("Không thể xoá file: " + file);
					}
				}
			});
		} catch (IOException e2)
		{
			System.out.println("Không thể liệt kê đường dẫn: " + directoryPath);
		}
	}
}
