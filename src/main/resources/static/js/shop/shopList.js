$(function() {
    getList();
    function getList(e){
        $.ajax({
            url: "/o2o/shopadmin/getshoplist",
            type: "get",
            dataType: "json",
            success: function (data) {
                if (data.success) {
                    handleList(data.shopList);
                    handleUser(data.user);
                } else {
                    $.toast("Failed to get shop list info: " + data.errMsg);
                }
            }
        });
    }

    function handleUser(data) {
        $("#user-name").text(data.name);
    }

    function handleList(shopList) {
        var tempShopHtml ='';
        shopList.map(function(shop, index) {
            tempShopHtml += '<div class="row row-shop"><div class="col-40">' + shop.shopName +
                '</div><div class="col-40">' + shopStatus(shop.enableStatus) +
                '</div><div class="col-20">' + goShop(shop.enableStatus, shop.shopId) +
                '</div></div>';
        });
        $('.shop-wrap').html(tempShopHtml);
    }

    function shopStatus(enableStatus) {
        if (enableStatus == 0) {
            return 'Under Review';
        } else if (enableStatus == 1) {
            return 'Active';
        } else {
            return 'Shop has already been banned';
        }
    }

    function goShop(enableStatus, shopId) {
        if (enableStatus == 1) {
            return '<a href="/o2o/shopadmin/shopmanage?shopId=?' + shopId + '">Enter</a>';
        } else {
            return '';
        }
    }
});