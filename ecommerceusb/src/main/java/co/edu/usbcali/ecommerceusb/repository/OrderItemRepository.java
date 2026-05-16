package co.edu.usbcali.ecommerceusb.repository;
import co.edu.usbcali.ecommerceusb.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    Boolean existsByOrderIdAndProductId(Integer orderId, Integer productId);
    Boolean existsByProductId(Integer productId);
    Boolean existsByOrderId(Integer orderId);
}
