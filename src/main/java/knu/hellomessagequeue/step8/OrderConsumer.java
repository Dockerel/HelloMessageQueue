package knu.hellomessagequeue.step8;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;


@Component
public class OrderConsumer {

    private final RabbitTemplate rabbitTemplate;
//    private final RetryTemplate retryTemplate;

    public OrderConsumer(RabbitTemplate rabbitTemplate, RetryTemplate retryTemplate) {
        this.rabbitTemplate = rabbitTemplate;
//        this.retryTemplate = retryTemplate;
    }

    private int retryCount;

    @RabbitListener(queues = RabbitMQConfig.ORDER_COMPLETED_QUEUE)
    public void processMessage(String message) {
        System.out.println("Received message: " + message + "count : " + retryCount++);
        if ("fail".equals(message)) {
            throw new RuntimeException("- Processing failed. Retry");
        }
        System.out.println("Message processed successfully: " + message);
    }
}
