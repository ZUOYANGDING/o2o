$(function() {
    var bindUrl = '/o2o/local/bindlocalauth';
    var usertype = getQueryString('usertype');

    $('#submit').click(function() {
        var username = $('#username').val();
        var password = $('#password').val();
        var verifyCode = $('#j_captcha').val();
        var needVerify = false;
        if (!verifyCode) {
            $.toast('Please Enter the verification code');
            return;
        }

        $.ajax({
            url: bindUrl,
            async: false,
            cache: false,
            type: 'POST',
            dataType: 'json',
            data: {
                username: username,
                password: password,
                verifyCodeActual: verifyCode
            },
            success : function(data) {
                if (data.success) {
                    // $.toast('Bind Operation Success');
                    if (usertype == 1) {
                        window.location.href = '/o2o/frontend/index';
                    } else {
                        window.location.href = '/o2o/shopadmin/shoplist';
                    }
                } else {
                    $.toast('Bind Operation Failed:  ' + data.errMsg);
                    $('#captcha_img').click();
                }
            }
        });
    });
});