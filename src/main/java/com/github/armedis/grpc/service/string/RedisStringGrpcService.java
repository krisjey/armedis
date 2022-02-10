
package com.github.armedis.grpc.service.string;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.github.armedis.grpc.service.string.RedisStringServiceGrpc.RedisStringServiceImplBase;
import com.github.armedis.http.service.ResponseCode;
import com.github.armedis.redis.command.RedisCommandExecuteResult;

import io.grpc.stub.StreamObserver;

/**
 * Redis get http request endpoint service.
 * @author krisjey
 *
 */
@Service
public class RedisStringGrpcService extends RedisStringServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(RedisStringGrpcService.class);

    @Override
    public void set(SetRequest setRequest, StreamObserver<SetResponse> responseObserver) {
    	logger.info("Unary message get " + setRequest.getKey());
        SetResponse response = SetResponse.newBuilder()
                .setCode("200")
                .setResult("hello world " + setRequest.getKey())
                .build();

        // Server Streaming이면 responseObserver.onNext()를 두 번 이상 호출할 수 있다.
        responseObserver.onNext(response);

        // 응답 완료
        responseObserver.onCompleted();
    }

    @Override
    public void get(GetRequest getRequest, StreamObserver<GetResponse> responseObserver) {

        // client 어떻게 생성하나?

        // 실행부..
        // converter 필요.
//        // execute redis command by http request params.
//        RedisCommandExecuteResult result = null;
//        try {
//            result = executeCommand(redisRequest);
//        }
//        catch (Exception e) {
//            logger.error("Can not execute redis command ", e);
//            return buildResponse(ResponseCode.UNKNOWN_ERROR, redisRequest);
//        }

//        Redisgrpc
        logger.info("Unary message get " + getRequest.getKey());
        GetResponse response = GetResponse.newBuilder()
                .setCode("200")
                .setResult("hello world " + getRequest.getKey())
                .build();

        // Server Streaming이면 responseObserver.onNext()를 두 번 이상 호출할 수 있다.
        responseObserver.onNext(response);

        // 응답 완료
        responseObserver.onCompleted();
    }

    @Override
    public void getSet(GetRequest getRequest, StreamObserver<GetResponse> responseObserver) {
//      Redisgrpc
        logger.info("Unary message getSet " + getRequest.getKey());
        GetResponse response = GetResponse.newBuilder()
                .setCode("200")
                .setResult("hello world " + getRequest.getKey())
                .build();

        // Server Streaming이면 responseObserver.onNext()를 두 번 이상 호출할 수 있다.
        responseObserver.onNext(response);

        // 응답 완료
        responseObserver.onCompleted();
    }
}
