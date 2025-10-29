package com.asem.grpc_service.ServiceImpl;

import com.asem.grpc_service.AddRequest;
import com.asem.grpc_service.AddResponse;
import com.asem.grpc_service.SumServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.support.RetryTemplate;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@GrpcService
public class SumServiceImpl extends SumServiceGrpc.SumServiceImplBase {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final RetryTemplate retryTemplate;
    private static final String TOPIC = "summation";
    private final Map<String, AddResponse> processedRequests = new ConcurrentHashMap<>();

    public SumServiceImpl(KafkaTemplate<String, String> kafkaTemplate, RetryTemplate retryTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.retryTemplate = retryTemplate;
    }

    @Override
    public void addNumbers(AddRequest request, StreamObserver<AddResponse> responseObserver) {

        int firstNumber = request.getFirstNumber();
        int secondNumber = request.getSecondNumber();
        int result = firstNumber + secondNumber;

        String requestId = UUID.randomUUID().toString();

        if (processedRequests.containsKey(requestId)) {
            System.out.println(" Duplicate request detected: " + requestId);
            responseObserver.onNext(processedRequests.get(requestId));
            responseObserver.onCompleted();
            return;
        }

        String payload = String.format(
                "{\"requestId\":\"%s\",\"a\":%d,\"b\":%d,\"result\":%d}",
                requestId, firstNumber, secondNumber, result
        );

        try {
            String finalRequestId = requestId;
            retryTemplate.execute(context -> {
                kafkaTemplate.send(TOPIC, finalRequestId, payload).get(5, TimeUnit.SECONDS);
                return true;
            });
        } catch (Exception e) {
            responseObserver.onError(
                    io.grpc.Status.UNAVAILABLE
                            .withDescription("Failed to publish event after retries")
                            .withCause(e)
                            .asRuntimeException()
            );
            return;
        }

        AddResponse response = AddResponse.newBuilder()
                .setResult(result)
                .build();
        processedRequests.put(requestId, response);
        //System.out.println(" Cached requests: " + processedRequests);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}