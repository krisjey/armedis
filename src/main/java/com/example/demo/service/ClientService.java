package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.example.demo.client.NewdataClient;
import com.example.demo.configuration.NewdataClientStubFactory;
import com.example.demo.proto.EventRequest;

@Service
public class ClientService {
    @PostConstruct
    private void init() {
        String host = "localhost";
        int port = 9595;

        NewdataClientStubFactory newdataClientStubFactory = new NewdataClientStubFactory(host, port);
        NewdataClient newdataClient = new NewdataClient(newdataClientStubFactory.getBlockingStub(),
                newdataClientStubFactory.getAsyncStub(), newdataClientStubFactory.getFutureStub());

        // Request 생성
        EventRequest eventRequest = EventRequest.newBuilder()
                .setSourceId("sourceId1")
                .setEventId("eventId1")
                .build();
        EventRequest eventRequest2 = EventRequest.newBuilder()
                .setSourceId("sourceId2")
                .setEventId("eventId2")
                .build();
        EventRequest eventRequest3 = EventRequest.newBuilder()
                .setSourceId("sourceId3")
                .setEventId("eventId3")
                .build();
        List<EventRequest> eventRequests = new ArrayList<>();
        eventRequests.add(eventRequest);
        eventRequests.add(eventRequest2);
        eventRequests.add(eventRequest3);
        // Blocking Unary
        // newdataClient.sendBlockingUnaryMessage(eventRequest);
        // async Unary
        // newdataClient.sendAsyncUnaryMessage(eventRequest);
        // future Unary
        // newdataClient.sendFutureUnaryMessage(eventRequest);
        // blockingServerStream
        // newdataClient.sendBlockingServerStreamingMessage(eventRequest);
        // biStream
        newdataClient.sendBiDirectionalStreamingMessage(eventRequests);
    }
}
