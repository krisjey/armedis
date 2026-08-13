/*
 * Template Name: Armedis - Admin & Dashboard Template
 * File: Ajax Js File
 */

var layoutSetupInitialized = false;
var currentPageUnload = null;
var currentAjaxRequest = null;
var requestedPage = null;
var activePage = null;

function getDefaultPage() {
	return "pages-armedis-overview.html";
}

function normalizePage(page) {
	var candidate = (page || "").replace(/^#/, "");
	var knownLink = document.querySelector("#navbar-nav a[href='" + CSS.escape(candidate) + "']");

	if (!/^[a-z0-9-]+\.html$/i.test(candidate) || !knownLink) {
		return getDefaultPage();
	}
	return candidate;
}

function updateDocumentTitle(page) {
	var titles = {
		"pages-armedis-overview.html": "Overview",
		"pages-armedis-realtime-stats.html": "Realtime Stats",
		"pages-armedis-management.html": "Management"
	};
	document.title = (titles[page] || "Armedis") + " | Armedis";
}

function updateActiveMenu(page) {
	$("#navbar-nav li, #navbar-nav li a").removeClass("active");
	$("#two-column-menu li a").removeClass("active");
	$("#navbar-nav li a").attr("aria-expanded", "false");

	var navbar = document.getElementById("navbar-nav");
	var link = navbar && navbar.querySelector('[href="' + page + '"]');
	if (!link) {
		return;
	}

	link.classList.add("active");
	var parentCollapseDiv = link.closest(".collapse.menu-dropdown");
	if (parentCollapseDiv) {
		parentCollapseDiv.classList.add("show");
		if (parentCollapseDiv.parentElement.children[0]) {
			parentCollapseDiv.parentElement.children[0].classList.add("active");
			parentCollapseDiv.parentElement.children[0].setAttribute("aria-expanded", "true");
		}
	}
}

function cleanupCurrentPage() {
	var unload = currentPageUnload;
	if (typeof unload !== "function" && activePage && window.pageHooks) {
		var activeHooks = window.pageHooks[activePage];
		unload = activeHooks && activeHooks.onUnload;
	}

	if (typeof unload === "function") {
		try {
			unload();
		} catch (error) {
			console.error("onUnload error:", error);
		}
	}
	currentPageUnload = null;
}

function clearPageLoadError() {
	var error = document.querySelector("#ajaxresult .page-load-error");
	if (error) {
		error.remove();
	}
}

function renderPageLoadError(page) {
	var result = document.getElementById("ajaxresult");
	if (!result) {
		return;
	}

	clearPageLoadError();
	var alert = document.createElement("div");
	alert.className = "alert alert-danger page-load-error";
	alert.setAttribute("role", "alert");
	alert.textContent = page + " 화면을 불러오지 못했습니다. 잠시 후 다시 시도해주세요.";
	result.prepend(alert);
}

function call_ajax_page(page, options) {
	var settings = Object.assign({ updateHistory: true, force: false }, options);
	var normalizedPage = normalizePage(page);

	if (!settings.force && requestedPage === normalizedPage) {
		return;
	}

	var previousPage = activePage;
	requestedPage = normalizedPage;
	clearPageLoadError();

	if (currentAjaxRequest) {
		currentAjaxRequest.abort();
	}

	var request = $.ajax({
		url: "/ajax/" + normalizedPage,
		cache: false,
		dataType: "html",
		type: "GET"
	});
	currentAjaxRequest = request;

	request.done(function (data) {
		if (currentAjaxRequest !== request) {
			return;
		}

		cleanupCurrentPage();
		$("#ajaxresult").empty().html(data);
		$(window).scrollTop(0);

		activePage = normalizedPage;
		requestedPage = normalizedPage;
		if (settings.updateHistory && window.location.hash !== "#" + normalizedPage) {
			window.history.pushState({ page: normalizedPage }, "", "#" + normalizedPage);
		}
		updateDocumentTitle(normalizedPage);
		updateActiveMenu(normalizedPage);

		var hooks = window.pageHooks && window.pageHooks[normalizedPage];
		if (hooks) {
			if (typeof hooks.onLoad === "function") {
				try {
					hooks.onLoad();
				} catch (error) {
					console.error("onLoad error:", error);
				}
			}
			if (typeof hooks.onUnload === "function") {
				currentPageUnload = hooks.onUnload;
			}
		}
	});

	request.fail(function (_request, status) {
		if (status === "abort" || currentAjaxRequest !== request) {
			return;
		}

		requestedPage = previousPage;
		if (previousPage) {
			window.history.replaceState({ page: previousPage }, "", "#" + previousPage);
			updateDocumentTitle(previousPage);
			updateActiveMenu(previousPage);
		}
		renderPageLoadError(normalizedPage);
	});

	request.always(function () {
		if (currentAjaxRequest === request) {
			currentAjaxRequest = null;
		}
	});
}

function routeFromLocation() {
	var rawPage = window.location.hash.replace(/^#/, "");
	var page = normalizePage(rawPage);
	if (rawPage !== page) {
		window.history.replaceState({ page: page }, "", "#" + page);
	}
	if (page === requestedPage && page === activePage) {
		return;
	}
	call_ajax_page(page, { updateHistory: false });
}

function setupLayout() {
	if (layoutSetupInitialized) {
		return;
	}
	layoutSetupInitialized = true;

	document.querySelectorAll("#navbar-nav li a").forEach(function (link) {
		link.addEventListener("click", function (event) {
			var page = link.getAttribute("href");
			var target = link.getAttribute("target");

			if (!page || page.indexOf(".html") === -1) {
				return;
			}
			if (target === "_blank" || target === "_self") {
				return;
			}

			event.preventDefault();
			call_ajax_page(page);
		});
	});
}

document.addEventListener("DOMContentLoaded", function () {
	setupLayout();
	routeFromLocation();
});

window.addEventListener("popstate", routeFromLocation);
window.addEventListener("hashchange", routeFromLocation);

document.body.addEventListener("click", function (event) {
	var link = event.target.closest("a[href]");
	if (!link || link.closest("#navbar-nav")) {
		return;
	}

	var page = link.hash ? link.hash.replace("#", "") : link.getAttribute("href");
	var navLink = page && document.querySelector("#navbar-nav a[href='" + CSS.escape(page) + "']");
	if (navLink) {
		event.preventDefault();
		call_ajax_page(page);
	}
});
