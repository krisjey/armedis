/*
Template Name: Armedis - Admin & Dashboard Template
Author: Themesbrand
Version: 3.5.0
Website: https://Themesbrand.com/
Contact: Themesbrand@gmail.com
File: Main Js File
*/

(function () {

    'use strict';

    var radios = document.querySelectorAll('input[type=radio][name="data-layout"]');
    radios.forEach(radio => radio.addEventListener('change', () => {
        if(radio.value == "horizontal"){
            window.location.href = "layouts-horizontal.html";
        }else if(radio.value == "twocolumn"){
            window.location.href = "layouts-two-column.html";
        }else{
            window.location.href = "index.html";
        }
    }));

    function pluginData() {
        /**
         * Common plugins
         */
        /**
         * Toast UI Notification
         */
        var toastExamples = document.querySelectorAll("[data-toast]");
        toastExamples.forEach(function (element) {
            element.addEventListener("click", function () {
                var toastData = {};
                var isToastVal = element.attributes;
                if (isToastVal["data-toast-text"]) {
                    toastData.text = isToastVal["data-toast-text"].value.toString();
                }
                if (isToastVal["data-toast-gravity"]) {
                    toastData.gravity = isToastVal["data-toast-gravity"].value.toString();
                }
                if (isToastVal["data-toast-position"]) {
                    toastData.position = isToastVal["data-toast-position"].value.toString();
                }
                if (isToastVal["data-toast-className"]) {
                    toastData.className = isToastVal["data-toast-className"].value.toString();
                }
                if (isToastVal["data-toast-duration"]) {
                    toastData.duration = isToastVal["data-toast-duration"].value.toString();
                }
                if (isToastVal["data-toast-close"]) {
                    toastData.close = isToastVal["data-toast-close"].value.toString();
                }
                if (isToastVal["data-toast-style"]) {
                    toastData.style = isToastVal["data-toast-style"].value.toString();
                }
                if (isToastVal["data-toast-offset"]) {
                    toastData.offset = isToastVal["data-toast-offset"]
                }
                Toastify({
                    newWindow: true,
                    text: toastData.text,
                    gravity: toastData.gravity,
                    position: toastData.position,
                    className: "bg-" + toastData.className,
                    stopOnFocus: true,
                    offset: {
                        x: toastData.offset ? 50 : 0, // horizontal axis - can be a number or a string indicating unity. eg: '2em'
                        y: toastData.offset ? 10 : 0, // vertical axis - can be a number or a string indicating unity. eg: '2em'
                    },
                    duration: toastData.duration,
                    close: toastData.close == "close" ? true : false,
                    style: toastData.style == "style" ? {
                        background: "linear-gradient(to right, #0AB39C, #405189)",
                    } : "",
                }).showToast();
            });
        });

        /**
         * Choices Select plugin
         */
        var choicesExamples = document.querySelectorAll("[data-choices]");
        
        choicesExamples.forEach(function (item) {
            var choiceData = {};
            var isChoicesVal = item.attributes;
            if (isChoicesVal["data-choices-groups"]) {
                (choiceData.placeholderValue = "This is a placeholder set in the config")
            }
            if (isChoicesVal["data-choices-search-false"]) {
                (choiceData.searchEnabled = false)
            }
            if (isChoicesVal["data-choices-search-true"]) {
                (choiceData.searchEnabled = true)
            }
            if (isChoicesVal["data-choices-removeItem"]) {
                (choiceData.removeItemButton = true)
            }
            if (isChoicesVal["data-choices-sorting-false"]) {
                choiceData.shouldSort = false;
            }
            if (isChoicesVal["data-choices-sorting-true"]) {
                choiceData.shouldSort = true;
            }
            if (isChoicesVal["data-choices-multiple-default"]) {
                // choiceData.removeItemButton=true
            }
            if (isChoicesVal["data-choices-multiple-groups"]) {
                // choiceData.removeItemButton=true
            }
            if (isChoicesVal["data-choices-multiple-remove"]) {
                choiceData.removeItemButton = true;
            }
            if (isChoicesVal["data-choices-limit"]) {
                choiceData.maxItemCount = isChoicesVal["data-choices-limit"].value.toString();
            }
            if (isChoicesVal["data-choices-limit"]) {
                choiceData.maxItemCount = isChoicesVal["data-choices-limit"].value.toString();
            }
            if (isChoicesVal["data-choices-editItem-true"]) {
                choiceData.maxItemCount = true
            }
            if (isChoicesVal["data-choices-editItem-false"]) {
                choiceData.maxItemCount = false
            }
            if (isChoicesVal["data-choices-text-unique-true"]) {
                (choiceData.duplicateItemsAllowed = false)
            }
            if (isChoicesVal["data-choices-text-disabled-true"]) {
                (choiceData.addItems = false)
            }
            isChoicesVal["data-choices-text-disabled-true"] ?
                new Choices(item, choiceData).disable() :
                new Choices(item, choiceData);
        });

        /**
         * flatpickr
         */
        var flatpickrExamples = document.querySelectorAll("[data-provider]");
        flatpickrExamples.forEach(function (item) {
            if (item.getAttribute("data-provider") == "flatpickr") {
                var dateData = {};
                var isFlatpickerVal = item.attributes;
                if (isFlatpickerVal["data-date-format"]) {
                    (dateData.dateFormat = isFlatpickerVal["data-date-format"].value.toString());
                }
                if (isFlatpickerVal["data-enable-time"]) {
                    dateData.enableTime = true,
                        dateData.dateFormat = isFlatpickerVal["data-date-format"].value.toString() + " H:i";
                }
                if (isFlatpickerVal["data-altFormat"]) {
                    dateData.altInput = true,
                        dateData.altFormat = isFlatpickerVal["data-altFormat"].value.toString();
                }
                if (isFlatpickerVal["data-minDate"]) {
                    dateData.minDate = isFlatpickerVal["data-minDate"].value.toString();
                    dateData.dateFormat = isFlatpickerVal["data-date-format"].value.toString();
                }
                if (isFlatpickerVal["data-maxDate"]) {
                    dateData.maxDate = isFlatpickerVal["data-maxDate"].value.toString();
                    dateData.dateFormat = isFlatpickerVal["data-date-format"].value.toString();
                }
                if (isFlatpickerVal["data-deafult-date"]) {
                    dateData.defaultDate = isFlatpickerVal["data-deafult-date"].value.toString();
                    dateData.dateFormat = isFlatpickerVal["data-date-format"].value.toString();
                }
                if (isFlatpickerVal["data-multiple-date"]) {
                    dateData.mode = "multiple";
                    dateData.dateFormat = isFlatpickerVal["data-date-format"].value.toString();
                }
                if (isFlatpickerVal["data-range-date"]) {
                    dateData.mode = "range";
                    dateData.dateFormat = isFlatpickerVal["data-date-format"].value.toString();
                }
                if (isFlatpickerVal["data-inline-date"]) {
                    dateData.inline = true,
                        dateData.defaultDate = isFlatpickerVal["data-deafult-date"].value.toString();
                    dateData.dateFormat = isFlatpickerVal["data-date-format"].value.toString();
                }
                if (isFlatpickerVal["data-disable-date"]) {
                    var dates = []
                    dates.push(isFlatpickerVal["data-disable-date"].value);
                    dateData.disable = dates.toString().split(",");
                }
                flatpickr(item, dateData);
            } else if (item.getAttribute("data-provider") == "timepickr") {
                var timeData = {};
                var isTimepickerVal = item.attributes;
                if (isTimepickerVal["data-time-basic"]) {
                    (timeData.enableTime = true),
                    (timeData.noCalendar = true),
                    (timeData.dateFormat = "H:i");
                }
                if (isTimepickerVal["data-time-hrs"]) {
                    (timeData.enableTime = true),
                    (timeData.noCalendar = true),
                    (timeData.dateFormat = "H:i"),
                    (timeData.time_24hr = true);
                }
                if (isTimepickerVal["data-min-time"]) {
                    (timeData.enableTime = true),
                    (timeData.noCalendar = true),
                    (timeData.dateFormat = "H:i"),
                    timeData.minTime = isTimepickerVal["data-min-time"].value.toString()
                }
                if (isTimepickerVal["data-max-time"]) {
                    (timeData.enableTime = true),
                    (timeData.noCalendar = true),
                    (timeData.dateFormat = "H:i"),
                    timeData.minTime = isTimepickerVal["data-max-time"].value.toString()
                }
                if (isTimepickerVal["data-default-time"]) {
                    (timeData.enableTime = true),
                    (timeData.noCalendar = true),
                    (timeData.dateFormat = "H:i"),
                    (timeData.defaultDate = isTimepickerVal["data-default-time"].value.toString());
                }
                if (isTimepickerVal["data-time-inline"]) {
                    (timeData.enableTime = true),
                    (timeData.noCalendar = true),
                    (timeData.defaultDate = isTimepickerVal["data-time-inline"].value.toString());
                    (timeData.inline = true);
                }
                flatpickr(item, timeData);
            }
        });

        // Dropdown
        document.querySelectorAll('.dropdown-menu a[data-bs-toggle="tab"]').forEach(function (element) {
            element.addEventListener('click', function (e) {
                e.stopPropagation();
                bootstrap.Tab.getInstance(e.target).show();
            });
        });
    }

    // Counter Number
    function counter() {
        var counter = document.querySelectorAll('.counter-value');
        var speed = 250; // The lower the slower
        counter && counter.forEach(function (counter_value) {
            function updateCount() {
                var target = +counter_value.getAttribute('data-target');
                var count = +counter_value.innerText;
                var inc = target / speed;
                if (inc < 1) {
                    inc = 1;
                }
                // Check if target is reached
                if (count < target) {
                    // Add inc to count and output in counter_value
                    counter_value.innerText = (count + inc).toFixed(0);
                    // Call function every ms
                    setTimeout(updateCount, 1);
                } else {
                    counter_value.innerText = numberWithCommas(target);
                }
                numberWithCommas(counter_value.innerText);
            };
            updateCount();
        });

        function numberWithCommas(x) {
            return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        }
    }

    function resetLayout() {
        if (document.getElementById('reset-layout')) {
            document.getElementById('reset-layout').addEventListener('click', function () {
                sessionStorage.clear();
                window.location.reload();
            });
        }
    }

    function windowLoadContent() {
        // Demo show code
            var checkbox = document.getElementsByClassName("code-switcher");
            Array.from(checkbox).forEach(function (check, i) {
                check.addEventListener('change', function () {
                    var card = check.closest('.card');
                    var preview = card.querySelector(".live-preview");
                    var code = card.querySelector(".code-view");

                    if (check.checked) {
                        // do this
                        preview.classList.add("d-none");
                        code.classList.remove("d-none");
                    } else {
                        // do that
                        preview.classList.remove("d-none");
                        code.classList.add("d-none");
                    }
                });
            });
       
    }

    function init() {
        counter();
        resetLayout();
        pluginData();
        windowLoadContent();
    }
    
    init();

    var timeOutFunctionId;
    function setResize(){
        var currentLayout = document.documentElement.getAttribute("data-layout");
        if(currentLayout !== "horizontal") {
            if(document.getElementById("navbar-nav")) {
                var simpleBar = new SimpleBar(document.getElementById("navbar-nav"));
                if(simpleBar)
                    simpleBar.getContentElement();
            }

            if(document.getElementsByClassName("twocolumn-iconview")[0]) {
                var simpleBar1 = new SimpleBar(document.getElementsByClassName("twocolumn-iconview")[0]);
                if(simpleBar1)
                    simpleBar1.getContentElement();
            }
            clearTimeout(timeOutFunctionId);
        }
    }

    window.addEventListener('resize', function () {       
        if(timeOutFunctionId)
            clearTimeout(timeOutFunctionId);        
        timeOutFunctionId = setTimeout(setResize, 2000);      
    });

})();

