package co.edu.usbcali.ecommerceusb.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "product_categories",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"product_id", "category_id"})
        }
)
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "product_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_pc_product"),
            referencedColumnName = "id"
    )
    private Product product;

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "category_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_pc_category"),
            referencedColumnName = "id"
    )
    private Category category;
}