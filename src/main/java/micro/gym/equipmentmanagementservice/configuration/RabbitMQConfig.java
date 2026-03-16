package micro.gym.equipmentmanagementservice.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {


    public static final String PAGOS_QUEUE   = "pagos-queue";
    public static final String PAGOS_DLQ     = "pagos-dlq";
    public static final String PAGOS_EXCHANGE = "pagos.exchange";
    public static final String PAGOS_ROUTING  = "pagos.routingkey";

    @Bean
    public Queue pagosQueue() {
        return QueueBuilder.durable(PAGOS_QUEUE)
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", PAGOS_DLQ)
                .withArgument("x-message-ttl", 30000) // 30 segundos
                .build();
    }

    @Bean
    public Queue pagosDLQ() {
        return QueueBuilder.durable(PAGOS_DLQ).build();
    }

    @Bean
    public TopicExchange pagosExchange() {
        return new TopicExchange(PAGOS_EXCHANGE);
    }

    @Bean
    public Binding pagosBinding(Queue pagosQueue, TopicExchange pagosExchange) {
        return BindingBuilder
                .bind(pagosQueue)
                .to(pagosExchange)
                .with(PAGOS_ROUTING);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
