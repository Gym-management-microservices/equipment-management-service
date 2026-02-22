package micro.gym.equipmentmanagementservice.repository;

import micro.gym.equipmentmanagementservice.model.Equipment;
import micro.gym.equipmentmanagementservice.model.EquipmentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, EquipmentId> {
}
