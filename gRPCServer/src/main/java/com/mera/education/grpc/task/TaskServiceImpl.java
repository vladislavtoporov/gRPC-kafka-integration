package com.mera.education.grpc.task;

import com.mera.education.grpc.proto.task.Task;
import com.mera.education.grpc.proto.task.TaskRequest;
import com.mera.education.grpc.proto.task.TaskResponse;
import com.mera.education.grpc.proto.task.TaskServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class TaskServiceImpl extends TaskServiceGrpc.TaskServiceImplBase {
    @Autowired
    private MessageProducer producer;
    private Logger logger = LoggerFactory.getLogger(LogGrpcInterceptor.class);

    @Override
    public void task(TaskRequest req, StreamObserver<TaskResponse> responseObserver) {
        Task task = req.getTask();
        logger.info("Incoming Message from gRPC Client " + task.getMessage());
        TaskResponse response = TaskResponse.newBuilder().setResult(task.getMessage()).build();
        producer.sendMessage(task.getMessage());
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}