
package com.example.demo.client;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demo.proto.EventRequest;
import com.example.demo.proto.EventResponse;
import com.example.demo.proto.NewdataServiceGrpc.NewdataServiceBlockingStub;
import com.example.demo.proto.NewdataServiceGrpc.NewdataServiceFutureStub;
import com.example.demo.proto.NewdataServiceGrpc.NewdataServiceStub;
import com.google.common.util.concurrent.ListenableFuture;

import io.grpc.stub.StreamObserver;

public class NewdataClient {
    private static final Logger logger = LoggerFactory.getLogger(NewdataClient.class);
    private final NewdataServiceBlockingStub blockingStub;
    private final NewdataServiceStub asyncStub;
    private final NewdataServiceFutureStub futureStub;

    public NewdataClient(NewdataServiceBlockingStub blockingStub, NewdataServiceStub asyncStub, NewdataServiceFutureStub futureStub) {
        this.blockingStub = blockingStub;
        this.asyncStub = asyncStub;
        this.futureStub = futureStub;
    }

    public void sendBlockingUnaryMessage(EventRequest eventRequest) {
        logger.info("request=" + eventRequest);
        EventResponse eventResponse = blockingStub.unaryEvent(eventRequest);
        logger.info("response=" + eventResponse);
    }

    public void sendAsyncUnaryMessage(EventRequest eventRequest) {
        logger.info("request=" + eventRequest);
        // 비동기로 응답을 받기 위해 서버로 보내는 callback 객체(StreamObserver)도 같이 보낸다.
        asyncStub.unaryEvent(eventRequest, new StreamObserver<EventResponse>() {
            @Override
            public void onNext(EventResponse value) {
                logger.info("response=" + value.getResult());
            }

            @Override
            public void onError(Throwable t) {
                logger.info("onError 호출");
            }

            @Override
            public void onCompleted() {
                logger.info("서버로부터 응답 끝");
            }
        });
        logger.info("nonblocking + async라 rpc한 후 바로 로그 찍힘");
    }

    public void sendFutureUnaryMessage(EventRequest eventRequest) {
        logger.info("request=" + eventRequest);
        EventResponse eventResponse = null;
        ListenableFuture<EventResponse> future = futureStub.unaryEvent(eventRequest);
        logger.info("future니까 nonblocking?");
        try {
            eventResponse = future.get(2, TimeUnit.SECONDS);
        }
        catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        logger.info("response=" + eventResponse);
    }

    public void sendBlockingServerStreamingMessage(EventRequest eventRequest) {
        logger.info("request=" + eventRequest);
        Iterator<EventResponse> responseIter;
        // streaming이 완료될 때까지 대기한다.
        responseIter = blockingStub.serverStreamingEvent(eventRequest);
        responseIter.forEachRemaining((response) -> {
            logger.info("response=" + response);
        });
    }

    public void sendAsyncServerStreamingMessage(EventRequest eventRequest) {
        logger.info("request=" + eventRequest);
        asyncStub.serverStreamingEvent(eventRequest, new StreamObserver<EventResponse>() {
            @Override
            public void onNext(EventResponse value) {
                logger.info("async repsonse=" + value.getResult());
            }

            @Override
            public void onError(Throwable t) {
                logger.info("onError호출");
            }

            @Override
            public void onCompleted() {
                logger.info("Async Server Streaming response 끝");
            }
        });
        logger.info("서버 응답과 상관없이 다른 작업중...");
    }

    // client streaming은 AsyncStub만 가능.
    public void sendAsyncClientStreamingMessage(List<EventRequest> eventRequests) {
        // 비동기로 응답받을 객체 생성
        StreamObserver<EventResponse> responseObserver = new StreamObserver<EventResponse>() {
            @Override
            public void onNext(EventResponse value) {
                logger.info("Async client Streaming response=" + value.getResult());
            }

            @Override
            public void onError(Throwable t) {
                logger.info("onError호출");
            }

            @Override
            public void onCompleted() {
                logger.info("서버 응답 끝");
            }
        };
        // 비동기로 요청을 보낼 객체 생성
        // 응답 받을 객체와 연결
        StreamObserver<EventRequest> requestObserver = asyncStub.clientStreamingEvent(responseObserver);
        // 원하는 만큼 데이터 보내기
        for (EventRequest eventRequest : eventRequests) {
            requestObserver.onNext(eventRequest);
        }
        // 데이터 보내기를 완료했다는 시그널 보내기
        requestObserver.onCompleted();
    }

    // bidiection Streaming
    public void sendBiDirectionalStreamingMessage(List<EventRequest> eventRequests) {
        // 응답 받을 객체 생성
        StreamObserver<EventResponse> responseObserver = new StreamObserver<EventResponse>() {
            @Override
            public void onNext(EventResponse value) {
                logger.info("BidirectionResponse=" + value.getResult());
            }

            @Override
            public void onError(Throwable t) {
                logger.info("onError호출");
            }

            @Override
            public void onCompleted() {
                logger.info("Bidirection Streaming response 끝");
            }
        };
        // 요청 보낼 객체 생성 및 응답객체와 연결
        StreamObserver<EventRequest> requestObserver = asyncStub.biStreamingEvent(responseObserver);
        for (EventRequest eventRequest : eventRequests) {
            requestObserver.onNext(eventRequest);
        }
        logger.info("async니까 바로 로그 찍힘");
        requestObserver.onCompleted();
    }
}