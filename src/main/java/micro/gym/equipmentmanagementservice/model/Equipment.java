package micro.gym.equipmentmanagementservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Equipment {

    @EmbeddedId
    private EquipmentId id;

    private String name;

    @Embedded
    private EquipmentType type;

    @Embedded
    private Quantity quantity;

    @Enumerated(EnumType.STRING)
    private EquipmentStatus status;

    // Domain methods
    public void markAsAvailable() {
        this.status = EquipmentStatus.AVAILABLE;
    }

    public void markUnderMaintenance() {
        this.status = EquipmentStatus.UNDER_MAINTENANCE;
    }

    public void markOutOfService() {
        this.status = EquipmentStatus.OUT_OF_SERVICE;
    }

    public void updateQuantity(Quantity newQuantity) {
        this.quantity = newQuantity;
    }

    public void updateType(EquipmentType newType) {
        this.type = newType;
    }
}
