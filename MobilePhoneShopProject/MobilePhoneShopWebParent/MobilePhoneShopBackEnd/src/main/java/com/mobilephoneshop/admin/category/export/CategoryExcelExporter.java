package com.mobilephoneshop.admin.category.export;

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

import com.mobilephoneshop.admin.AbstractExporter;
import com.mobilephoneshop.common.entity.Category;

public class CategoryExcelExporter extends AbstractExporter
{
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;

	public CategoryExcelExporter()
	{
		workbook = new XSSFWorkbook();
	}

	private void writeHeaderLine()
	{
		sheet = workbook.createSheet("Danh sách loại hàng");
		XSSFRow row = sheet.createRow(0);

		XSSFCellStyle cellStyle = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontHeight(14);
		cellStyle.setFont(font);

		createCell(row, 0, "Mã loại hàng", cellStyle);
		createCell(row, 1, "Tên loại hàng", cellStyle);
		createCell(row, 2, "Tên riêng", cellStyle);
		createCell(row, 3, "Cho phép hiển thị", cellStyle);
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

	private void writeDataLine(List<Category> listCategory)
	{
		int rowIndex = 1;

		XSSFCellStyle style = workbook.createCellStyle();
		XSSFFont font = workbook.createFont();
		font.setFontHeight(12);
		style.setFont(font);

		for (Category category : listCategory)
		{
			XSSFRow row = sheet.createRow(rowIndex++);
			int columnIndex = 0;
			createCell(row, columnIndex++, category.getId(), style);
			createCell(row, columnIndex++, category.getName(), style);
			createCell(row, columnIndex++, category.getAlias(), style);
			createCell(row, columnIndex++, category.isEnabled(), style);
		}
	}

	public void export(List<Category> listCategories, HttpServletResponse httpServletResponse) throws IOException
	{
		super.setResponseHeader(httpServletResponse, "application/octet-stream", ".xlsx", "utf-8", "loaiHang_");
		writeHeaderLine();
		writeDataLine(listCategories);
		ServletOutputStream outputStream = httpServletResponse.getOutputStream();
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}
}
