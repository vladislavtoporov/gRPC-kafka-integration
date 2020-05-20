package com.mera.education.grpc.task;

import com.mera.education.grpc.proto.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.concurrent.CountDownLatch;

public class MessageListener {

    private CountDownLatch latch = new CountDownLatch(3);

    private CountDownLatch partitionLatch = new CountDownLatch(2);

    private CountDownLatch filterLatch = new CountDownLatch(2);

    private CountDownLatch greetingLatch = new CountDownLatch(1);
    private Logger logger = LoggerFactory.getLogger(LogGrpcInterceptor.class);

//    @KafkaListener(topics = "${message.topic.name}", groupId = "foo", containerFactory = "fooKafkaListenerContainerFactory")
//    public void listenGroupFoo(String message) {
//        System.out.println("Received Messasge in group 'foo': " + message);
//        latch.countDown();
//    }
//
//    @KafkaListener(topics = "${message.topic.name}", groupId = "bar", containerFactory = "barKafkaListenerContainerFactory")
//    public void listenGroupBar(String message) {
//        System.out.println("Received Messasge in group 'bar': " + message);
//        latch.countDown();
//    }

    @KafkaListener(topics = "${message.topic.name}", containerFactory = "headersKafkaListenerContainerFactory")
    public void listenWithHeaders(@Payload String message, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        logger.info("Received Messasge: " + message + " from partition: " + partition);
        latch.countDown();
    }

    @KafkaListener(topicPartitions = @TopicPartition(topic = "${partitioned.topic.name}", partitions = {"0", "1"}), containerFactory = "partitionsKafkaListenerContainerFactory")
    public void listenToParition(@Payload String message, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        logger.info("Received Message: " + message + " from partition: " + partition);
        this.partitionLatch.countDown();
    }

    @KafkaListener(topics = "${filtered.topic.name}", containerFactory = "filterKafkaListenerContainerFactory")
    public void listenWithFilter(String message) {
        logger.info("Recieved Message in filtered listener: " + message);
        this.filterLatch.countDown();
    }

    @KafkaListener(topics = "${greeting.topic.name}", containerFactory = "greetingKafkaListenerContainerFactory")
    public void greetingListener(Task greeting) {
        logger.info("Recieved greeting message: " + greeting);
        this.greetingLatch.countDown();
    }

}