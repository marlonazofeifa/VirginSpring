window.onload = function () {

    var partnerType = $('#partner-type');
    var email = $('#email');

    if(partnerType.val() == 0) {
        email.addClass('not-visible');
    }

    partnerType.change( function() {
        if($(partnerType).val() == 0) {
            email.addClass('not-visible');
        } else {
           email.removeClass('not-visible');
        }
    });
};

