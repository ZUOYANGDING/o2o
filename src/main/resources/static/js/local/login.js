$(function() {
    var loginUrl = '/o2o/local/logincheck';
    var usertype = getQueryString('usertype');
    var loginCount = 0;

    $('#submit').click(function () {
        var username = $('#username').val();
        var password = $('#password').val();
        var verifyCode = $('#j_captcha').val();
        var needVerify = false;

        if (loginCount >= 3) {
            if (!verifyCode) {
                $.toast("Please enter the verification code");
            } else {
                needVerify = true;
            }
        }

        $.ajax({
            url: loginUrl,
            async: false,
            cache: false,
            type: 'POST',
            dataType: 'json',
            data: {
                username: username,
                password: password,
                verifyCodeActual: verifyCode,
                needVerify: needVerify
            },
            success : function(data) {
                if (data.success) {
                    $.toast("Login Successfully");
                    if (usertype == 1) {
                        window.location.href = '/o2o/frontend/index';
                    } else {
                        window.location.href = '/o2o/shopadmin/shoplist';
                    }
                } else {
                    $.toast("Failed login " + data.errMsg);
                    loginCount++;
                    if (loginCount >= 3) {
                        $('#verifyPart').show();
                    }
                }
            }
        })
    })
})