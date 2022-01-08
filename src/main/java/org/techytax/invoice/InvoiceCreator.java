package org.techytax.invoice;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.springframework.stereotype.Component;
import org.techytax.domain.Activity;
import org.techytax.domain.Invoice;
import org.techytax.saas.domain.Registration;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Locale;

@Component
public class InvoiceCreator {

  private double vat;
  private BigDecimal netAmount;
  private BigDecimal vatAmount;

  private void addSpace(PdfPTable table) {
    PdfPCell cell = new PdfPCell();
    cell.setFixedHeight(15f);
    cell.setBorder(PdfPCell.NO_BORDER);
    table.addCell(cell);
  }

  public String formatDecimal(BigDecimal b) {
    Locale loc = new Locale("nl", "NL", "EURO");
    NumberFormat n = NumberFormat.getCurrencyInstance(loc);
    double doublePayment = b.doubleValue();
    return n.format(doublePayment);
  }

  public byte[] createPdfInvoice(Invoice invoice, Registration registration) {
    Document document = new Document(PageSize.A4);
    ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
    try {
      PdfWriter.getInstance(document, byteOutputStream);

      document.open();

      Font titleFont = new Font(Font.FontFamily.COURIER, 25, Font.BOLD);
      Font font = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD);

      Font totalAmountFont = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD);
      Font headerFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);

      PdfPTable subTable = new PdfPTable(1);
      PdfPCell cell = new PdfPCell(new Paragraph(""));
      cell.setFixedHeight(50f);
      subTable.addCell(cell);

      subTable = new PdfPTable(2);
      cell = new PdfPCell(new Paragraph("Nummer"));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph(invoice.getInvoiceNumber()));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph("Datum"));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph(invoice.getSent().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph("Maand"));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph(invoice.getMonth()));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);

      if (invoice.getUnitsOfWork() > 0) {
        cell = new PdfPCell(new Paragraph("Betalingstermijn"));
        cell.setBorder(PdfPCell.NO_BORDER);
        subTable.addCell(cell);
        cell = new PdfPCell(new Paragraph(invoice.getProject().getPaymentTermDays() + " dagen"));
        cell.setBorder(PdfPCell.NO_BORDER);
        subTable.addCell(cell);
        cell = new PdfPCell(new Paragraph("Vervaldatum"));
        cell.setBorder(PdfPCell.NO_BORDER);
        subTable.addCell(cell);
        cell = new PdfPCell(new Paragraph(invoice.getSent().plusDays(invoice.getProject().getPaymentTermDays()).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
        cell.setBorder(PdfPCell.NO_BORDER);
        subTable.addCell(cell);
      }

      PdfPTable table = new PdfPTable(1);
      table.setWidthPercentage(75);

      Paragraph title = new Paragraph(registration.getCompanyData().getCompanyName(), titleFont);
      cell = new PdfPCell(title);
      cell.setBorder(PdfPCell.NO_BORDER);
      table.addCell(cell);

      addSpace(table);
      addSpace(table);
      addSpace(table);

      String invoiceType = "Factuur";
      if (invoice.getUnitsOfWork() < 0) {
        invoiceType = "Creditnota";
      }
      Paragraph chunk = new Paragraph(invoiceType, font);
      cell = new PdfPCell(chunk);
      cell.setBorder(PdfPCell.NO_BORDER);
      table.addCell(cell);

      cell = new PdfPCell(subTable);
      cell.setBorder(PdfPCell.NO_BORDER);
      table.addCell(cell);

      addSpace(table);

      chunk = new Paragraph("Leverancier", font);
      subTable = new PdfPTable(2);

      cell = new PdfPCell(new Paragraph("Bedrijfsnaam"));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph(registration.getCompanyData().getCompanyName()));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph("Naam"));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph(registration.getPersonalData().getFullName()));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph("Adres"));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph(registration.getCompanyData().getFullAddress()));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph("btw-nr"));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph("NL " + registration.getFiscalData().getVatNumber()));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph("KvK-nr"));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph(Long.toString(registration.getCompanyData().getChamberOfCommerceNumber())));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph("Rekening"));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph(registration.getCompanyData().getAccountNumber()));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(chunk);
      cell.setBorder(PdfPCell.NO_BORDER);
      table.addCell(cell);
      cell = new PdfPCell(subTable);
      cell.setBorder(PdfPCell.NO_BORDER);
      table.addCell(cell);

      addSpace(table);

      chunk = new Paragraph("Afnemer", font);
      subTable = new PdfPTable(2);
      cell = new PdfPCell(new Paragraph("Naam"));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph(invoice.getProject().getCustomer().getName()));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph("Adres"));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph(invoice.getProject().getCustomer().getAddress()));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);

      cell = new PdfPCell(chunk);
      cell.setBorder(PdfPCell.NO_BORDER);
      table.addCell(cell);
      cell = new PdfPCell(subTable);
      cell.setBorder(PdfPCell.NO_BORDER);
      table.addCell(cell);

      addSpace(table);
      addSpace(table);

      chunk = new Paragraph("Rekening", font);

      subTable = new PdfPTable(4);
      subTable.setWidths(new int[] { 3, 1, 1, 2 });
      Paragraph headerChunk = new Paragraph("Omschrijving", headerFont);
      cell = new PdfPCell(headerChunk);
      BaseColor color = new BaseColor(225, 245, 245);
      cell.setBackgroundColor(color);
      subTable.addCell(cell);
      headerChunk = new Paragraph("Aantal uren", headerFont);
      cell = new PdfPCell(headerChunk);
      cell.setBackgroundColor(color);
      subTable.addCell(cell);
      headerChunk = new Paragraph("Tarief", headerFont);
      cell = new PdfPCell(headerChunk);
      cell.setBackgroundColor(color);
      subTable.addCell(cell);
      headerChunk = new Paragraph("Totaal", headerFont);
      cell = new PdfPCell(headerChunk);
      cell.setBackgroundColor(color);
      subTable.addCell(cell);

      String description = invoice.getProject().getActivityDescription() + ", " + invoice.getMonth() + " " + invoice.getSent().minusDays(20).getYear();
      if (invoice.getUnitsOfWork() < 0) {
        description = "Creditering van factuurnummer " + invoice.getOriginalInvoiceNumber() + " (" + invoice.getProject().getActivityDescription() + ")";
      }
      cell = new PdfPCell(new Paragraph(description));
      cell.setGrayFill(0.9f);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph(Float.toString(invoice.getUnitsOfWork())));
      cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph(invoice.getProject().getRate().toString()));
      cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      netAmount = invoice.getProject().getRate().multiply(BigDecimal.valueOf(invoice.getUnitsOfWork()));
      cell = new PdfPCell(new Paragraph(formatDecimal(netAmount)));
      cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);

