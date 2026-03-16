package micro.gym.equipmentmanagementservice.service;

import micro.gym.equipmentmanagementservice.dto.EquipmentPaymentDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.stereotype.Service;

@Service
public class PaymentEquipmentConsumer {
    @RabbitListener(queues = "pagos-queue")
    public void procesarPago(EquipmentPaymentDTO payment) {
        try {
            System.out.println("Procesando pago:");
            System.out.println("  Equipo: " + payment.getEquipmentName());
            System.out.println("  Monto:  $" + payment.getAmount());

            if (!procesoPagoExitoso(payment)) {
                throw new RuntimeException("Fallo en el procesamiento del pago");
            }

            System.out.println("Pago procesado exitosamente para: " + payment.getEquipmentName());

        } catch (Exception e) {
            System.err.println("Error procesando pago, enviando a DLQ: " + e.getMessage());
            throw new AmqpRejectAndDontRequeueException("Error en el pago, enviando a DLQ", e);
        }
    }

    @RabbitListener(queues = "pagos-dlq")
    public void procesarPagoFallido(EquipmentPaymentDTO payment) {
        System.err.println("Pago fallido en DLQ:");
        System.err.println("  Equipo: " + payment.getEquipmentName());
        System.err.println("  Monto:  $" + payment.getAmount());

    }

    private boolean procesoPagoExitoso(EquipmentPaymentDTO payment) {

        return payment.getAmount() > 0;
    }
}
