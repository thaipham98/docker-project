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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@RestController
@AllArgsConstructor
/**
 * MainController
 */
public class MainController {

    private ManagerImpl manager;
    private static final Logger logger = LogManager.getLogger(MainController.class);

    @GetMapping("/viewAllProduct")
    /**
     * viewAllProduct
     */
    public ResponseEntity<Object> viewAll() {
        logger.info("viewAll controller called");
        try {
            List<Product> result = manager.viewAllProducts();
            return ResponseHandler.generateResponse("Success viewing all products!", HttpStatus.OK, result);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }
    }


    @GetMapping("/viewProduct/{id}")
    /**
     * viewProduct
     */
    public ResponseEntity<Object> view(@PathVariable String id) {
        logger.info("view controller called with id: " + id);
        try {
            int productId = Integer.parseInt(id);
            Product result = manager.findProductById(productId);
            return ResponseHandler.generateResponse("Success viewing a specific product!", HttpStatus.OK, result);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }
    }


    @PostMapping("/addToOrder")
    /**
     * addToOrder
     */
    public ResponseEntity<Object> addToOrder(@RequestBody Map<String, Integer> body) {
        logger.info("addToOrder controller called with body: ");
        body.forEach((key, value) -> logger.info(key + ":" + value));
        try {
            int productId = body.get("productId");
            List<OrderHasProduct> result = manager.addToOrder(productId);
            return ResponseHandler.generateResponse("Success adding product to cart!", HttpStatus.OK, result);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }
    }

    @PutMapping("/deleteFromOrder")
    /**
     * deleteFromOrder
     */
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
    /**
     * showOrder
     */
    public ResponseEntity<Object> showOrder() {
        try {
            List<OrderHasProduct> result = manager.showOrder();
            return ResponseHandler.generateResponse("Success showing order!", HttpStatus.OK, result);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.MULTI_STATUS, null);
        }
    }

    @PutMapping("/checkout")
    /**
     * checkout
     */
    public void checkout() {
        manager.checkout();
    }

    @PutMapping("/clearOrder")
    /**
     * clearOrder
     */
    public void clearOrder() {
        manager.clearOrder();
    }

    @PutMapping("/removeFromOrder")
    /**
     * removeFromOrder
     */
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
