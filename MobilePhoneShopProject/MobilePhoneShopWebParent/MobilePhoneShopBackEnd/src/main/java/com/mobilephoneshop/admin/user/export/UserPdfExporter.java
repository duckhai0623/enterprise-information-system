package com.mobilephoneshop.admin.user.export;

import java.io.IOException;
import java.util.List;
import java.awt.Color;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mobilephoneshop.admin.AbstractExporter;
import com.mobilephoneshop.common.entity.User;

public class UserPdfExporter extends AbstractExporter
{

	public void export(List<User> listUsers, HttpServletResponse httpServletResponse) throws IOException
	{
		super.setResponseHeader(httpServletResponse, "application/pdf", ".pdf", "windows-1258", "nhanVien_");
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, httpServletResponse.getOutputStream());
		document.open();
		Font font = new Font(Font.BOLD);
		font.setStyle(18);
		font.setColor(Color.BLUE);

		
		Paragraph paragraph = new Paragraph("Phạm Đức Khải", font);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(paragraph);
		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100f);
		table.setSpacingBefore(10);
		table.setWidths(new float[] {1.5f, 3.5f, 3.0f, 3.0f, 3.0f, 1.5f});
		writeTableHeader(table);
		writeTableData(table,listUsers);
		document.add(table);
		document.close();
		
	}

	private void writeTableData(PdfPTable table, List<User> listUsers)
	{
		for (User user : listUsers)
		{
			table.addCell(String.valueOf(user.getId()));
			table.addCell(user.getEmail());
			table.addCell(user.getLastName());
			table.addCell(user.getFirstName());
			table.addCell(user.getRoles().toString());
			table.addCell(String.valueOf(user.isEnabled()));
		}
		
	}

	private void writeTableHeader(PdfPTable table)
	{
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLUE);
		cell.setPadding(5);
		
		Font font = new Font();
		font.setColor(Color.WHITE);
		cell.setPhrase(new Phrase("Mã nhân viên",font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("E-mail", font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Họ lót",font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Tên",font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Chức vụ",font));
		table.addCell(cell);
		cell.setPhrase(new Phrase("Cho phép hoạt động",font));
		table.addCell(cell);
	}
}
