package micro.gym.equipmentmanagementservice.service;

import micro.gym.equipmentmanagementservice.model.*;
import micro.gym.equipmentmanagementservice.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentService {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private PaymentEquipmentProducer paymentEquipmentProducer;

    public Equipment getEquipment(EquipmentId id) {
        return equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found with ID: " + id.getEquipmentid_value()));
    }

    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    public Equipment createEquipment(Equipment equipment) {

        Equipment saved = equipmentRepository.save(equipment);
        paymentEquipmentProducer.postPayment(saved,-1); // Publica el evento de pago con un precio fijo de 100.0
        return saved;
    }

    public void changeStatus(EquipmentId id, EquipmentStatus newStatus) {
        Equipment equipment = getEquipment(id);
        switch (newStatus) {
            case AVAILABLE         -> equipment.markAsAvailable();
            case UNDER_MAINTENANCE -> equipment.markUnderMaintenance();
            case OUT_OF_SERVICE    -> equipment.markOutOfService();
        }
        equipmentRepository.save(equipment);
    }

    public void updateQuantity(EquipmentId id, int newQuantity) {
        Equipment equipment = getEquipment(id);
        equipment.updateQuantity(new Quantity(newQuantity));
        equipmentRepository.save(equipment);
    }
}
