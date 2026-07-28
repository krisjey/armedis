package com.github.armedis.ui;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

class StaticUiRegressionTest {

    @Test
    void routerSupportsBrowserHistoryAndUsesSinglePageLoader() throws IOException {
        String ajax = resource("static/assets/js/ajax.js");
        String app = resource("static/assets/js/app.js");

        assertTrue(ajax.contains("window.addEventListener(\"popstate\", routeFromLocation)"));
        assertTrue(ajax.contains("window.addEventListener(\"hashchange\", routeFromLocation)"));
        assertTrue(ajax.contains("window.history.pushState"));
        assertTrue(ajax.contains("window.history.replaceState({ page: page }, \"\", \"#\" + page)"));
        assertFalse(app.contains("function call_ajax_page(page)"));
    }

    @Test
    void managementRendersServerValuesWithoutHtmlTemplates() throws IOException {
        String management = resource("static/ajax/pages-armedis-management.html");

        assertFalse(management.contains("tr.innerHTML ="));
        assertFalse(management.contains("row.innerHTML ="));
        assertTrue(management.contains("currentValue.textContent"));
        assertTrue(management.contains("descriptionCell.textContent"));
        assertTrue(management.contains("input.value = curr"));
    }

    @Test
    void managementUpdatesDisplayedValueOnlyAfterSuccessfulSave() throws IOException {
        String management = resource("static/ajax/pages-armedis-management.html");
        int successfulResultCheck = management.indexOf("if (!isSuccessfulResult(saveResult))");
        int displayedValueUpdate = management.indexOf("currentValue.textContent = saveRequest.value");

        assertTrue(successfulResultCheck >= 0);
        assertTrue(displayedValueUpdate > successfulResultCheck);
        assertTrue(management.contains("loginButton.disabled = isBusy"));
    }

    @Test
    void managementWaitsForTheMatchingDetailResponseBeforeSaving() throws IOException {
        String management = resource("static/ajax/pages-armedis-management.html");
        int saveDisabled = management.indexOf("document.getElementById(\"saveBtn\").disabled = true");
        int detailFetch = management.indexOf("fetch(\"/v1/management/settings/config/\"");
        int matchingEditCheck = management.indexOf("currentEdit !== requestedEdit");
        int editReady = management.indexOf("currentEditReady = true", detailFetch);

        assertTrue(saveDisabled >= 0);
        assertTrue(detailFetch > saveDisabled);
        assertTrue(matchingEditCheck > detailFetch);
        assertTrue(editReady > matchingEditCheck);
        assertTrue(management.contains("var saveRequest = pendingSave"));
        assertTrue(management.contains("encodeURIComponent(saveRequest.key)"));
    }

    @Test
    void managementCleansUpRequestsAndModalsOnUnload() throws IOException {
        String management = resource("static/ajax/pages-armedis-management.html");

        assertTrue(management.contains("configListController.abort()"));
        assertTrue(management.contains("configDetailController.abort()"));
        assertTrue(management.contains("editModal.dispose()"));
        assertTrue(management.contains("loginModal.dispose()"));
        assertTrue(management.contains("\"pages-armedis-management.html\""));
        assertTrue(management.contains("onUnload: function ()"));
        assertFalse(management.contains("class=\"modal fade\""));
    }

    @Test
    void realtimeRendersOnlyNewTimestampsAndKeepsSixtyPoints() throws IOException {
        String realtime = resource("static/ajax/pages-armedis-realtime-stats.html");

        assertTrue(realtime.contains("lastRenderedPointX[cardId] === lastX"));
        assertTrue(realtime.contains("data: item.data.slice(-60)"));
        assertTrue(realtime.contains("lastRenderedPointX[cardId] = lastX"));
        assertFalse(realtime.contains(".appendData("));
    }

    @Test
    void overviewDestroysEveryDataTableOnUnload() throws IOException {
        String overview = resource("static/ajax/pages-armedis-overview.html");

        assertTrue(overview.contains("var overviewDatatables = ["));
        assertTrue(overview.contains("overviewDatatables.forEach(datatable =>"));
        assertTrue(overview.contains("datatable.destroy()"));
        assertTrue(overview.contains("overviewDatatables = []"));
    }

    @Test
    void pollingIsSerializedAndLifecycleResourcesAreRemoved() throws IOException {
        String overview = resource("static/ajax/pages-armedis-overview.html");
        String realtime = resource("static/ajax/pages-armedis-realtime-stats.html");

        assertFalse(overview.contains("setInterval(() =>"));
        assertFalse(realtime.contains("setInterval(intervalInit"));
        assertTrue(overview.contains("setTimeout(intervalInit, 5000)"));
        assertTrue(realtime.contains("setTimeout(intervalInit, 1000)"));
        assertTrue(realtime.contains("removeEventListener(\"visibilitychange\""));
        assertFalse(overview.contains("\"HTTP \" + res.status"));
        assertFalse(realtime.contains("\"HTTP \" + res.status"));
    }

    @Test
    void tablesStayInsideResponsiveContainersAndCustomizerDoesNotAutoOpen() throws IOException {
        String overview = resource("static/ajax/pages-armedis-overview.html");
        String realtime = resource("static/ajax/pages-armedis-realtime-stats.html");
        String management = resource("static/ajax/pages-armedis-management.html");
        String app = resource("static/assets/js/app.js");

        assertTrue(countOccurrences(overview, "class=\"table-responsive\"") >= 4);
        assertTrue(countOccurrences(realtime, "table-responsive") >= 2);
        assertTrue(countOccurrences(management, "class=\"table-responsive\"") >= 1);
        assertFalse(app.contains("offCanvas ? offCanvas.click()"));
    }

    @Test
    void memoryChartKeepsNumericAxisValues() throws IOException {
        String overview = resource("static/ajax/pages-armedis-overview.html");
        String common = resource("static/assets/js/common.js");

        assertTrue(overview.contains("max: Number(lastStatsSumMemory.totalSystemMemory || 0)"));
        assertTrue(common.contains("size = Number(size)"));
        assertTrue(common.contains("Number.isFinite(size)"));
    }

    private static String resource(String path) throws IOException {
        try (InputStream stream = StaticUiRegressionTest.class.getClassLoader().getResourceAsStream(path)) {
            if (stream == null) {
                throw new IOException("Resource not found: " + path);
            }
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private static int countOccurrences(String text, String value) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(value, index)) >= 0) {
            count++;
            index += value.length();
        }
        return count;
    }
}
