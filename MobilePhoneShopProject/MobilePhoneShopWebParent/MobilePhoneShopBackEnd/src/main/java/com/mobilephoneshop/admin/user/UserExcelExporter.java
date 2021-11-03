package com.mobilephoneshop.admin.user;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.mobilephoneshop.common.entity.User;

public class UserExcelExporter extends AbstractExporter
{
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;

	public UserExcelExporter()
	{
		workbook = new XSSFWorkbook();

	}

	private void writeHeaderLine()
	{
		sheet = workbook.createSheet("Danh sách nhân viên");
		XSSFRow row = sheet.createRow(0);

		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(14);
		cellStyle.setFont(font);

		createCell(row, 0, "Mã nhân viên", cellStyle);
		createCell(row, 1, "E-mail", cellStyle);
		createCell(row, 2, "Họ lót", cellStyle);
		createCell(row, 3, "Tên", cellStyle);
		createCell(row, 4, "Chức vụ", cellStyle);
		createCell(row, 5, "Cho phép hoạt động", cellStyle);
	}

	private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle style)
	{
		XSSFCell cell = row.createCell(columnIndex);
		sheet.autoSizeColumn(columnIndex);
		if (value instanceof Integer)
		{
			cell.setCellValue((Integer) value);
		} else
		{
			if (value instanceof Boolean)
			{
				cell.setCellValue((Boolean) value);
			} else
			{
				cell.setCellValue((String) value);
			}
		}
		cell.setCellStyle(style);
	}

	private void writeDataLine(List<User> listUsers)
	{
		int rowIndex = 1;
		
		XSSFCellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(12);
		style.setFont(font);
		
		for (User user : listUsers)
		{
			XSSFRow row = sheet.createRow(rowIndex++);
			int columnIndex = 0;
			createCell(row, columnIndex++, user.getId(), style);
			createCell(row, columnIndex++, user.getEmail(), style);
			createCell(row, columnIndex++, user.getLastName(), style);
			createCell(row, columnIndex++, user.getFirstName(), style);
			createCell(row, columnIndex++, user.getRoles().toString(), style);
			createCell(row, columnIndex++, user.isEnabled(), style);
		}
	}

	public void export(List<User> listUsers, HttpServletResponse httpServletResponse) throws IOException
	{
		super.setResponseHeader(httpServletResponse, "application/octet-stream", ".xlsx", "utf-8");
		writeHeaderLine();
		writeDataLine(listUsers);
		ServletOutputStream outputStream = httpServletResponse.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}

}
