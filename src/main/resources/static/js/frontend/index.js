$(function() {
    var url = '/o2o/frontend/listmainpageinfo'
    // get list of headline and root category list
    $.getJSON(url, function(data) {
        // root category list
        if (data.shopCategorySuccess) {
            if (data.shopCategoryList!=null && data.shopCategoryList.size!==0){
                var shopCategoryList = data.shopCategoryList;
                var categoryHtml = '';
                shopCategoryList.map(function (item, index) {
                    categoryHtml += ''
                        + '<div class="col-50 shop-classify" data-category='
                        + item.shopCategoryId + '>' + '<div class="word">'
                        + '<p class="shop-title">' + item.shopCategoryName
                        + '</p>' + '<p class="shop-desc">'
                        + item.shopCategoryDescription + '</p>' + '</div>'
                        + '<div class="shop-classify-img-warp">'
                        + '<img class="shop-img" src="' + getContextPath()
                        + item.shopCategoryImg + '">'
                        + '</div>'
                        + '</div>';
                });
                $('.row').html(categoryHtml);
            } else {
                $.toast("root category list is null");
            }
        } else {
            $.toast(data.shopCategoryErrMsg);
        }

        // head line
        if (data.headLineSuccess) {
            var headLineHtml = '';
            if (data.headLineList!=null && data.headLineList.size!==0) {
                var headLineList = data.headLineList;
                headLineList.map(function (item, index) {
                    headLineHtml += '' + '<div class="swiper-slide img-wrap">'
                        + '<a href="' + item.lineLink
                        + '" external><img class="banner-img" src="'
                        + getContextPath() + item.lineImg + '" alt="'
                        + item.lineName + '"></a>' + '</div>';
                });
            } else {
                $.toast("head line list is null");
            }
            $('.swiper-wrapper').html(headLineHtml);
            // set the head line image swap auto every 3s
            $(".swiper-container").swiper({
                autoplay: 3000,
                autoplayDisableOnInteraction: false
            });
        } else {
            $.toast(data.headLineErrMsg);
        }
    });

    // click me to get the side bar
    $('#me').click(function () {
        $.openPanel('#panel-right-demo');
    });

    $('.row').on('click', 'shop-classify', function (e) {
        var shopCategoryId = e.currentTarget.dataset.category;
        var shopListUrl = '/o2o/frontend/shoplist?parentId=' + shopCategoryId;
        window.location.href = shopListUrl;
    });
})