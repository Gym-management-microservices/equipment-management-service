package micro.gym.equipmentmanagementservice.service;

import micro.gym.equipmentmanagementservice.configuration.RabbitMQConfig;
import micro.gym.equipmentmanagementservice.dto.EquipmentPaymentDTO;
import micro.gym.equipmentmanagementservice.model.Equipment;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentEquipmentProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void postPayment(Equipment equipment, double amount) {
        EquipmentPaymentDTO payment = new EquipmentPaymentDTO(
                equipment.getId().getEquipmentid_value(),
                equipment.getName(),
                amount
        );
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PAGOS_EXCHANGE,
                RabbitMQConfig.PAGOS_ROUTING,
                payment
        );
        System.out.println("Pago publicado para equipamiento: " + equipment.getName());
    }
}
