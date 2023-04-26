package com.example.finalproject.Controller;

import com.example.finalproject.Manager.ManagerImpl;
import com.example.finalproject.Model.OrderHasProduct;
import com.example.finalproject.Model.Product;
import com.example.finalproject.Response.ResponseHandler;
import jakarta.persistence.criteria.Order;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class MainController {

    private ManagerImpl manager;

    @GetMapping("/")
    public String index() {
        return "Đây là Hà Nội của tao";
    }

    @GetMapping("/viewAllProduct")
//    public List<Product> viewAll() {
    public ResponseEntity<Object> viewAll() {
        System.out.println("viewAll controller called");
        try {
            List<Product> result = manager.viewAllProducts();
            return ResponseHandler.generateResponse("Success viewing all products!", HttpStatus.OK, result);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }
    }

    @GetMapping("/viewProduct/{id}")
    public ResponseEntity<Object> view(@PathVariable String id) {
        System.out.println("view controller called with id: " + id);
        try {
            int productId = Integer.parseInt(id);
            Product result = manager.findProductById(productId);
            return ResponseHandler.generateResponse("Success viewing a specific product!", HttpStatus.OK, result);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }
    }

    @PostMapping("/addToOrder")
    public ResponseEntity<Object> addToOrder(@RequestBody Map<String, Integer> body) {
        System.out.println("addToOrder controller called with body: ");
        body.forEach((key, value) -> System.out.println(key + ":" + value));
        try {
            int productId = body.get("productId");
            List<OrderHasProduct> result = manager.addToOrder(productId);
            return ResponseHandler.generateResponse("Success adding product to cart!", HttpStatus.OK, result);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }
    }

    @PutMapping("/deleteFromOrder")
    public ResponseEntity<Object> deleteFromOrder(@RequestBody Map<String, Integer> body) {
        try {
            int productId = body.get("productId");
            List<OrderHasProduct> result = manager.deleteFromOrder(productId);
            return ResponseHandler.generateResponse("Success deleting a product from cart!", HttpStatus.OK, result);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }
    }

    @GetMapping("/showOrder")
    public ResponseEntity<Object> showOrder() {
        try {
            List<OrderHasProduct> result = manager.showOrder();
            return ResponseHandler.generateResponse("Success showing order!", HttpStatus.OK, result);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }
    }

    @PutMapping("/checkout")
    public void checkout() {
        manager.checkout();
    }

    @PutMapping("/clearOrder")
    public void clearOrder() {
        manager.clearOrder();
    }

    @PutMapping("/removeFromOrder")
    public ResponseEntity<Object> editOrder(@RequestBody Map<String, Integer> body) {
        try {
            int productId = body.get("productId");
            int quantity = body.get("quantity");
            List<OrderHasProduct> result = manager.removeFromOrder(productId, quantity);
            return ResponseHandler.generateResponse("Success removing the product from order!", HttpStatus.OK, result);
        } catch ( Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }
    }

}
