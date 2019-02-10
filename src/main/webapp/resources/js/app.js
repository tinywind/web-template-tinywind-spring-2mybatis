function popupReceivedHtml(url, id, classes) {
    return receiveHtml(url).done(function (html) {
        $('#' + id).remove();

        const $body = $('body');
        const target = $('<div/>', {
            id: id, 'class': 'layer-modal ' + classes || '', style: 'display: none;', click: function (event) {
                event.stopPropagation();
            }
        }).appendTo($body);
        const mixedNodes = $.parseHTML(html, null, true);
        for (let i = 0; i < mixedNodes.length; i++) {
            const node = $(mixedNodes[i]);
            if (node.is('script')) {
                const id = node.attr('id');
                if (id) $(document.getElementById(id)).remove();
                $body.append(node);
            } else {
                node.appendTo(target)
                    .bindHelpers();
            }
        }

        const caller = $('<a/>', {href: '#' + id, style: 'display: none;'})
            .appendTo($body)
            .leanModal({closeButton: '#lean-overlay-container, .close-button'});

        caller.click().remove();
    });
}
