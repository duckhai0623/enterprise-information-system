package com.mobilephoneshop.admin.user;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

public class AbstractExporter
{
	public void setResponseHeader(HttpServletResponse httpServletResponse, String contentType, String extension, String charSet)
			throws IOException
	{
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
		String timestamp = dateFormat.format(new Date());
		String fileName = "nhanVien_" + timestamp + extension;
		httpServletResponse.setContentType(contentType);
		httpServletResponse.setCharacterEncoding(charSet);
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; fileName= " + fileName;
		httpServletResponse.setHeader(headerKey, headerValue);
	}
}
