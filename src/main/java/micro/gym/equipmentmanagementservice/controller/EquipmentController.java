package micro.gym.equipmentmanagementservice.controller;

import micro.gym.equipmentmanagementservice.model.*;
import micro.gym.equipmentmanagementservice.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipment")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public List<Equipment> getAll() {
        return equipmentService.getAllEquipment();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public Equipment getById(@PathVariable String id) {
        return equipmentService.getEquipment(new EquipmentId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Equipment create(@RequestBody Equipment equipment) {
        return equipmentService.createEquipment(equipment);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public void changeStatus(@PathVariable String id, @RequestBody EquipmentStatus newStatus) {
        equipmentService.changeStatus(new EquipmentId(id), newStatus);
    }

    @PutMapping("/{id}/quantity")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateQuantity(@PathVariable String id, @RequestBody int quantity) {
        equipmentService.updateQuantity(new EquipmentId(id), quantity);
    }
}
