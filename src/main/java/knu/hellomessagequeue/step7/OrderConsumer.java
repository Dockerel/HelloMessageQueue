package knu.hellomessagequeue.step7;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class OrderConsumer {

    private static final int MAX_RETRIES = 3;
    private int retryCount = 0;

    @RabbitListener(queues = RabbitMQConfig.ORDER_COMPLETED_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public void processOrder(String message, Channel channel, @Header("amqp_deliveryTag") Long tag) {
        try {
            if ("fail".equalsIgnoreCase(message)) {
                if (retryCount < MAX_RETRIES) {
                    System.err.println("#### Fail & Retry = " + retryCount);
                    retryCount++;
                    throw new RuntimeException(message);
                } else {
                    System.err.println("#### 최대 횟수 초과, DLQ 이동 시킴 ");
                    retryCount = 0;
                    channel.basicNack(tag, false, false);
                    return;
                }
            }
            // 성공 처리
            System.out.println("# 성공 : " + message);
            channel.basicAck(tag, false);
        } catch (Exception e) {
            System.err.println("# error 발생 : " + e.getMessage());
            try {
                channel.basicReject(tag, true);
            } catch (IOException e1) {
                System.err.println("# fail & reject message : " + e.getMessage());
            }
        }
    }
}
