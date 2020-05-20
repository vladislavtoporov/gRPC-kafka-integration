package com.mera.education.grpc.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class GrpcClientController {

    @Autowired
    private GrpcClientService grpcClientService;
    private Logger logger = LoggerFactory.getLogger(LogGrpcInterceptor.class);

    @PostMapping(value = "/")
    public ResponseEntity<?> sendMessage(@RequestBody String message) {
        logger.info("Incoming message: " + message);
        String response = grpcClientService.sendMessage(message);
        logger.info("Response from gRPC server: " + response);
        return ResponseEntity.ok(response);
    }

}