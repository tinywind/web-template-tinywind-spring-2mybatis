(function ($) {
    "use strict";

    $('input[type=hidden][name^=_][value]').remove();
    $('body').bindHelpers();
})(jQuery);