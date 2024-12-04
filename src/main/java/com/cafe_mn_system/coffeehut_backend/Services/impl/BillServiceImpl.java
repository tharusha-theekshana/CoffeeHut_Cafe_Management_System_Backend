package com.cafe_mn_system.coffeehut_backend.Services.impl;

import com.cafe_mn_system.coffeehut_backend.Jwt.JwtFilter;
import com.cafe_mn_system.coffeehut_backend.Models.Bill;
import com.cafe_mn_system.coffeehut_backend.Repo.BillRepo;
import com.cafe_mn_system.coffeehut_backend.Services.BillService;
import com.cafe_mn_system.coffeehut_backend.Utils.CoffeeHutConstants;
import com.cafe_mn_system.coffeehut_backend.Utils.CoffeeHutUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.util.Map;
import java.util.stream.Stream;

@Service
@Slf4j
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepo billRepo;

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> generateReport(Map<String, String> requestMap) {
        log.info("Inside generate Report");

        try {
            String fileName;

            if (validateRequestMap(requestMap)) {
                if (requestMap.containsKey("isGenerate") && !(requestMap.get("isGenerate") == "true")) {

                    fileName = requestMap.get("uuid").toString();
                } else {
                    fileName = CoffeeHutUtils.getUuid();
                    requestMap.put("uuid", fileName);
                    insertBill(requestMap);
                }

                String data = "\nName : \t\t\t" + requestMap.get("name") + "\n" + "Contact Number : \t" + requestMap.get("contactNumber") +
                        "\n" + "Email : " + requestMap.get("email") + "\n" + "Payment Method : " + requestMap.get("paymentMethod") + "\n \n";

                // Create document
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(CoffeeHutConstants.LOCATION + "\\" + fileName + ".pdf"));
                document.open();

                setRectangleAndPdf(document);

                // Header text
                Paragraph headerPara = new Paragraph("CoffeeHut Cafe", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, BaseColor.BLACK));
                headerPara.setAlignment(Element.ALIGN_CENTER);
                document.add(headerPara);

                // Bill id text
                Paragraph billIdPara = new Paragraph("\n" + "Bill Id : " + requestMap.get("uuid"), FontFactory.getFont(FontFactory.TIMES_BOLD, 11, BaseColor.BLACK));
                billIdPara.setAlignment(Element.ALIGN_CENTER);
                document.add(billIdPara);

                // Bill data text ( name, contact number , email , payment method )
                Paragraph dataPara = new Paragraph(data, FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, BaseColor.BLACK));
                document.add(dataPara);

                // Create table for inout product data
                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                addTableHeader(table);

                JSONArray jsonArray = CoffeeHutUtils.getJsonArray(requestMap.get("productDetails"));
                System.out.println(jsonArray);

                for (int i = 0; i < jsonArray.length(); i++) {
                    addRow(table, CoffeeHutUtils.getMapFromJson(jsonArray.getString(i)));
                }

                document.add(table);

                // Total amount text
                Paragraph totalAmountPara = new Paragraph("\nTotal Amount ( Rs ) : " + Double.parseDouble(requestMap.get("totalAmount")) , FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, BaseColor.BLACK));
                document.add(totalAmountPara);

                // Footer text
                Paragraph footerPara = new Paragraph("\n" + "Thank you for visiting. Please visit again .. !",FontFactory.getFont(FontFactory.TIMES_BOLD, 11, BaseColor.BLACK));
                footerPara.setAlignment(Element.ALIGN_CENTER);
                document.add(footerPara);

                document.close();

                return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.generateBillMessage(CoffeeHutUtils.getUuid()) , HttpStatus.OK);
            } else {
                return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.INVALID_DATA_BILL, HttpStatus.BAD_REQUEST);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return CoffeeHutUtils.getResponseEntity(CoffeeHutConstants.MESSAGE, CoffeeHutConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void addRow(PdfPTable table, Map<String, Object> data) {
        // Define the font with size 10
        Font font = new Font(Font.FontFamily.HELVETICA, 10);

        // Add each cell with the specified font
        PdfPCell nameCell = new PdfPCell(new Phrase(data.get("name").toString(), font));
        PdfPCell categoryCell = new PdfPCell(new Phrase(data.get("category").toString(), font));
        PdfPCell quantityCell = new PdfPCell(new Phrase(data.get("quantity").toString(), font));
        PdfPCell priceCell = new PdfPCell(new Phrase(data.get("price").toString(), font));
        PdfPCell totalCell = new PdfPCell(new Phrase(data.get("total").toString(), font));

        // Optional: Set alignment for each cell
        nameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        categoryCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        quantityCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        priceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        totalCell.setHorizontalAlignment(Element.ALIGN_CENTER);

        // Optional: Set padding for each cell
        nameCell.setPadding(5);
        categoryCell.setPadding(5);
        quantityCell.setPadding(5);
        priceCell.setPadding(5);
        totalCell.setPadding(5);

        // Add cells to the table
        table.addCell(nameCell);
        table.addCell(categoryCell);
        table.addCell(quantityCell);
        table.addCell(priceCell);
        table.addCell(totalCell);
    }


    // Add table headers and header styles
    private void addTableHeader(PdfPTable table) {
        Stream.of("Name", "Category", "Quantity", "Price ( Rs ) ", "Sub Total ( Rs )")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(1);

                    Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                    header.setPhrase(new Phrase(columnTitle, boldFont));

                    header.setBackgroundColor(BaseColor.GRAY);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_CENTER);

                    header.setPaddingTop(10f);
                    header.setPaddingBottom(10f);

                    table.addCell(header);

                });
    }

    // Create a pdf are
    private void setRectangleAndPdf(Document document) throws DocumentException {
        Rectangle rect = new Rectangle(577, 825, 18, 15);
        rect.enableBorderSide(1);
        rect.enableBorderSide(2);
        rect.enableBorderSide(4);
        rect.enableBorderSide(8);
        rect.setBackgroundColor(BaseColor.WHITE);
        rect.setBorderWidth(1);
        document.add(rect);

    }

    // Insert bill data to database
    private void insertBill(Map<String, String> requestMap) {
        try {
            Bill bill = new Bill();
            bill.setUuid(requestMap.get("uuid").toString());
            bill.setName(requestMap.get("name").toString());
            bill.setEmail(requestMap.get("email").toString());
            bill.setContactNumber(requestMap.get("contactNumber").toString());
            bill.setPaymentMethod(requestMap.get("paymentMethod").toString());
            bill.setTotal(Double.parseDouble(requestMap.get("totalAmount")));
            bill.setProductDetail(requestMap.get("productDetails").toString());
            bill.setCreatedBy(jwtFilter.getCurrentUser());

            // Save bill to database
            billRepo.save(bill);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    // Validate request map
    private boolean validateRequestMap(Map<String, String> requestMap) {
        return requestMap.containsKey("name") && requestMap.containsKey("contactNumber") && requestMap.containsKey("email") && requestMap.containsKey("paymentMethod")
                && requestMap.containsKey("productDetails") && requestMap.containsKey("totalAmount");
    }
}
