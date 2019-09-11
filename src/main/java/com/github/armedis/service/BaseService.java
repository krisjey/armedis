package com.github.armedis.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.text.StringEscapeUtils;
import com.github.armedis.config.ConstantNames;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.linecorp.armeria.common.HttpResponse;
import com.linecorp.armeria.common.HttpStatus;
import com.linecorp.armeria.common.MediaType;

public class BaseService implements ArmeriaAnnotatedHttpService {
    private static final DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Client API 호출은 PHP 구조상 Get과 Post만 있음.<br/>
     * EndPoint of API URL Constants.
     * @author krisjey
     *
     */
    protected final class ServiceUrl {
        /**
         * 알림 Badge 조회 API
         */
        public static final String BADGE_GET = "/pual/v1/badge/{userId}";

        /**
         * 알림 목록조회 API
         */
        public static final String NOTIFICATIONS_GET = "/pual/v1/notifications/{userId}";

        /**
         * 방송국 게시글 생성 알림 
         */
        public static final String BBS_WRITE_EVENT_POST = "/pual/v1/bbsNotification/{userId}";

        /**
         * 방송 알림 예약 API
         */
        public static final String RESERVE_BROADCAST_NOTIFICATION_POST = "/pual/v1/reserveBroadcastNotification/{userId}";

        /**
         * 알림/Push 설정 API
         */
        public static final String SETTINGS_POST = "/pual/v1/settings/{userId}";

        /**
         * 구독 만료 처리 API
         */
        public static final String SUBSCRIBE_POST = "/pual/v1/subscribe/{userId}";

        /**
         * 즐겨찾기 추가/삭제 API
         */
        public static final String FAVORITES_POST = "/pual/v1/favorites/{userId}";

        /**
         * 즐겨찾기 알림 toggle
         */
        public static final String FAVORITES_PUSH_TOGGLE_POST = "/pual/v1/favoritesPushToggle/{userId}";

        /**
         * 즐겨찾기 사용자 보정 API
         */
        public static final String ADMIN_FAVORITES_CORRECTION_POST = "/pual/v1/favoritesCorrection/{userId}";

        /**
         * 방송 시작 알림
         */
        public static final String BROADCAST_NOTIFICATION_POST = "/pual/v1/broadcastNotification/{userId}";

        /**
         * 알림 이력 조회
         */
        public static final String ADMIN_NOTIFICATION_HISTORY_GET = "/pual/v1/notificationHistory";

        /**
         * 알림 읽음처리(delete/read/deleteAll/readAll)
         */
        public static final String NOTIFICATION_POST = "/pual/v1/notification/{userId}";

        /**
         * BJ 메시지 생성 알림
         */
        public static final String BJ_MESSAGE_POST = "/pual/v1/bjMessageNotification/{userId}";

        /**
         * BJ 메시지 알림 조회
         */
        public static final String BJ_MESSAGE_GET = "/pual/v1/bjMessageNotification/{userId}";

        /**
         * 방명록 글 작성 알림
         */
        public static final String GUESTBOOK_NOTIFICATION_POST = "/pual/v1/guestbookNotification/{userId}";

        /**
         * 알림 화면 출력 상태 변경 On/Off
         */
        public static final String NOTIFICATION_DISPLAY_TOGGLE_POST = "/pual/v1/notificationDisplayToggle/{userId}";

        /**
         * 알림 화면 출력 상태 변경 On/Off
         */
        public static final String USER_GIFT_POST = "/pual/v1/userGift/{userId}";

        /**
         * 쪽지(Note) 수신 알림
         */
        public static final String NOTE_RECEIVE_POST = "/pual/v1/userNote/{userId}";

        /**
         * 내 VOD를 방송한 BJ가 삭제
         */
        public static final String VOD_BLIND_POST = "/pual/v1/vodBlindNotification/{stationOwner}";

        /**
         * 매니저 추가 삭제
         */
        public static final String MANAGER_POST = "/pual/v1/manager/{stationOwner}";

        /**
         * dummy api endpoint
         */
        public static final String HELLO_WORLD_GET = "/hello/world/{userId}";

        public static final String RUOK_GET = "/pual/v1/ruok";

        public static final String FREE_MEMORY_GET = "/pual/v1/free";
    }

    private static final Gson GSON_CONVERTER = new Gson();

    private static final String DATA_FIELD_NAME = "DATA";

    protected HttpResponse buildResponse(Map<String, ?> resultData) {
        return buildResponse((JsonObject) GSON_CONVERTER.toJsonTree(resultData));
    }

    protected HttpResponse buildResponse(ResponseCode responseCode) {
        return buildResponse(responseCode, null);
    }

    /**
     * // FIXME 디버그 응답 만들 때 ServiceRequestObject가 필요함.
     * // 디버그 플래그를 request header에서 받을까?
     * Wrapper method of {@link #buildResponse(ResponseCode code, String resultData)} using {@link ResponseCode#SUCCESS} parameter.
     * @param resultData
     * @return
     */
    protected HttpResponse buildResponse(JsonObject resultData) {
        return buildResponse(ResponseCode.SUCCESS, resultData);
    }

    /**
     * Final message builder<br/>
     * Build {@link HttpResponse} object using {@code resultData} parameter.<br/>
     * Actual response writer
     * @param code
     * @param resultData
     * @return Object of {@link HttpResponse} 
     */
    protected final HttpResponse buildResponse(ResponseCode code, JsonObject resultData) {
        if (resultData == null) {
            resultData = new JsonObject();
        }

        resultData.addProperty(ConstantNames.RESULT_CODE, code.getResultCode());
        resultData.addProperty(ConstantNames.RESULT_MESSAGE, code.getMessage());

        return HttpResponse.of(HttpStatus.valueOf(code.getStatusCode()), MediaType.JSON_UTF_8, resultData.toString());
    }

    protected String getDataFieldName() {
        return DATA_FIELD_NAME;
    }

    protected void unescapeText(Map<String, Object> item, String key) {
        Object object = item.get(key);
        if (object == null) {
            // do nothing.
        }
        else {
            item.put(key, StringEscapeUtils.unescapeHtml4((String) object).replaceAll("&apos;", "'"));
        }
    }

    protected String unixTimestampToDateString(String unixtimestamp) {
        long timeStapm = NumberUtils.toLong(unixtimestamp);
        return LocalDateTime.ofEpochSecond(timeStapm, 0, ZoneOffset.ofHours(9)).format(pattern);
    }
}
