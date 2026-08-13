package com.github.armedis.ui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

class StaticUiRegressionTest {

    @Test
    void routerCommitsThePageOnlyAfterItsRequestSucceeds() throws IOException {
        String ajax = resource("static/assets/js/ajax.js");
        int doneHandler = ajax.indexOf("request.done(function (data)");
        int cleanup = ajax.indexOf("cleanupCurrentPage()", doneHandler);
        int contentSwap = ajax.indexOf("$(\"#ajaxresult\").empty().html(data)", doneHandler);

        assertTrue(doneHandler >= 0);
        assertTrue(cleanup > doneHandler);
        assertTrue(contentSwap > cleanup);
        assertFalse(ajax.substring(0, doneHandler).contains("cleanupCurrentPage();"));
        assertTrue(ajax.contains("requestedPage = previousPage"));
        assertTrue(ajax.contains("window.history.replaceState({ page: previousPage }"));
        assertTrue(ajax.contains("currentAjaxRequest === request"));
        assertTrue(ajax.contains("window.pageHooks[activePage]"));
        assertTrue(ajax.contains("page-load-error"));
    }

    @Test
    void routerSupportsBrowserHistory() throws IOException {
        String ajax = resource("static/assets/js/ajax.js");

        assertTrue(ajax.contains("window.addEventListener(\"popstate\", routeFromLocation)"));
        assertTrue(ajax.contains("window.addEventListener(\"hashchange\", routeFromLocation)"));
        assertTrue(ajax.contains("window.history.pushState"));
        assertTrue(ajax.contains("updateDocumentTitle(normalizedPage)"));
        assertTrue(ajax.contains("updateActiveMenu(normalizedPage)"));
    }

    @Test
    void managementAcceptsOnlyIntegerText() throws IOException {
        String management = resource("static/ajax/pages-armedis-management.html");

        assertTrue(management.contains("type=\"number\" step=\"1\""));
        assertTrue(management.contains("val = val.trim()"));
        assertTrue(management.contains("if (!/^-?\\d+$/.test(val))"));
        assertFalse(management.contains("if (isNaN(val))"));
    }

    @Test
    void managementCategoryTabsDoNotNavigate() throws IOException {
        String management = resource("static/ajax/pages-armedis-management.html");

        assertTrue(management.contains("<button type=\"button\" class=\"nav-link active\" data-cat=\"All\">"));
        assertTrue(management.contains("<button type=\"button\" class=\"nav-link\" data-cat=\"${category}\">"));
        assertTrue(management.contains("var tab = e.target.closest(\".nav-link\")"));
        assertTrue(management.contains("if(tab && ul.contains(tab))"));
        assertTrue(management.contains("renderTable(tab.dataset.cat"));
        assertFalse(management.contains("href=\"#\""));
    }

    @Test
    void managementPreservesRedisMemoryUnitSemantics() throws IOException {
        String management = resource("static/ajax/pages-armedis-management.html");

        assertTrue(management.contains("{ suffix: \"gb\", bytes: 1073741824n }"));
        assertTrue(management.contains("{ suffix: \"g\", bytes: 1000000000n }"));
        assertTrue(management.contains("{ suffix: \"mb\", bytes: 1048576n }"));
        assertTrue(management.contains("{ suffix: \"m\", bytes: 1000000n }"));
        assertTrue(management.contains("{ suffix: \"kb\", bytes: 1024n }"));
        assertTrue(management.contains("{ suffix: \"k\", bytes: 1000n }"));
        assertTrue(management.contains("bytes % units[i].bytes === 0n"));
        assertTrue(management.contains("var memoryPattern = /^\\d+(k|kb|m|mb|g|gb)?$/i"));
        assertTrue(management.contains("refreshCurrentConfigValue(currentEdit)"));
        assertFalse(management.contains("getMemoryCapacityUnit(i.currentValue, \"kbyte\")"));
        assertFalse(management.contains("getMemoryCapacityUnit(currentVal, \"kbyte\")"));
        assertFalse(management.contains("getMemoryCapacityUnit(curr, \"kbyte\")"));
        assertFalse(management.contains(".textContent = val"));
    }

    @Test
    void overviewPausesPollingWhileHiddenAndCleansUpResources() throws IOException {
        String overview = resource("static/ajax/pages-armedis-overview.html");

        assertFalse(overview.contains("setInterval(() =>"));
        assertTrue(overview.contains("document.hidden || overviewPollInFlight"));
        assertTrue(overview.contains("setTimeout(intervalInit, 5000)"));
        assertTrue(overview.contains("overviewAbortControllers.forEach(controller => controller.abort())"));
        assertTrue(overview.contains("addEventListener(\"visibilitychange\", handleOverviewVisibilityChange)"));
        assertTrue(overview.contains("removeEventListener(\"visibilitychange\", handleOverviewVisibilityChange)"));
    }

    @Test
    void realtimePausesPollingWhileHiddenAndCleansUpResources() throws IOException {
        String realtime = resource("static/ajax/pages-armedis-realtime-stats.html");

        assertFalse(realtime.contains("setInterval(intervalInit"));
        assertTrue(realtime.contains("document.hidden || realtimePollInFlight"));
        assertTrue(realtime.contains("setTimeout(intervalInit, 1000)"));
        assertTrue(realtime.contains("realtimeStatsController.abort()"));
        assertTrue(realtime.contains("addEventListener(\"visibilitychange\", handleRealtimeVisibilityChange)"));
        assertTrue(realtime.contains("removeEventListener(\"visibilitychange\", handleRealtimeVisibilityChange)"));
        assertFalse(realtime.contains("document.addEventListener(\"visibilitychange\", function()"));
    }

    private static String resource(String path) throws IOException {
        try (InputStream stream = StaticUiRegressionTest.class.getClassLoader().getResourceAsStream(path)) {
            if (stream == null) {
                throw new IOException("Resource not found: " + path);
            }
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
