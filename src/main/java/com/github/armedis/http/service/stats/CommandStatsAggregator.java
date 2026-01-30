package com.github.armedis.http.service.stats;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.github.armedis.redis.command.management.vo.CommandStatsVO;

@Component
public class CommandStatsAggregator {
    private static final Set<String> EXCLUDED_COMMANDS = Set.of("replconf", "info", "ping", "auth", "psync", "sync", "cluster|nodes", "cluster|myid", "client|setname", "client|setinfo", "client|list");

    public static CommandStatsVO aggregate(List<CommandStatsVO> statsList) {
        CommandStatsVO result = new CommandStatsVO();

        if (statsList == null || statsList.isEmpty()) {
            return result;
        }

        statsList.stream()
                .flatMap(vo -> vo.getStats().values().stream())
                .filter(stat -> !EXCLUDED_COMMANDS.contains(stat.getCommandName()))
                .forEach(result::addStat);

        return result;
    }
}
