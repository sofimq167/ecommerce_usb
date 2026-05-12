package co.edu.usbcali.ecommerceusb.controllers;

import co.edu.usbcali.ecommerceusb.dto.CreateInventoryMovementRequest;
import co.edu.usbcali.ecommerceusb.dto.InventoryMovementResponse;
import co.edu.usbcali.ecommerceusb.dto.UpdateInventoryMovementRequest;
import co.edu.usbcali.ecommerceusb.service.InventoryMovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory-movement")
public class InventoryMovementController {

    @Autowired
    private InventoryMovementService inventoryMovementService;

    @GetMapping("/all")
    public List<InventoryMovementResponse> getAll() {
        return inventoryMovementService.getInventoryMovements();
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryMovementResponse> getInventoryMovementById(
            @PathVariable Integer id) throws Exception {
        return new ResponseEntity<>(
                inventoryMovementService.getInventoryMovementById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<InventoryMovementResponse> createInventoryMovement(
            @RequestBody CreateInventoryMovementRequest createInventoryMovementRequest) throws Exception {
        return new ResponseEntity<>(
                inventoryMovementService.createInventoryMovement(createInventoryMovementRequest),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryMovementResponse> updateInventoryMovement(
            @PathVariable Integer id,
            @RequestBody UpdateInventoryMovementRequest updateInventoryMovementRequest) throws Exception {
        return new ResponseEntity<>(inventoryMovementService.updateInventoryMovement(id, updateInventoryMovementRequest), HttpStatus.OK);
    }
}