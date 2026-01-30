package com.github.armedis.http.service.stats;

import java.util.List;

import org.springframework.stereotype.Component;

import com.github.armedis.redis.command.management.vo.CommandStatsVO;

@Component
public class CommandStatsAggregator {

    public static CommandStatsVO aggregate(List<CommandStatsVO> statsList) {
        CommandStatsVO result = new CommandStatsVO();

        if (statsList == null || statsList.isEmpty()) {
            return result;
        }

        statsList.stream()
                .flatMap(vo -> vo.getStats().values().stream())
                .forEach(result::addStat);

        return result;
    }
}
