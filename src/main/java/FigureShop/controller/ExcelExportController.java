package FigureShop.controller;

import FigureShop.model.Order;
import FigureShop.model.User;
import FigureShop.repository.OrderRepository;
import FigureShop.repository.ProductRepository;
import FigureShop.repository.UserRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
public class ExcelExportController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @GetMapping("/export-user")
    public ResponseEntity<byte[]> exportUser() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Users");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Username");
            header.createCell(1).setCellValue("Phone");
            header.createCell(2).setCellValue("Email");

            List<User> users = userRepository.findAll();
            int rowIdx = 1;
            for (User user : users) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(user.getUsername());
                row.createCell(1).setCellValue(user.getPhone());
                row.createCell(2).setCellValue(user.getEmail());
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=userslist.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/export-order")
    public ResponseEntity<byte[]> exportOrder() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Order");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Username");
            header.createCell(1).setCellValue("customerName");
            header.createCell(2).setCellValue("phoneNumber");
            header.createCell(3).setCellValue("eMail");
            header.createCell(4).setCellValue("adress");
            header.createCell(5).setCellValue("note");
            header.createCell(6).setCellValue("payment");
            header.createCell(7).setCellValue("totalPrice");
            header.createCell(8).setCellValue("Voucher");
            List<Order> orders = orderRepository.findAll();
            List<User> users = userRepository.findAll();
            int rowIdx = 1;

                for (Order order : orders) {
                    Row row = sheet.createRow(rowIdx++);
                    row.createCell(0).setCellValue(order.getUsername());
                    row.createCell(1).setCellValue(order.getCustomerName());
                    row.createCell(2).setCellValue(order.getPhoneNumber());
                    row.createCell(3).setCellValue(order.getEMail());
                    row.createCell(4).setCellValue(order.getAddress());
                    row.createCell(5).setCellValue(order.getNote());
                    row.createCell(6).setCellValue(order.getPayment());
                    row.createCell(7).setCellValue(order.getTotalPrice());
                    row.createCell(8).setCellValue(order.getVoucherCode());
                }




            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=orderslist.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

