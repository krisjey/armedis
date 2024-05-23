# Current implemented command of Redis STRING

## RedisGetService Request 
    public HttpResponse urlencodedWithoutKey(RedisGetRequest redisRequest) {
       ...
    }
    
    public HttpResponse jsonWithoutKey(AggregatedHttpRequest httpRequest) {
        JsonNode jsonBody = getAsJsonBody(httpRequest);

        RedisRequest redisRequest = buildRedisRequest(REDIS_COMMAND, httpRequest, jsonBody);
        ...
    }

## GET
http get request 
/v1/get

/v1/get/:key

## SET
http post/put request