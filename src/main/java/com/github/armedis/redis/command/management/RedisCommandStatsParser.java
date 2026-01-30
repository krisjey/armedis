package com.github.armedis.redis.command.management;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.github.armedis.redis.command.management.vo.CommandStatsVO;
import com.github.armedis.redis.command.management.vo.CommandStatsVO.CommandStat;

@Component
public class RedisCommandStatsParser {

    private static final Pattern CMDSTAT_PATTERN = Pattern.compile(
            "cmdstat_([^:]+):calls=(\\d+),usec=(\\d+),usec_per_call=[\\d.]+,rejected_calls=(\\d+),failed_calls=(\\d+)");

    public CommandStatsVO parseCommandStats(String raw) {
        CommandStatsVO vo = new CommandStatsVO();
        if (raw == null || raw.isBlank()) {
            return vo;
        }

        raw.lines()
                .map(CMDSTAT_PATTERN::matcher)
                .filter(Matcher::find)
                .forEach(m -> vo.addStat(
                        new CommandStat(
                                m.group(1),
                                Long.parseLong(m.group(2)),
                                Long.parseLong(m.group(3)),
                                Long.parseLong(m.group(4)),
                                Long.parseLong(m.group(5)))));

        return vo;
    }
}