//      if (invoice.getDiscountPercentage() > 0) {
//        cell = new PdfPCell(new Paragraph("Korting: "+invoice.getDiscountPercentage()+"% (Versneld betalen)"));
//        cell.setGrayFill(0.9f);
//        subTable.addCell(cell);
//        cell = new PdfPCell();
//        cell.setBorder(PdfPCell.NO_BORDER);
//        subTable.addCell(cell);
//        subTable.addCell(cell);
//        Paragraph chunkMoney = new Paragraph("- "+formatDecimal(invoice.getDiscount()));
//        cell = new PdfPCell(chunkMoney);
//        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
//        cell.setBorder(PdfPCell.NO_BORDER);
//        subTable.addCell(cell);
//
//        cell = new PdfPCell(new Paragraph("Subtotaal"));
//        cell.setGrayFill(0.9f);
//        subTable.addCell(cell);
//        cell = new PdfPCell();
//        cell.setBorder(PdfPCell.NO_BORDER);
//        subTable.addCell(cell);
//        subTable.addCell(cell);
//        chunkMoney = new Paragraph(formatDecimal(invoice.getNetAmountAfterDiscount()));
//        cell = new PdfPCell(chunkMoney);
//        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
//        cell.setBorder(PdfPCell.NO_BORDER);
//        subTable.addCell(cell);
//
//      }

      vat = invoice.getProject().getVatType().getValue();
      cell = new PdfPCell(new Paragraph("btw (" + (int)(100*vat) + "%)"));
      cell.setGrayFill(0.9f);
      subTable.addCell(cell);
      cell = new PdfPCell();
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      subTable.addCell(cell);
      vatAmount = netAmount.multiply(BigDecimal.valueOf(vat));
      cell = new PdfPCell(new Paragraph(formatDecimal(vatAmount)));
      cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);

      cell = new PdfPCell(new Paragraph("Totaal"));
      cell.setGrayFill(0.9f);
      subTable.addCell(cell);
      cell = new PdfPCell();
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      subTable.addCell(cell);
      Paragraph chunkMoney = new Paragraph(formatDecimal(netAmount.add(vatAmount)), totalAmountFont);
      cell = new PdfPCell(chunkMoney);
      cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);

      cell = new PdfPCell(chunk);
      cell.setBorder(PdfPCell.NO_BORDER);
      table.addCell(cell);

      addSpace(table);

      cell = new PdfPCell(subTable);
      table.addCell(cell);

      addSpace(table);

      document.add(table);
      LineSeparator horizontalLine = new LineSeparator();
      BaseColor lineColor = new BaseColor(141, 141, 141);
      horizontalLine.setLineWidth(1f);
      horizontalLine.setLineColor(lineColor);
      document.add(horizontalLine);
    } catch (Exception de) {
      de.printStackTrace();
    }

    document.close();
    return byteOutputStream.toByteArray();
  }

  public byte[] createPdfInvoiceForBig(Invoice invoice, Registration registration, Collection<Activity> activities) {
    Document document = new Document(PageSize.A4);
    ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
    try {
      PdfWriter.getInstance(document, byteOutputStream);

      document.open();

      Font titleFont = new Font(Font.FontFamily.COURIER, 25, Font.BOLD);
      Font font = new Font(Font.FontFamily.HELVETICA, 15, Font.BOLD);

      Font totalAmountFont = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD);
      Font subTotalAmountFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
      Font headerFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);

      PdfPTable subTable = new PdfPTable(1);
      PdfPCell cell = new PdfPCell(new Paragraph(""));
      cell.setFixedHeight(50f);
      subTable.addCell(cell);

      subTable = new PdfPTable(2);
      cell = new PdfPCell(new Paragraph("Nummer"));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph(invoice.getInvoiceNumber()));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph("Datum"));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph(invoice.getSent().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph("Maand"));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph(invoice.getMonth()));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);

      if (invoice.getUnitsOfWork() > 0) {
        cell = new PdfPCell(new Paragraph("Betalingstermijn"));
        cell.setBorder(PdfPCell.NO_BORDER);
        subTable.addCell(cell);
        cell = new PdfPCell(new Paragraph(invoice.getProject().getPaymentTermDays() + " dagen"));
        cell.setBorder(PdfPCell.NO_BORDER);
        subTable.addCell(cell);
        cell = new PdfPCell(new Paragraph("Vervaldatum"));
        cell.setBorder(PdfPCell.NO_BORDER);
        subTable.addCell(cell);
        cell = new PdfPCell(new Paragraph(invoice.getSent().plusDays(invoice.getProject().getPaymentTermDays()).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
        cell.setBorder(PdfPCell.NO_BORDER);
        subTable.addCell(cell);
      }

      PdfPTable table = new PdfPTable(1);
      table.setWidthPercentage(80);

      Paragraph title = new Paragraph(invoice.getProject().getCustomer().getName(), titleFont);
      cell = new PdfPCell(title);
      cell.setBorder(PdfPCell.NO_BORDER);
      table.addCell(cell);

      addSpace(table);
      addSpace(table);
      addSpace(table);

      String invoiceType = "Factuur";
      if (invoice.getUnitsOfWork() < 0) {
        invoiceType = "Creditnota";
      }
      Paragraph chunk = new Paragraph(invoiceType, font);
      cell = new PdfPCell(chunk);
      cell.setBorder(PdfPCell.NO_BORDER);
      table.addCell(cell);

      cell = new PdfPCell(subTable);
      cell.setBorder(PdfPCell.NO_BORDER);
      table.addCell(cell);

      addSpace(table);

      chunk = new Paragraph("Leverancier", font);
      subTable = new PdfPTable(2);

      cell = new PdfPCell(new Paragraph("BIG"));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph(registration.getCompanyData().getJobsInIndividualHealthcareNumber()));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph("KvK nr"));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph(Long.toString(registration.getCompanyData().getChamberOfCommerceNumber())));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph("Bank IBAN"));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph(registration.getCompanyData().getAccountNumber()));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph("Tel"));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph(registration.getPersonalData().getPhoneNumber()));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph("E-mail"));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph(registration.getPersonalData().getEmail()));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(chunk);
      cell.setBorder(PdfPCell.NO_BORDER);
      table.addCell(cell);
      cell = new PdfPCell(subTable);
      cell.setBorder(PdfPCell.NO_BORDER);
      table.addCell(cell);

      addSpace(table);

      chunk = new Paragraph("Afnemer", font);
      subTable = new PdfPTable(2);
      cell = new PdfPCell(new Paragraph("Naam"));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph(invoice.getProject().getCustomer().getName()));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph("Adres"));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph(invoice.getProject().getCustomer().getAddress()));
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);

      cell = new PdfPCell(chunk);
      cell.setBorder(PdfPCell.NO_BORDER);
      table.addCell(cell);
      cell = new PdfPCell(subTable);
      cell.setBorder(PdfPCell.NO_BORDER);
      table.addCell(cell);

      addSpace(table);
      addSpace(table);

      chunk = new Paragraph("Rekening", font);

      subTable = new PdfPTable(3);
      subTable.setWidths(new int[] { 1, 3, 2 });
      Paragraph headerChunk = new Paragraph("Datum", headerFont);
      cell = new PdfPCell(headerChunk);
      BaseColor color = new BaseColor(225, 245, 245);
      cell.setBackgroundColor(color);
      subTable.addCell(cell);
      headerChunk = new Paragraph("Omschrijving", headerFont);
      cell = new PdfPCell(headerChunk);
      cell.setBackgroundColor(color);
      subTable.addCell(cell);
      headerChunk = new Paragraph("Bedrag", headerFont);
      cell = new PdfPCell(headerChunk);
      cell.setBackgroundColor(color);
      subTable.addCell(cell);

      BigDecimal subTotalRevenue = BigDecimal.ZERO;
      for (Activity activity: activities) {
        cell = new PdfPCell(new Paragraph(activity.getActivityDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
        subTable.addCell(cell);
        cell = new PdfPCell(new Paragraph(activity.getActivityDescription()));
        subTable.addCell(cell);
        BigDecimal revenue = activity.getRevenue();
        subTotalRevenue = subTotalRevenue.add(revenue);
        cell = new PdfPCell(new Paragraph(revenue.toString()));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        subTable.addCell(cell);
      }

      cell = new PdfPCell();
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph(" "));
      cell.setGrayFill(0.9f);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph(" "));
      subTable.addCell(cell);

      cell = new PdfPCell();
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph("Subtotaal"));
      cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
      cell.setGrayFill(0.9f);
      subTable.addCell(cell);
      Paragraph chunkMoney1 = new Paragraph(formatDecimal(subTotalRevenue), subTotalAmountFont);
      cell = new PdfPCell(chunkMoney1);
      cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
      subTable.addCell(cell);

      cell = new PdfPCell();
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph(" "));
      cell.setGrayFill(0.9f);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph(" "));
      subTable.addCell(cell);

      cell = new PdfPCell();
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph("Omzetpercentage"));
      cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
      cell.setGrayFill(0.9f);
      subTable.addCell(cell);
      Paragraph percentage = new Paragraph(invoice.getProject().getRevenuePerc().toString().replace('.', ',')+"%", subTotalAmountFont);
      cell = new PdfPCell(percentage);
      cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
      subTable.addCell(cell);

      cell = new PdfPCell();
      cell.setBorder(PdfPCell.NO_BORDER);
      subTable.addCell(cell);
      cell = new PdfPCell(new Paragraph("Totaal"));
      cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
      cell.setGrayFill(0.9f);
      subTable.addCell(cell);
      Paragraph chunkMoney = new Paragraph(formatDecimal(subTotalRevenue.multiply(invoice.getProject().getRevenuePerc().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP))), totalAmountFont);
      cell = new PdfPCell(chunkMoney);
      cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
      subTable.addCell(cell);

      cell = new PdfPCell(chunk);
      cell.setBorder(PdfPCell.NO_BORDER);
      table.addCell(cell);

      addSpace(table);

      cell = new PdfPCell(subTable);
      table.addCell(cell);

      document.add(table);
    } catch (Exception de) {
      de.printStackTrace();
    }

    document.close();
    return byteOutputStream.toByteArray();
  }
}
