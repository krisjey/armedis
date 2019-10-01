package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.proto.EventRequest;
import com.example.demo.proto.EventResponse;
import com.example.demo.proto.NewdataServiceGrpc.NewdataServiceImplBase;

import io.grpc.stub.StreamObserver;

@Service
public class NewdataServiceImpl extends NewdataServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(NewdataServiceImpl.class);

    @Override
    public void unaryEvent(EventRequest request, StreamObserver<EventResponse> responseObserver) {
        logger.info("input= sourceId : " + request.getSourceId() + ", eventId : " + request.getEventId());

        EventResponse eventResponse = EventResponse.newBuilder()
                .setResult(request.getSourceId() + request.getEventId()).build();
        // unary라 onNext 1회만 호출 가능
        // 데이터 전송
        responseObserver.onNext(eventResponse);
        logger.info("onNext");
        // 1초후 응답 스트림에 대한 완료 보낼 것임
//        try {
//            Thread.sleep(1000);
//        }
//        catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        responseObserver.onCompleted();
        logger.info("onCompleted unaryEvent");
    }

    @Override
    public void serverStreamingEvent(EventRequest request, StreamObserver<EventResponse> responseObserver) {
        logger.info("input= sourceId : " + request.getSourceId() + ", eventId : " + request.getEventId());
        EventResponse eventResponse = EventResponse.newBuilder()
                .setResult(request.getSourceId() + request.getEventId())
                .build();
        // 클라이언트에 의해 데이터는 한 번 들어오지만 serverStreaming이므로 여러번 onNext 호출 가능
        responseObserver.onNext(eventResponse);
        responseObserver.onNext(eventResponse);
        responseObserver.onNext(eventResponse);
        responseObserver.onNext(eventResponse);
        // 1초후 완료 응답
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        responseObserver.onCompleted();
        logger.info("onCompleted serverStreamingEvent");
    }

    @Override
    public StreamObserver<EventRequest> clientStreamingEvent(StreamObserver<EventResponse> responseObserver) {
        return new StreamObserver<EventRequest>() {
            @Override
            public void onNext(EventRequest value) {
                // 클라이언트로부터 여러번의 onNext가 호출될 예정이다.
                logger.info("메세지 : " + value.getSourceId() + "/" + value.getEventId());
            }

            @Override
            public void onError(Throwable t) {
                logger.info("onError호출");
            }

            @Override
            public void onCompleted() {
                // 클라이언트로부터 스트림 완료 응답이 오면 1번만 next를 보낼 수 있다.
                // 그래서 위의 onNext에서 같이 onNext하면 안된다.(여러번 응답해버리는 biStream형식이 되어버리기 때문.)
                responseObserver.onNext(EventResponse.newBuilder().setResult("response").build());
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<EventRequest> biStreamingEvent(StreamObserver<EventResponse> responseObserver) {
        return new StreamObserver<EventRequest>() {
            @Override
            public void onNext(EventRequest value) {
                // 클라이언트로부터 데이터가 올 때마다 onNext가 호출된다.
                // 1개의 데이터가 올 때마다 3개의 응답을 던져줄 예정이다.
                logger.info("Bidirection" + value.getSourceId() + "/" + value.getEventId());
                responseObserver.onNext(EventResponse.newBuilder().setResult("응답1").build());
                responseObserver.onNext(EventResponse.newBuilder().setResult("응답2").build());
                responseObserver.onNext(EventResponse.newBuilder().setResult("응답3").build());
            }

            @Override
            public void onError(Throwable t) {
                logger.info("onError");
            }

            @Override
            public void onCompleted() {
                logger.info("onCompleted biStreamingEvent");
                responseObserver.onCompleted();
            }
        };
    }

}