package com.github.armedis.redis.command.management.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandStatsVO {
    private final Map<String, CommandStat> stats = new HashMap<>();

    public Map<String, CommandStat> getStats() {
        return stats;
    }

    public List<CommandStat> getStatsList() {
        return List.copyOf(stats.values());
    }

    public long getTotalCalls() {
        return stats.values().stream().mapToLong(CommandStat::getCalls).sum();
    }

    public void addStat(CommandStat stat) {
        stats.merge(stat.getCommandName(), stat, CommandStat::merge);
    }

    public static class CommandStat {
        private final String commandName;
        private final long calls;
        private final long usec;
        private final long rejectedCalls;
        private final long failedCalls;

        public CommandStat(String commandName, long calls, long usec, long rejectedCalls, long failedCalls) {
            this.commandName = commandName;
            this.calls = calls;
            this.usec = usec;
            this.rejectedCalls = rejectedCalls;
            this.failedCalls = failedCalls;
        }

        public String getCommandName() {
            return commandName;
        }

        public long getCalls() {
            return calls;
        }

        public long getUsec() {
            return usec;
        }

        public long getRejectedCalls() {
            return rejectedCalls;
        }

        public long getFailedCalls() {
            return failedCalls;
        }

        public double getUsecPerCall() {
            return calls > 0 ? (double) usec / calls : 0.0;
        }

        public CommandStat merge(CommandStat other) {
            return new CommandStat(
                    this.commandName,
                    this.calls + other.calls,
                    this.usec + other.usec,
                    this.rejectedCalls + other.rejectedCalls,
                    this.failedCalls + other.failedCalls);
        }
    }
}