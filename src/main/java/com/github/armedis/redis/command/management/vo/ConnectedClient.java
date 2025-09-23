package com.github.armedis.redis.command.management.vo;

/**
     * Redis 클라이언트 연결 정보를 담는 Record
     */
public record ConnectedClient(
        Long id, // 클라이언트 고유 ID
        String addr, // 클라이언트 주소 (IP:PORT)
        Integer fd, // 파일 디스크립터
        String name, // 클라이언트 이름 (빈 문자열일 수 있음)
        Long age, // 연결 지속 시간 (초)
        Long idle, // 마지막 명령 이후 경과 시간 (초)
        String flags, // 클라이언트 플래그
        Integer db, // 현재 데이터베이스 번호
        Integer sub, // 구독 중인 채널 수
        Integer psub, // 패턴 구독 수
        Integer multi, // 트랜잭션 상태
        Long qbuf, // 쿼리 버퍼 길이
        Long qbufFree, // 쿼리 버퍼 여유 공간
        Long obl, // 출력 버퍼 길이
        Long oll, // 출력 리스트 길이
        Long omem, // 출력 메모리 사용량
        String events, // 파일 이벤트 플래그
        String cmd // 마지막 실행 명령
) {

    /**
     * 클라이언트가 활성 상태인지 확인
     */
    public boolean isActive() {
        return idle < 30; // 30초 이내에 활동한 클라이언트를 활성으로 간주
    }

    /**
     * 클라이언트가 구독자인지 확인
     */
    public boolean isSubscriber() {
        return sub > 0 || psub > 0;
    }

    /**
     * IP 주소만 추출
     */
    public String getIpAddress() {
        if (addr == null || !addr.contains(":")) {
            return addr;
        }
        return addr.substring(0, addr.lastIndexOf(":"));
    }
}