package co.edu.usbcali.ecommerceusb.mapper;

import co.edu.usbcali.ecommerceusb.dto.CreateInventoryRequest;
import co.edu.usbcali.ecommerceusb.dto.InventoryResponse;
import co.edu.usbcali.ecommerceusb.model.Inventory;
import co.edu.usbcali.ecommerceusb.model.Product;

import java.time.OffsetDateTime;
import java.util.List;

public class InventoryMapper {

    public static InventoryResponse modelToInventoryResponse(Inventory inventory) {
        return InventoryResponse.builder()
                .id(inventory.getId())
                .productId(inventory.getProduct() != null ? inventory.getProduct().getId() : null)
                .productName(inventory.getProduct() != null ? inventory.getProduct().getName() : null)
                .stock(inventory.getStock())
                .build();
    }

    public static List<InventoryResponse> modelToInventoryResponseList(List<Inventory> inventories) {
        return inventories.stream().map(InventoryMapper::modelToInventoryResponse).toList();
    }

    public static Inventory createInventoryRequestToInventory(CreateInventoryRequest request,
                                                              Product product) {
        return Inventory.builder()
                .product(product)
                .stock(request.getStock())
                .updatedAt(OffsetDateTime.now())
                .build();
    }
}