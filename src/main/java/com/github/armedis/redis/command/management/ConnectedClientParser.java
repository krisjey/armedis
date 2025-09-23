package com.github.armedis.redis.command.management;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.github.armedis.redis.command.management.vo.ConnectedClient;

/**
 * Redis CLIENT LIST 응답을 파싱하기 위한 Record와 Parser
 */
public class ConnectedClientParser {

    // key=value 패턴을 매칭하는 정규식
    private static final Pattern KEY_VALUE_PATTERN = Pattern.compile("(\\w+)=([^\\s]*)");

    /**
     * Redis CLIENT LIST 응답 문자열을 ConnectedClient 리스트로 파싱
     * 
     * @param clientListResponse Redis CLIENT LIST 명령의 응답 문자열
     * @return ConnectedClient 객체의 리스트
     * @throws IllegalArgumentException 파싱 실패 시
     */
    public static List<ConnectedClient> parseClientList(String clientListResponse) {
        if (clientListResponse == null || clientListResponse.trim().isEmpty()) {
            return Collections.emptyList();
        }

        return clientListResponse.lines()
                .filter(line -> !line.trim().isEmpty())
                .map(ConnectedClientParser::parseSingleClient)
                .collect(Collectors.toList());
    }

    /**
     * 단일 클라이언트 정보 라인을 파싱
     */
    private static ConnectedClient parseSingleClient(String line) {
        Map<String, String> attributes = parseAttributes(line);

        try {
            return new ConnectedClient(
                    parseLong(attributes.get("id")),
                    attributes.get("addr"),
                    parseInt(attributes.get("fd")),
                    attributes.getOrDefault("name", ""),
                    parseLong(attributes.get("age")),
                    parseLong(attributes.get("idle")),
                    attributes.get("flags"),
                    parseInt(attributes.get("db")),
                    parseInt(attributes.get("sub")),
                    parseInt(attributes.get("psub")),
                    parseInt(attributes.get("multi")),
                    parseLong(attributes.get("qbuf")),
                    parseLong(attributes.get("qbuf-free")),
                    parseLong(attributes.get("obl")),
                    parseLong(attributes.get("oll")),
                    parseLong(attributes.get("omem")),
                    attributes.get("events"),
                    attributes.get("cmd"));
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Failed to parse client info: " + line, e);
        }
    }

    /**
     * 라인에서 key=value 쌍들을 추출하여 Map으로 반환
     */
    private static Map<String, String> parseAttributes(String line) {
        Map<String, String> attributes = new HashMap<>();
        Matcher matcher = KEY_VALUE_PATTERN.matcher(line);

        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);
            attributes.put(key, value.isEmpty() ? null : value);
        }

        return attributes;
    }

    /**
     * 안전한 Long 파싱
     */
    private static Long parseLong(String value) {
        return value == null || value.isEmpty() ? null : Long.parseLong(value);
    }

    /**
     * 안전한 Integer 파싱
     */
    private static Integer parseInt(String value) {
        return value == null || value.isEmpty() ? null : Integer.parseInt(value);
    }

    // 사용 예제
    public static void main(String[] args) {
        String clientListResponse = """
                id=3 addr=127.0.0.1:52555 fd=8 name= age=184 idle=5 flags=N db=0 sub=0 psub=0 multi=-1 qbuf=0 qbuf-free=26 obl=0 oll=0 omem=0 events=r cmd=client
                id=4 addr=127.0.0.1:52556 fd=9 name=myapp age=10 idle=0 flags=N db=1 sub=1 psub=0 multi=-1 qbuf=0 qbuf-free=32766 obl=0 oll=0 omem=0 events=r cmd=ping
                """;

        List<ConnectedClient> clients = parseClientList(clientListResponse);

        System.out.println("Connected Clients:");
        clients.forEach(client -> {
            System.out.printf("ID: %d, Address: %s, Name: '%s', Age: %ds, Active: %s%n",
                    client.id(),
                    client.addr(),
                    client.name(),
                    client.age(),
                    client.isActive());

            client.getIpAddress();
        });

        // 활성 클라이언트만 필터링
        List<ConnectedClient> activeClients = clients.stream()
                .filter(ConnectedClient::isActive)
                .collect(Collectors.toList());

        System.out.println("\nActive clients: " + activeClients.size());
    }
}