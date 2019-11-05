
package com.example.demo.configuration;

import com.example.demo.proto.NewdataServiceGrpc;
import com.example.demo.proto.NewdataServiceGrpc.NewdataServiceBlockingStub;
import com.example.demo.proto.NewdataServiceGrpc.NewdataServiceFutureStub;
import com.example.demo.proto.NewdataServiceGrpc.NewdataServiceStub;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class NewdataClientStubFactory {
    private final ManagedChannel channel;
    private final NewdataServiceBlockingStub blockingStub;
    private final NewdataServiceStub asyncStub;
    private final NewdataServiceFutureStub futureStub;

    public NewdataClientStubFactory(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.blockingStub = NewdataServiceGrpc.newBlockingStub(channel);
        this.asyncStub = NewdataServiceGrpc.newStub(channel);
        this.futureStub = NewdataServiceGrpc.newFutureStub(channel);
    }

    public NewdataServiceBlockingStub getBlockingStub() {
        return blockingStub;
    }

    public NewdataServiceStub getAsyncStub() {
        return asyncStub;
    }

    public NewdataServiceFutureStub getFutureStub() {
        return futureStub;
    }

}