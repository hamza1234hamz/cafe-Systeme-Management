package com.hamza.cafe.service;

import com.hamza.cafe.Exception.CafeUtils;
import com.hamza.cafe.Exception.MessageError.CafeErros;
import com.hamza.cafe.Repository.BillRepository;
import com.hamza.cafe.Repository.UserRepository;
import com.hamza.cafe.entities.Bill;
import com.hamza.cafe.entities.Role;
import com.hamza.cafe.entities.User;
import com.hamza.cafe.security.config.JwtAuthenticationFilter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillServiceImpl implements BillService {
    private final JwtAuthenticationFilter filter;
    private final BillRepository billRepository;
    private final UserRepository userRepository;
    @Override
    public ResponseEntity<String> generateRaport(Map<String, Object> request) {
        try{
            String fileName;
            if (validateRequest(request)){
                if (request.containsKey("isGenerate") && !(Boolean) request.get("isGenerate")){
                    fileName=(String) request.get("uuid");
                }
                else {
                    fileName= CafeUtils.getUUID();
                    request.put("uuid",fileName);
                    insertBill(request);
                }
                log.info("1");
                String data="Name : "+request.get("name")+"\n"+
                        "Contact Number : "+request.get("contactNumber")+"\n"+
                        "Email : "+request.get("email")+"\n"+
                        "Payment method "+request.get("paymentMethod");
                log.info("2");
                Document document=new Document();
                log.info("3");
                PdfWriter.getInstance(document,new FileOutputStream(CafeErros.STORE_LOCATION+"\\"+fileName+".pdf"));
                log.info("4");
                document.open();
                log.info("5");
                setRectangleInPdf(document);
                log.info("6");
                Paragraph chunk=new Paragraph("Cafe Management system",getFront("Header"));
                log.info("7");
                chunk.setAlignment(Element.ALIGN_CENTER);
                log.info("8");
                document.add(chunk);

                Paragraph paragraph=new Paragraph(data+"\n \n",getFront("Data"));
                document.add(paragraph);
                log.info("9");
                PdfPTable table=new PdfPTable(5);
                table.setWidthPercentage(100);
                addTableHaeder(table);

                JSONArray jsonArray=CafeUtils.getJsonArrayFromString((String) request.get("productDetail"));
                log.info("KKKKKKK");
                for (int i=0;i < jsonArray.length() ;i++){
                    addRows(table,CafeUtils.getMapFromJson(jsonArray.getString(i)));
                }
                log.info("after for loup");
                document.add(table);
                log.info("add table to document ");

                Paragraph footer=new Paragraph("Total : "+request.get("totalAmount")+"\n"
                +"Thank you for visiting .",getFront("Data"));
                document.add(footer);
                log.info("add footer to doucumnet");
                document.close();
                log.info("after colsing");
                return new ResponseEntity<>("{\"uuid\":\""+fileName+"\"}",HttpStatus.OK);
            }
            else {
                return CafeUtils.getResponseEntity("Required data not found",HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex){

        }
        return CafeUtils.getResponseEntity(CafeErros.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {
        List <Bill> list=new ArrayList<>();
        String email=filter.getCurrentUset();
        var user=userRepository.findByEmail(email).orElse(null);
        if (user!=null && user.getRole()== Role.admin){
            list=billRepository.getAllBills();
        }else{
            list=billRepository.getBillByUsername(email);
        }
        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    private void addRows(PdfPTable table, Map<String, Object> data) {
        log.info("inside addRow");
        table.addCell((String) data.get("name"));
        table.addCell((String) data.get("category"));
        table.addCell((String) data.get("quantity"));
        table.addCell(Double.toString((Double) data.get("price")));
        table.addCell(Double.toString((Double) data.get("total")));
    }

    private void addTableHaeder(PdfPTable table) {
        log.info("inside addTableHxaeder");
        Stream.of("Name","Category","Quantity","Price","Sub total")
                .forEach(columnTitle -> {
                    PdfPCell header=new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    header.setBackgroundColor(BaseColor.YELLOW);
                    header.setVerticalAlignment(Element.ALIGN_CENTER);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });
    }

    private Font getFront(String type) {
        log.info("inside getfont");
        switch (type){
            case "Header" :
                Font headerFont =FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE,18,BaseColor.BLACK);
                headerFont.setStyle(Font.BOLD);
                return headerFont;
            case "Data":
                Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN,11,BaseColor.BLACK);
                dataFont.setStyle(Font.BOLD);
                return dataFont;
            default:
                return new Font();
        }
    }


    private void insertBill(Map<String, Object> request) {
        try{
            Bill bill=new Bill();
            bill.setUuid((String) request.get("uuid"));
            bill.setName((String)request.get("name"));
            bill.setEmail((String) request.get("email"));
            bill.setContactNumber((String) request.get("contactNumber"));
            bill.setPaymentMethod((String) request.get("paymentMethod"));
            bill.setTotal(Integer.parseInt((String) request.get("totalAmount")));
            bill.setProductDetail((String) request.get("productDetail"));
            bill.setCreatedBy(filter.getCurrentUset());
            billRepository.save(bill);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private boolean validateRequest(Map<String, Object> request) {
        return request.containsKey("name") &&
                request.containsKey("contactNumber") &&
                request.containsKey("email") &&
                request.containsKey("paymentMethod") &&
                request.containsKey("productDetail") &&
                request.containsKey("totalAmount");
    }
    private void setRectangleInPdf(Document document) throws DocumentException {
        log.info("inside setRectangleInPdf");
        Rectangle rect=new Rectangle(577,825 ,18,15);
        rect.enableBorderSide(1);
        rect.enableBorderSide(2);
        rect.enableBorderSide(4);
        rect.enableBorderSide(8);
        rect.setBorderColor(BaseColor.BLACK);
        rect.setBorderWidth(1);
        document.add(rect);
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> request) {
        log.info("inside getPdf : request {}",request);
        try {
            byte[] byteArray=new byte[0];
            if (!request.containsKey("uuid") && validateRequest(request)){
                return new ResponseEntity<>(byteArray,HttpStatus.BAD_REQUEST);
            }
            String fillePath=CafeErros.STORE_LOCATION+"\\"+(String) request.get("uuid")+".pdf";
            if(CafeUtils.isFileExsit(fillePath)){
                byteArray=getByteArray(fillePath);
                return new ResponseEntity<>(byteArray,HttpStatus.OK);
            }
            else{
                request.put("isGenerate",false);
                generateRaport(request);
                byteArray=getByteArray(fillePath);
                return new ResponseEntity<>(byteArray,HttpStatus.OK);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }



    private byte[] getByteArray(String fillePath) throws Exception {
        File initialFile=new File(fillePath);
        InputStream targetStream=new FileInputStream(initialFile);
        byte[] byteArray= IOUtils.toByteArray(targetStream);
        targetStream.close();
        return byteArray;
    }

    @Override
    public ResponseEntity<String> deleteBill(Integer id) {
        try {
            Optional optional=billRepository.findById(id);
            if (!optional.isEmpty()){
                billRepository.deleteById(id);
                return CafeUtils.getResponseEntity("Bill successfully deleted",HttpStatus.OK);

            }else {
                return CafeUtils.getResponseEntity("Bill id does not exist",HttpStatus.OK);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeErros.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
