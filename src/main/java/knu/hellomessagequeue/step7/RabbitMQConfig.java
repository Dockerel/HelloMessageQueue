package knu.hellomessagequeue.step7;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_COMPLETED_QUEUE = "order_completed_queue";
    public static final String ORDER_EXCHANGE = "order_completed_exchange";
    public static final String DLQ = "deadLetterQueue";
    public static final String DLX = "deadLetterExchange";

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange(DLX);
    }

    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(ORDER_COMPLETED_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX)
                .withArgument("x-dead-letter-routing-key", DLQ)
                .ttl(5000)
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DLQ);
    }

    @Bean
    public Binding orderCompletedBinding() {
        return BindingBuilder.bind(orderQueue()).to(orderExchange()).with("order.completed.#");
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(DLQ);
    }
}
