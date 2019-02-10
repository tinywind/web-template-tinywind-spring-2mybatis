const restSelf = {
    get: function (url, data, failFunc, noneBlockUi) {
        return restUtils.get((contextPath ? contextPath : '') + url, data, noneBlockUi).done(function (response) {
            if (response.result !== 'success')
                return alert(getDefaultReason(response));
        }).fail(failFunc ? typeof failFunc === "string" ? failFunction(failFunc) : failFunc.apply(null, []) : failFunction());
    },
    post: function (url, data, failFunc, noneBlockUi) {
        return restUtils.post((contextPath ? contextPath : '') + url, data, noneBlockUi).done(function (response) {
            if (response.result !== 'success')
                return alert(getDefaultReason(response));
        }).fail(failFunc ? typeof failFunc === "string" ? failFunction(failFunc) : failFunc.apply(null, []) : failFunction());
    },
    put: function (url, data, failFunc, noneBlockUi) {
        return restUtils.put((contextPath ? contextPath : '') + url, data, noneBlockUi).done(function (response) {
            if (response.result !== 'success')
                return alert(getDefaultReason(response));
        }).fail(failFunc ? typeof failFunc === "string" ? failFunction(failFunc) : failFunc.apply(null, []) : failFunction());
    },
    delete: function (url, data, failFunc, noneBlockUi) {
        return restUtils.delete((contextPath ? contextPath : '') + url, data, noneBlockUi).done(function (response) {
            if (response.result !== 'success')
                return alert(getDefaultReason(response));
        }).fail(failFunc ? typeof failFunc === "string" ? failFunction(failFunc) : failFunc.apply(null, []) : failFunction());
    }
};

function receiveHtml(url, data, failFunc, noneBlockUi) {
    function failFunction(defaultMessage) {
        return alert(defaultMessage ? defaultMessage : '처리 실패');
    }

    if (!noneBlockUi) $.blockUIFixed();
    return $.ajax({
        type: 'get',
        dataType: 'html',
        url: $.addQueryString($.addQueryString(url, data), {____t: new Date().getTime()})
    }).fail(function (e) {
        failFunc ? typeof failFunc === "string" ? failFunction(failFunc) : failFunc.apply(null, [e]) : failFunction()
    }).always(function () {
        if (!noneBlockUi) $.unblockUI();
    });
}

function getDefaultReason(response) {
    if (response.reason != null)
        return response.reason;

    if (response.globalErrors && response.globalErrors.length > 0)
        return response.globalErrors[0].defaultMessage;

    if (response.fieldErrors && response.fieldErrors.length > 0)
        return response.fieldErrors[0].defaultMessage;

    return "";
}

function failFunction(defaultMessage) {
    return function (response) {
        if (response.responseText) {
            try {
                return alert(getDefaultReason(JSON.parse(response.responseText)));
            } catch (e) {
                return console.error(e);
            }
        }

        alert(defaultMessage ? defaultMessage : '처리 실패');
    }
}

function submitDone(form, response, done) {
    $(form).find('.-alert[data-field]').hide();
    if (response.result === "success") {
        if (done) window[done].apply(form, [form, response]);
        return;
    }

    const formAlert = (form != null ? $(form).find(".-alert") : $(".-alert"));
    if (formAlert.length === 0) {
        const reason = getDefaultReason(response);
        return alert(reason == null ? "처리에 실패하였습니다." : reason);
    }

    const queryGlobalAlert = ".-alert[data-field=" + "-alert".toJquerySelector() + "]";
    const formGlobalAlert = (form != null ? $(form).find(queryGlobalAlert) : $(queryGlobalAlert));

    if ((response.globalErrors == null || response.globalErrors.length === 0)
        && (response.fieldErrors == null || response.fieldErrors.length === 0)) {
        const reason = response.reason ? response.reason : "처리에 실패하였습니다.";
        if (formGlobalAlert.length === 0)
            return alert(reason);

        formGlobalAlert.find("span").text(reason);
        formGlobalAlert.show();
    }

    if (response.globalErrors && response.globalErrors.length > 0) {
        if (formGlobalAlert.length === 0) {
            alert(response.globalErrors[0].defaultMessage);
        } else {
            formGlobalAlert.find("span").text(response.globalErrors[0].defaultMessage);
            formGlobalAlert.show();
        }
    }

    let toScroll = null;
    if (response.fieldErrors) {
        const presentResult = presentFormAlert(response.fieldErrors, form);
        toScroll = presentResult.toScroll;
        response.outstanding = presentResult.outstanding;
    }

    if (toScroll != null) {
        const closestModal = $(form).closest('.modal');
        if (closestModal.length > 0)
            closestModal.animate({scrollTop: toScroll - 30}, 'fast');
        else
            $('html, body').animate({scrollTop: toScroll - 30}, 'fast');
    }
}

