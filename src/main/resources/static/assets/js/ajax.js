/*
Template Name: Armedis - Admin & Dashboard Template
Author: Themesbrand
Version: 3.5.0
Website: https://Themesbrand.com/
Contact: Themesbrand@gmail.com
File: Ajax Js File
*/

function setupLayout() {
	var menuLinks = document.querySelectorAll("#navbar-nav li a");

	menuLinks.forEach(function (ele, i) {
		ele.addEventListener("click", function (e) {
			e.preventDefault()
			var page = e.target.getAttribute("href");
			var target = e.target.getAttribute("target");

			if (!page && e.target.parentElement instanceof HTMLAnchorElement) {
				page = e.target.parentElement.getAttribute("href");
				target = e.target.parentElement.getAttribute("href");
			}

			if (page.indexOf('.html') === -1) {
				return false;
			}

			if (target == "_self") { window.location.href = page; return true };
			if (target == "_blank") window.open(page, "_blank");
			if (page == "javascript: void(0);") return false;

			$("#navbar-nav li, #navbar-nav li a").removeClass("active");
			$("#two-column-menu li a").removeClass("active");
			$("#navbar-nav li a").attr("aria-expanded", "false");
			if (page) {
				// navbar-nav
				var a = document.getElementById("navbar-nav").querySelector('[href="' + page + '"]');
				if (a) {
					if (document.documentElement.getAttribute("data-layout") == "twocolumn") {
						a.classList.add("active");
						var parentCollapseDiv = a.closest(".collapse.menu-dropdown");
						if (parentCollapseDiv && parentCollapseDiv.parentElement.closest(".collapse.menu-dropdown")) {
							parentCollapseDiv.classList.add("show");
							parentCollapseDiv.parentElement.children[0].classList.add("active");
							parentCollapseDiv.parentElement.children[0].setAttribute("aria-expanded", "true");
							parentCollapseDiv.parentElement.closest(".collapse.menu-dropdown").parentElement.classList.add("twocolumn-item-show");
							if (parentCollapseDiv.parentElement.parentElement.parentElement.parentElement.closest(".collapse.menu-dropdown")) {
								var menuIdSub = parentCollapseDiv.parentElement.parentElement.parentElement.parentElement.closest(".collapse.menu-dropdown").getAttribute("id");
								parentCollapseDiv.parentElement.parentElement.parentElement.parentElement.children[0].setAttribute("aria-expanded", "true");
								parentCollapseDiv.parentElement.parentElement.parentElement.parentElement.closest(".collapse.menu-dropdown").parentElement.classList.add("twocolumn-item-show");
								parentCollapseDiv.parentElement.closest(".collapse.menu-dropdown").parentElement.classList.remove("twocolumn-item-show");
								if (document.getElementById("two-column-menu").querySelector('[href="#' + menuIdSub + '"]'))
									document.getElementById("two-column-menu").querySelector('[href="#' + menuIdSub + '"]').classList.add("active");
							}
							var menuId = parentCollapseDiv.parentElement.closest(".collapse.menu-dropdown").getAttribute("id");
							if (document.getElementById("two-column-menu").querySelector('[href="#' + menuId + '"]'))
								document.getElementById("two-column-menu").querySelector('[href="#' + menuId + '"]').classList.add("active");
						} else {
							a.closest(".collapse.menu-dropdown").parentElement.classList.add("twocolumn-item-show");
							var menuId = parentCollapseDiv.getAttribute("id");
							if (document.getElementById("two-column-menu").querySelector('[href="#' + menuId + '"]'))
								document.getElementById("two-column-menu").querySelector('[href="#' + menuId + '"]').classList.add("active");
						}
					} else {
						a.classList.add("active");
						var parentCollapseDiv = a.closest('.collapse.menu-dropdown');

						if (parentCollapseDiv) {
							parentCollapseDiv.classList.add("show");
							parentCollapseDiv.parentElement.children[0].classList.add("active");
							parentCollapseDiv.parentElement.children[0].setAttribute("aria-expanded", "true");
							if (parentCollapseDiv.parentElement.closest('.collapse.menu-dropdown')) {
								parentCollapseDiv.parentElement.closest(".collapse").classList.add("show");
								if (parentCollapseDiv.parentElement.closest(".collapse").previousElementSibling)
									parentCollapseDiv.parentElement.closest(".collapse").previousElementSibling.classList.add("active");
							}
						}
					}
				}
			}
			if (page == "javascript: void(0);") return false;
			call_ajax_page(page);
		});
	})
}

var currentPageUnload = null; // 이전 페이지 cleanup 함수 저장
function call_ajax_page(page) {
	if (typeof currentPageUnload === "function") {
		try {
			currentPageUnload();
		} catch (e) {
			console.error(e);
		}
		currentPageUnload = null;
	}

	$.ajax({
		url: "/ajax/" + page,
		cache: false,
		dataType: "html",
		type: "GET",
		success: function (data) {
			window.location.hash = page;
			$("#ajaxresult").empty();
			$("#ajaxresult").html(data);
			$(window).scrollTop(0);

			var hooks = window.pageHooks && window.pageHooks[page];
			if (hooks) {
				if (typeof hooks.onLoad === "function") {
					try {
						hooks.onLoad();
					} catch (e) {
						console.error("onLoad error: ", e);
					}
				}
				if (typeof hooks.onUnload === "function") {
					currentPageUnload = hooks.onUnload;
				}
			}
		}
	});
}

document.addEventListener('DOMContentLoaded', function () {
	var currentPage = document.location.hash;
	if (currentPage) {
		currentPage = currentPage.replace("#", "");
		document.querySelector("#navbar-nav a[href='" + currentPage + "']") ? document.querySelector("#navbar-nav a[href='" + currentPage + "']").click() : call_ajax_page("pages-armedis-overview.html");
	} else {
		call_ajax_page("pages-armedis-overview.html");
	}
});

document.body.addEventListener("click", function (e) {
	if(!e.target.closest("#navbar-nav")) {
		if(e.target.hash) {
			var urlHash = e.target.hash.replace("#", '');
			if(document.querySelector("#navbar-nav a[href='" + urlHash + "']"))
				document.querySelector("#navbar-nav a[href='" + urlHash + "']").click()
		} else if(e.target.getAttribute("href")) {
			if(document.querySelector("#navbar-nav a[href='" + e.target.getAttribute("href") + "']"))
				document.querySelector("#navbar-nav a[href='" + e.target.getAttribute("href") + "']").click()
		}
	}
});

function windowResizeHover() {
	setupLayout();
}

window.addEventListener("resize", windowResizeHover);
windowResizeHover();