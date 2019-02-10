(function ($) {
    function findAndMe(selector, context) {
        return context.find(selector).add(context.filter(selector));
    }

    $.fn.bindHelpers = function () {
        findAndMe('.-ajax-loader', this).asAjaxLoader();
        findAndMe('.-json-submit', this).asJsonSubmit();
        findAndMe('.-lead-selector', this).asLeadSelector();
        findAndMe('.-lean-modal', this).asLeanModal();
        findAndMe('.-input-float', this).asInputFloat();
        findAndMe('.-input-numerical', this).asInputNumerical();
        findAndMe('.-input-phone', this).asInputPhone();
        findAndMe('.-input-restrict-length', this).asInputRestrictLength();
        findAndMe('.-count', this).animateCount();

        /* jquery-ui.datepicker 호출. id 재생성되므로, 기존 id 속성을 삭제시킨다. */
        findAndMe('.-datepicker', this).asDatepicker();

        return this;
    };
})($ || jQuery);