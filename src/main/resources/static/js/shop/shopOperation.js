$(function() {
    var shopId = getQueryString('shopId');
    var isEdit = shopId ? true : false;
    var initUrl = '/o2o/shopadmin/getshopinitinfo';
    var registerShopUrl = '/o2o/shopadmin/registershop';
    var shopInfoUrl = '/o2o/shopadmin/getshopbyid?shopId=' + shopId;
    var editShopUrl = '/o2o/shopadmin/modifyshop';

    if (!isEdit) {
        getShopInitInfo();
    } else {
        getShopInfo(shopId);
    }

    function getShopInfo(shopId) {
        $.getJSON(shopInfoUrl, function(data) {
            if (data.success) {
                var shop = data.shop;
                $('#shop-name').val(shop.shopName);
                $('#shop-address').val(shop.shopAddress);
                $('#shop-phone').val(shop.phone);
                $('#shop-desc').val(shop.shopDesc);
                // provide shop category by get id and name from backend (cannot be edit here)
                var shopCategory = '<option data-id="' + shop.shopCategory.shopCategoryId + '"selected>' +
                    shop.shopCategory.shopCategoryName + '</option>>';
                var tempAreaHtml = '';
                // provide area list by get id and name from backend (can be edit here)
                data.areaList.map(function (item, index) {
                    tempAreaHtml += '<option data-id="' + item.areaId + '">' + item.areaName + '</option>';
                });
                $('#shop-category').html(shopCategory);
                // disable category edit
                $('#shop-category').attr('disabled', 'disabled');
                $('#area').html(tempAreaHtml);
                // show origin selected shop area
                $("#area option[data-id='" + shop.area.areaId + "']").attr("selected", "selected");
            } else {
                $.toast("Failed to get shop info: " + data.errMsg);
            }
        })
    }

    function getShopInitInfo() {
        $.getJSON(initUrl, function(data) {
            if (data.success) {
                var tempHtml = '';
                var tempAreaHtml ='';
                data.shopCategoryList.map(function(item, index) {
                    tempHtml += '<option data-id="' + item.shopCategoryId + '">' +
                        item.shopCategoryName + '</option>'
                });
                data.areaList.map(function(item, index) {
                    tempAreaHtml += '<option data-id="' + item.areaId + '">' +
                        item.areaName + '</option>'
                });
                $('#shop-category').html(tempHtml);
                $('#area').html(tempAreaHtml);
            } else {
                $.toast("failed to get shopInfo" + data.errMsg);
            }

        });
    }

    $('#submit').click(function() {
        var shop= {};
        if (isEdit) {
            shop.shopId = shopId;
        }
        shop.shopName = $('#shop-name').val();
        shop.shopAddress = $('#shop-address').val();
        shop.phone = $('#shop-phone').val();
        shop.shopDesc = $('#shop-desc').val();
        shop.shopCategory = {
            shopCategoryId : $('#shop-category').find('option').not(function(){
                return !this.selected;
            }).data('id')
        };
        shop.area = {
            areaId: $('#area').find('option').not(function() {
                return !this.selected;
            }).data('id')
        };
        var shopImg = $('#shop-img')[0].files[0];
        var formData = new FormData();
        formData.append('shopImg',shopImg);
        formData.append('shopStr', JSON.stringify(shop));
        var verifyCodeActual = $('#j-captcha').val();
        if (!verifyCodeActual) {
            $.toast("please enter the verify code");
            return;
        } else {
            formData.append('verifyCodeActual', verifyCodeActual);
        }
        $.ajax({
            url: (isEdit ? editShopUrl : registerShopUrl),
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            success: function(data) {
                if (data.success) {
                    $.toast("success submit");
                } else {
                    $.toast("failed to submit: " + data.errMsg);
                }
                $('#captcha_img').click();
            }
        });
    });
})