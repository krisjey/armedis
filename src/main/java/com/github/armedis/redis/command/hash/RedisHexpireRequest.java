
package com.github.armedis.redis.command.hash;

import java.util.List;
import java.util.Optional;

import com.github.armedis.http.service.request.RedisRequest;
import com.linecorp.armeria.common.AggregatedHttpRequest;
import com.linecorp.armeria.server.annotation.Param;

public class RedisHexpireRequest extends RedisRequest {
    @Param("field")
    private List<String> field;

    @Param("seconds")
    private Long seconds;

    @Param("option")
    private Optional<String> option;

    public RedisHexpireRequest(AggregatedHttpRequest httpRequest) {
        super(httpRequest);
        this.setCommand("Hexpire");
    }

    /**
     * @return the field
     */
    public List<String> getField() {
        return field;
    }

    /**
     * @param field the field to set
     */
    public void setField(List<String> field) {
        this.field = field;
    }

    /**
     * @return the seconds
     */
    public Long getSeconds() {
        return seconds;
    }

    /**
     * @param seconds the seconds to set
     */
    public void setSeconds(Long seconds) {
        this.seconds = seconds;
    }

    /**
     * @return the option
     */
    public Optional<String> getOption() {
        return option;
    }

    /**
     * @param option the option to set
     */
    public void setOption(Optional<String> option) {
        this.option = option;
    }
}