function presentFormAlert(fieldErrors, form) {
    let toScroll = null;
    const outstanding = [];

    for (let i = 0; i < fieldErrors.length > 0; i++) {
        const error = fieldErrors[i];
        if (error.field == null) {
            console.error("error.field is null");
            console.error(error);
            outstanding.push(error);
            fieldErrors.splice(i--, 1);
            continue;
        }

        const query = ".-alert[data-field=" + error.field.toJquerySelector() + "]";
        const formAlert = (form != null ? $(form).find(query) : $(query));
        if (formAlert.length > 0) {
            formAlert.find("span").text(error.defaultMessage);
            formAlert.show();

            const top = formAlert.offset().top;
            if (toScroll == null || toScroll > top)
                toScroll = top;
        } else {
            console.error("not found alert field" + (error.bindingFailure != null && error.bindingFailure === true ? "(bindingFailure)" : "") + ": " + error.field.toJquerySelector());
            outstanding.push(error);
            fieldErrors.splice(i--, 1);
        }
    }

    return {toScroll: toScroll, outstanding: outstanding};
}

function PageNavigation(page, totalCount, numberOfRowsPerPage, numberOfPagesPerNavigation) {
    this.first = 0;
    this.page = page;
    this.rowsPerPage = numberOfRowsPerPage;
    const last = parseInt(Math.ceil(totalCount * 1.0 / numberOfRowsPerPage));
    const begin = page - (page % numberOfPagesPerNavigation);
    const end = parseInt(Math.min(begin + numberOfPagesPerNavigation, last));

    const max = Math.max(end, 1);
    this.items = [];
    for (let i = begin; i < max; i++)
        this.items[i - begin] = i;
    this.previous = Math.max(page - 1, this.first);
    this.next = Math.max(Math.min(last - 1, page + 1), this.first);
    this.last = Math.max(last - 1, this.first);
}

PageNavigation.prototype.contains = function (page) {
    for (let i = 0; i < this.items.length; i++)
        if (this.items[i] === parseInt(page))
            return true;
    return false;
};

function createPagination(page, totalCount, numberOfRowsPerPage, makeHref, actionScript) {
    const navigation = new PageNavigation(page, totalCount, numberOfRowsPerPage, 5);

    const container = $('<ul/>', {'class': 'pagination pagination-sm justify-content-center'});

    const firstLink = $('<a/>', {
        'class': 'page-link',
        'aria-label': "Previous",
        href: (actionScript ? "javascript:" : "") + makeHref(navigation.first),
        text: 'Previous'
    }).attr('data-page', navigation.first);

    const lastLink = $('<a/>', {
        'class': 'page-link',
        'aria-label': "Next",
        href: (actionScript ? "javascript:" : "") + makeHref(navigation.next),
        text: 'Next'
    }).attr('data-page', navigation.next);

    const linkerList = [];
    for (let i = 0; i < navigation.items.length; i++)
        linkerList.push($('<a/>', {
            'class': 'page-link',
            href: (actionScript ? "javascript:" : "") + makeHref(navigation.items[i]),
            text: navigation.items[i] + 1
        }).attr('data-page', navigation.items[i]));

    container.append($('<li/>', {'class': navigation.contains(navigation.first) ? 'page-item disabled' : 'page'}).append(firstLink));
    for (let i = 0; i < linkerList.length; i++)
        container.append($('<li/>', {'class': parseInt(linkerList[i].attr('data-page')) === parseInt(page) ? 'page-item active' : 'page'}).append(linkerList[i]));
    container.append($('<li/>', {'class': navigation.contains(navigation.last) ? 'page-item disabled' : 'page'}).append(lastLink));

    return container;
}

function popupOpen(url, popupId, width, height) {
    window.open((contextPath || '') + url, popupId || '_blank', "menubar=no,status=no,titlebar=no,scrollbars=yes,resizable=yes,width=" + (width || 1000) + ",height=" + (height || 700));
}