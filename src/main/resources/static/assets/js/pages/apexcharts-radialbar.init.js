/*
Template Name: Armedis - Admin & Dashboard Template
Author: Themesbrand
Website: https://Themesbrand.com/
Contact: Themesbrand@gmail.com
File: Radialbar Chart init js
*/

// get colors array from the string
function getChartColorsArray(chartId) {
    var chartElement = document.getElementById(chartId);

    if (chartElement !== null) {
        var colors = chartElement.getAttribute("data-colors");
        colors = JSON.parse(colors);
        return colors.map(function (value) {
            var newValue = value.replace(" ", "");
            if (newValue.indexOf(",") === -1) {
                var color = getComputedStyle(document.documentElement).getPropertyValue(newValue);
                if (color) return color;
                else return newValue;
            } else {
                var val = value.split(',');
                if (val.length == 2) {
                    var rgbaColor = getComputedStyle(document.documentElement).getPropertyValue(val[0]);
                    rgbaColor = "rgba(" + rgbaColor + "," + val[1] + ")";
                    return rgbaColor;
                } else {
                    return newValue;
                }
            }
        });
    }
}

function getChartColors(chartId, value) {
    var colors = getChartColorsArray(chartId);
    
    if (value >= 90) return colors[2];
    else if (value >= 70) return colors[1];
    return colors[0];
}