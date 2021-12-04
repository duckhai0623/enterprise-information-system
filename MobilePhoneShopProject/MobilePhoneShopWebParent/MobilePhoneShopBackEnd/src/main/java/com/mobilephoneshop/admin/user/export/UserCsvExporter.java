package com.mobilephoneshop.admin.user.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.mobilephoneshop.common.entity.User;

public class UserCsvExporter extends AbstractExporter
{
	public void export(List<User> listUsers, HttpServletResponse httpServletResponse) throws IOException
	{
		super.setResponseHeader(httpServletResponse, "text/csv", ".csv", "utf-8");
		ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(httpServletResponse.getWriter(),
				CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader =
		{ "Mã nhân viên", "E-mail", "Họ", "Tên", "Chức vụ", "Cho phép hoạt động" };
		String[] fieldMapping =
		{ "id", "email", "lastName", "firstName", "roles", "enabled" };
		csvBeanWriter.writeHeader(csvHeader);
		for (User user : listUsers)
		{
			csvBeanWriter.write(user, fieldMapping);
		}
		csvBeanWriter.close();
	}
}
