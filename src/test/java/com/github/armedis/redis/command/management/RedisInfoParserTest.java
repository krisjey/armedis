/**
 * 
 */
package com.github.armedis.redis.command.management;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.github.armedis.ArmedisServer;
import com.github.armedis.http.service.stats.CommandStatsAggregator;
import com.github.armedis.redis.command.management.vo.CommandStatsVO;
import com.github.armedis.redis.command.management.vo.CommandStatsVO.CommandStat;

/**
 * 
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ArmedisServer.class)
class RedisInfoParserTest {
    @Nested
    @DisplayName("RedisCommandStatsParser 테스트")
    class ParserTest {

        private RedisCommandStatsParser parser;

        @BeforeEach
        void setUp() {
            parser = new RedisCommandStatsParser();
        }

        @Test
        @DisplayName("정상적인 commandstats 파싱")
        void parseValidCommandStats() {
            String raw = """
                    # Commandstats
                    cmdstat_hello:calls=2096,usec=34412,usec_per_call=16.42,rejected_calls=0,failed_calls=0
                    cmdstat_config|get:calls=977,usec=13640,usec_per_call=13.96,rejected_calls=0,failed_calls=0
                    cmdstat_config|set:calls=23,usec=268,usec_per_call=11.65,rejected_calls=0,failed_calls=0
                    cmdstat_replconf:calls=92320,usec=177294,usec_per_call=1.92,rejected_calls=0,failed_calls=0
                    cmdstat_get:calls=10,usec=64,usec_per_call=6.40,rejected_calls=1,failed_calls=2
                    cmdstat_command|docs:calls=1,usec=10655,usec_per_call=10655.00,rejected_calls=0,failed_calls=0
                    cmdstat_hexists:calls=8,usec=73,usec_per_call=9.12,rejected_calls=0,failed_calls=0
                    cmdstat_cluster|nodes:calls=2024,usec=214755,usec_per_call=106.10,rejected_calls=0,failed_calls=0
                    cmdstat_cluster|myid:calls=38,usec=66,usec_per_call=1.74,rejected_calls=0,failed_calls=0
                    cmdstat_hgetall:calls=4,usec=53,usec_per_call=13.25,rejected_calls=0,failed_calls=0
                    cmdstat_hlen:calls=8,usec=56,usec_per_call=7.00,rejected_calls=0,failed_calls=0
                    cmdstat_psync:calls=1,usec=128,usec_per_call=128.00,rejected_calls=0,failed_calls=0
                    cmdstat_hexpire:calls=8,usec=320,usec_per_call=40.00,rejected_calls=0,failed_calls=0
                    cmdstat_del:calls=23,usec=706,usec_per_call=30.70,rejected_calls=0,failed_calls=0
                    cmdstat_set:calls=14,usec=322,usec_per_call=23.00,rejected_calls=0,failed_calls=0
                    cmdstat_info:calls=62998,usec=4038115,usec_per_call=64.10,rejected_calls=0,failed_calls=0
                    cmdstat_hset:calls=86,usec=1847,usec_per_call=21.48,rejected_calls=0,failed_calls=0
                    cmdstat_client|list:calls=595,usec=33549,usec_per_call=56.38,rejected_calls=0,failed_calls=0
                    cmdstat_client|setname:calls=2015,usec=6080,usec_per_call=3.02,rejected_calls=0,failed_calls=0
                    cmdstat_client|setinfo:calls=4192,usec=4763,usec_per_call=1.14,rejected_calls=0,failed_calls=0
                    cmdstat_ping:calls=13,usec=36,usec_per_call=2.77,rejected_calls=0,failed_calls=0
                    cmdstat_exists:calls=2,usec=10,usec_per_call=5.00,rejected_calls=0,failed_calls=0
                    cmdstat_hget:calls=6,usec=61,usec_per_call=10.17,rejected_calls=0,failed_calls=0
                    cmdstat_httl:calls=4,usec=44,usec_per_call=11.00,rejected_calls=0,failed_calls=0
                    cmdstat_hdel:calls=4,usec=251,usec_per_call=62.75,rejected_calls=0,failed_calls=0
                                        """;

            CommandStatsVO vo = parser.parseCommandStats(raw);

            assertThat(vo.getStats()).hasSize(25);

            CommandStat getStat = vo.getStats().get("get");
            assertThat(getStat).isNotNull();
            assertThat(getStat.getCommandName()).isEqualTo("get");
            assertThat(getStat.getCalls()).isEqualTo(10);
            assertThat(getStat.getUsec()).isEqualTo(64);
            assertThat(getStat.getRejectedCalls()).isEqualTo(1);
            assertThat(getStat.getFailedCalls()).isEqualTo(2);
            assertThat(getStat.getUsecPerCall()).isCloseTo(6.4, within(0.1));
        }

        @Test
        @DisplayName("파이프 포함 명령어 파싱 (config|get)")
        void parsePipeCommand() {
            String raw = "cmdstat_config|get:calls=10,usec=100,usec_per_call=10.00,rejected_calls=0,failed_calls=0";

            CommandStatsVO vo = parser.parseCommandStats(raw);

            assertThat(vo.getStats()).containsKey("config|get");
            assertThat(vo.getStats().get("config|get").getCalls()).isEqualTo(10);
        }

        @Test
        @DisplayName("빈 문자열 파싱")
        void parseEmptyString() {
            CommandStatsVO vo = parser.parseCommandStats("");
            assertThat(vo.getStats()).isEmpty();
        }

        @Test
        @DisplayName("null 파싱")
        void parseNull() {
            CommandStatsVO vo = parser.parseCommandStats(null);
            assertThat(vo.getStats()).isEmpty();
        }

        @Test
        @DisplayName("잘못된 형식 무시")
        void parseInvalidFormat() {
            String raw = """
                    invalid_line
                    cmdstat_get:calls=10,usec=50,usec_per_call=5.00,rejected_calls=0,failed_calls=0
                    another_invalid
                    """;

            CommandStatsVO vo = parser.parseCommandStats(raw);

            assertThat(vo.getStats()).hasSize(1).containsKey("get");
        }
    }

    @Nested
    @DisplayName("CommandStatsVO 테스트")
    class VOTest {

        @Test
        @DisplayName("CommandStat merge 테스트")
        void mergeCommandStat() {
            CommandStat stat1 = new CommandStat("get", 100, 500, 1, 2);
            CommandStat stat2 = new CommandStat("get", 200, 1000, 3, 4);

            CommandStat merged = stat1.merge(stat2);

            assertThat(merged.getCommandName()).isEqualTo("get");
            assertThat(merged.getCalls()).isEqualTo(300);
            assertThat(merged.getUsec()).isEqualTo(1500);
            assertThat(merged.getRejectedCalls()).isEqualTo(4);
            assertThat(merged.getFailedCalls()).isEqualTo(6);
        }

        @Test
        @DisplayName("usecPerCall 재계산 검증")
        void usecPerCallCalculation() {
            CommandStat stat1 = new CommandStat("get", 100, 200, 0, 0); // 2.0 per call
            CommandStat stat2 = new CommandStat("get", 100, 600, 0, 0); // 6.0 per call

            CommandStat merged = stat1.merge(stat2);

            // 합산 후 재계산: 800 / 200 = 4.0
            assertThat(merged.getUsecPerCall()).isCloseTo(4.0, within(0.01));
        }

        @Test
        @DisplayName("calls가 0일 때 usecPerCall은 0")
        void usecPerCallWithZeroCalls() {
            CommandStat stat = new CommandStat("get", 0, 0, 0, 0);
            assertThat(stat.getUsecPerCall()).isCloseTo(0.0, within(0.01));
        }

        @Test
        @DisplayName("addStat 중복 명령어 자동 병합")
        void addStatMergesDuplicates() {
            CommandStatsVO vo = new CommandStatsVO();
            vo.addStat(new CommandStat("get", 100, 500, 0, 0));
            vo.addStat(new CommandStat("get", 50, 250, 1, 1));

            CommandStat stat = vo.getStats().get("get");
            assertThat(stat.getCalls()).isEqualTo(150);
            assertThat(stat.getUsec()).isEqualTo(750);
        }

        @Test
        @DisplayName("totalCalls 계산")
        void totalCallsCalculation() {
            CommandStatsVO vo = new CommandStatsVO();
            vo.addStat(new CommandStat("get", 100, 500, 0, 0));
            vo.addStat(new CommandStat("set", 50, 250, 0, 0));
            vo.addStat(new CommandStat("del", 30, 150, 0, 0));

            assertThat(vo.getTotalCalls()).isEqualTo(180);
        }

        @Test
        @DisplayName("getStatsList 반환 검증")
        void getStatsListReturnsAllStats() {
            CommandStatsVO vo = new CommandStatsVO();
            vo.addStat(new CommandStat("get", 100, 500, 0, 0));
            vo.addStat(new CommandStat("set", 50, 250, 0, 0));

            List<CommandStat> list = vo.getStatsList();

            assertThat(list).hasSize(2);
            assertThat(list).extracting(CommandStat::getCommandName).containsExactlyInAnyOrder("get", "set");
        }
    }

    @Nested
    @DisplayName("CommandStatsAggregator 테스트")
    class AggregatorTest {

        private CommandStatsAggregator aggregator;

        @BeforeEach
        void setUp() {
            aggregator = new CommandStatsAggregator();
        }

        @Test
        @DisplayName("여러 VO 합산")
        void aggregateMultipleVOs() {
            CommandStatsVO vo1 = new CommandStatsVO();
            vo1.addStat(new CommandStat("get", 100, 500, 0, 0));
            vo1.addStat(new CommandStat("set", 50, 300, 0, 0));

            CommandStatsVO vo2 = new CommandStatsVO();
            vo2.addStat(new CommandStat("get", 200, 1000, 1, 1));
            vo2.addStat(new CommandStat("del", 30, 150, 0, 0));

            CommandStatsVO result = aggregator.aggregate(Arrays.asList(vo1, vo2));

            assertThat(result.getStats()).hasSize(3);
            assertThat(result.getStats().get("get").getCalls()).isEqualTo(300);
            assertThat(result.getStats().get("get").getUsec()).isEqualTo(1500);
            assertThat(result.getStats().get("set").getCalls()).isEqualTo(50);
            assertThat(result.getStats().get("del").getCalls()).isEqualTo(30);
            assertThat(result.getTotalCalls()).isEqualTo(380);
        }

        @Test
        @DisplayName("빈 리스트 합산")
        void aggregateEmptyList() {
            CommandStatsVO result = aggregator.aggregate(Collections.emptyList());
            assertThat(result.getStats()).isEmpty();
            assertThat(result.getTotalCalls()).isZero();
        }

        @Test
        @DisplayName("null 리스트 합산")
        void aggregateNullList() {
            CommandStatsVO result = aggregator.aggregate(null);
            assertThat(result.getStats()).isEmpty();
        }

        @Test
        @DisplayName("단일 VO 합산")
        void aggregateSingleVO() {
            CommandStatsVO vo = new CommandStatsVO();
            vo.addStat(new CommandStat("get", 100, 500, 0, 0));

            CommandStatsVO result = aggregator.aggregate(List.of(vo));

            assertThat(result.getStats()).hasSize(1);
            assertThat(result.getStats().get("get").getCalls()).isEqualTo(100);
        }
    }

    @Nested
    @DisplayName("통합 테스트")
    class IntegrationTest {

        @Test
        @DisplayName("파싱 → 합산 전체 흐름")
        void fullFlow() {
            RedisCommandStatsParser parser = new RedisCommandStatsParser();
            CommandStatsAggregator aggregator = new CommandStatsAggregator();

            String server1 = """
                    cmdstat_get:calls=100,usec=500,usec_per_call=5.00,rejected_calls=0,failed_calls=0
                    cmdstat_set:calls=50,usec=300,usec_per_call=6.00,rejected_calls=0,failed_calls=0
                    """;

            String server2 = """
                    cmdstat_get:calls=150,usec=900,usec_per_call=6.00,rejected_calls=1,failed_calls=0
                    cmdstat_info:calls=10,usec=100,usec_per_call=10.00,rejected_calls=0,failed_calls=0
                    """;

            List<CommandStatsVO> statsList = Arrays.asList(
                    parser.parseCommandStats(server1),
                    parser.parseCommandStats(server2));

            CommandStatsVO result = aggregator.aggregate(statsList);

            assertThat(result.getStats()).hasSize(3);
            assertThat(result.getStats().get("get").getCalls()).isEqualTo(250);
            assertThat(result.getStats().get("get").getUsec()).isEqualTo(1400);
            assertThat(result.getStats().get("get").getUsecPerCall()).isCloseTo(5.6, within(0.01));
            assertThat(result.getTotalCalls()).isEqualTo(310);
        }
    }
}
