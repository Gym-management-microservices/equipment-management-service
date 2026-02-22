package micro.gym.equipmentmanagementservice;

import micro.gym.equipmentmanagementservice.model.*;
import micro.gym.equipmentmanagementservice.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Override
    public void run(String... args) throws Exception {

        Equipment treadmill = new Equipment(
                new EquipmentId("EQ001"),
                "Treadmill",
                new EquipmentType("Cardio"),
                new Quantity(5),
                EquipmentStatus.AVAILABLE
        );

        Equipment dumbbells = new Equipment(
                new EquipmentId("EQ002"),
                "Dumbbells 10kg",
                new EquipmentType("Free Weights"),
                new Quantity(20),
                EquipmentStatus.AVAILABLE
        );

        Equipment bike = new Equipment(
                new EquipmentId("EQ003"),
                "Stationary Bike",
                new EquipmentType("Cardio"),
                new Quantity(3),
                EquipmentStatus.UNDER_MAINTENANCE
        );

        Equipment bench = new Equipment(
                new EquipmentId("EQ004"),
                "Weight Bench",
                new EquipmentType("Strength"),
                new Quantity(8),
                EquipmentStatus.AVAILABLE
        );

        equipmentRepository.saveAll(Arrays.asList(treadmill, dumbbells, bike, bench));

        System.out.println(" Equipment sample data loaded successfully.");
    }
}
