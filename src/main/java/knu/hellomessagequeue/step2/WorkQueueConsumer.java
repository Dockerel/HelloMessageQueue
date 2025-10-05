//package knu.hellomessagequeue.step2;
//
//import org.springframework.stereotype.Component;
//
//@Component
//public class WorkQueueConsumer {
//
//    public void workQueueTask(String message) {
//        String[] messageParts = message.split("\\|");
//        String originMessage = messageParts[0];
//        int duration = Integer.parseInt(messageParts[1].trim());
//
//        System.out.println("# Received: " + originMessage + " (duration: " + duration + " ms)");
//
//        try {
//            int seconds = duration / 1000;
//            for (int i = 0; i < seconds; i++) {
//                Thread.sleep(1000);
//                System.out.print(".");
//            }
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//        System.out.println("\n [X] Completed: " + originMessage);
//    }
//}
