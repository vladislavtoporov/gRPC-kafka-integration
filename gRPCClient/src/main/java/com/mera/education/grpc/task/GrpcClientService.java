package com.mera.education.grpc.task;

import com.mera.education.grpc.proto.task.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import net.devh.boot.grpc.examples.lib.SimpleGrpc.SimpleBlockingStub;

@Service
public class GrpcClientService {
    private ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 5051)
            .usePlaintext()
            .build();
    private Logger logger = LoggerFactory.getLogger(LogGrpcInterceptor.class);

    @GrpcClient("local-grpc-server")
    private TaskServiceGrpc.TaskServiceBlockingStub syncClient = TaskServiceGrpc.newBlockingStub(channel);

    public String sendMessage(final String name) {
        logger.info("Sent message to server via gRPC");
        try {
            Task tasking = Task.newBuilder()
                    .setMessage("Hello")
                    .build();

            final TaskResponse response = this.syncClient.task(TaskRequest.newBuilder().setTask(tasking).build());
            return response.getResult();
        } catch (final StatusRuntimeException e) {
            return "FAILED with " + e.getStatus().getCode().name();
        }

    }
}