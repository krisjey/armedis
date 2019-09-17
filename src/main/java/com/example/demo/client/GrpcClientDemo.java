package com.example.demo.client;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.configuration.NewdataClientStubFactory;
import com.example.demo.proto.EventRequest;

public class GrpcClientDemo {
    public static void main(String[] args) throws InterruptedException {
        String host = "localhost";
        int port = 8081;

        NewdataClientStubFactory clientStubFactory = new NewdataClientStubFactory(host, port);

        NewdataClient grpcClient = new NewdataClient(clientStubFactory.getBlockingStub(),
                clientStubFactory.getAsyncStub(),
                clientStubFactory.getFutureStub());

        // Blocking Unary
//        grpcClient.sendBlockingUnaryMessage("Blocking Unary, gㅏ벼운 RPC, gPRC");

        // Async Unary
//        grpcClient.sendAsyncUnaryMessage("Async Unary, gㅏ벼운 RPC, gRPC");
//        Thread.sleep(3000);

        // Blocking Server Streaming
//        grpcClient.sendBlockingServerStreamingMessage("Blocking Server Streaming, gㅏ벼운 RPC, gPRC");

        // Async Server Streaming
//        grpcClient.sendAsyncServerStreamingMessage("Async Server Streaming, gㅏ벼운 RPC, gRPC");
//        Thread.sleep(3000);

        // Async Client Streaming
//        grpcClient.sendAsyncClientStreamingMessage(Arrays.asList("Async Client Streaming,", "gㅏ벼운 RPC,", "gRPC"));
//        Thread.sleep(3000);
//        clientStubFactory.shutdownChannel();
        // 아래와 같이 1초만 대기해서 서버 응답 전에 channel을 닫으면
        // 클라이언트는 응답을 못 받으며, 서버 쪽에서도 에러가 발생하지 않는다.
//        Thread.sleep(1000);

        List<EventRequest> eventRequests = new ArrayList<>();

        EventRequest eventRequest3 = EventRequest.newBuilder()
                .setSourceId("sourceId3")
                .setEventId("eventId3").build();
        
        eventRequests.add(eventRequest3);

        // Bidirectional Client Streaming
        grpcClient.sendBlockingUnaryMessage(eventRequest3);
        Thread.sleep(3000);
    }
}