/**
 * get colors array from the string
 * 
 * @param {*} chartId 
 * @returns 
 */
/* function getChartColorsArray(chartId) {
    if (document.getElementById(chartId) !== null) {
        var colors = document.getElementById(chartId).getAttribute("data-colors");
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
} */

/**
 * 테이블 생성
 * 
 * 사용방법
 * var headers = ['title1', 'title2']
 * var rows = [{ title1: 'cell 1-1', title2: 'cell 1-2'}, { title1: 'cell 2-1', title2: 'cell 2-2' }]
 * createTable(headers, rows, "table table-bordered dt-responsive nowrap table-striped align-middle", { width: "100%" }) 
 * 
 * @param {*} headers Headers Data
 * @param {*} rows Rows Data
 * @param {*} className CSS Class명
 * @param {*} styleObj Style Object
 * @returns 
 */
function createTable(headers, rows, className, styleObj) {
    var table = document.createElement("table");

    // class 설정
    if (className) {
        table.className = className;
    }

    // style 설정
    if (styleObj) {
        Object.assign(table.style, styleObj);
    }

    // 제목 설정
    var thead = document.createElement("thead");
    var trHead = document.createElement("tr");
    headers.forEach(header => {
        var th = document.createElement("th");
        th.style.textAlign = "center";
        th.innerText = header;
        trHead.appendChild(th);
    });
    thead.appendChild(trHead);
    table.appendChild(thead);
    
    // 데이터 설정
    var tbody = document.createElement("tbody");
    rows.forEach(rowObj => {
        var tr = document.createElement("tr");
        headers.forEach(key => {
            var td = document.createElement("td");
            td.style.textAlign = "center";
            td.innerText = rowObj[key] ?? "-";
            tr.appendChild(td);
        });
        tbody.appendChild(tr);
    });
    table.appendChild(tbody);

    return table;
}

/**
 * 메모리 용량 단위
 * 
 * @param {1} value Byte값
 * @returns 
 */
function getMemoryCapacityUnit(value, decimal = 1) {
    if (value) {
        if (value >= 1024 * 1024 * 1024) {
            return (value / (1024 * 1024 * 1024)).toFixed(decimal) + " GB";
        } else if (value >= 1024 * 1024) {
            return (value / (1024 * 1024)).toFixed(decimal) + " MB";
        } else if (value >= 1024) {
            return (value / 1024).toFixed(decimal) + " KB";
        } else {
            return value.toFixed(decimal) + " B";
        }
    } 
    return "";
}