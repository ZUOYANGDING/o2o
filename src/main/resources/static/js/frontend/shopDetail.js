$(function () {
    // loading from backend status
    var loading = false;
    var maxItem = 999;
    var pageSize = 3;
    // url to fetch product list
    var listUrl = '/o2o/frontend/listproductsbyshop';
    var pageNum = 1;
    // get shopId from url
    var shopId = getQueryString('shopId');
    var productCategoryId = '';
    var productName = '';
    // url to fetch shop detail info and product categories in this shop
    var categoryAndShopInfoUrl = '/o2o/frontend/listshopdetailpageinfo?shopId=' + shopId;
    getShopInfoAndProductCategory();
    // init 3 products
    addItems(pageNum, pageSize);

    function getShopInfoAndProductCategory() {
        var url = categoryAndShopInfoUrl;
        $.getJSON(url, function (data) {
            if(data.success) {
                if (data.shopSuccess) {
                    var shop = data.shop;
                    $('#shop-cover-pic').attr('src', getContextPath() + shop.shopImg);
                    $('#shop-update-time').html(new Date(shop.lastEditTime).Format("yyyy-MM-dd"));
                    $('#shop-name').html(shop.shopName);
                    $('#shop-desc').html(shop.shopDesc);
                    $('#shop-addr').html(shop.shopAddress);
                    $('#shop-phone').html(shop.phone);
                } else {
                    $.toast(data.shopErrMsg);
                }
                if (data.productCategorySuccess) {
                    var productCategoryList = data.productCategoryList;
                    var html = '';
                    productCategoryList.map(function (item, index) {
                        html += '<a href="#" class="button" data-product-search-id='
                            + item.productCategoryId
                            + '>'
                            + item.productCategoryName
                            + '</a>';
                    });
                    $('#shopdetail-button-div').html(html);
                } else {
                    $.toast(data.productErrMsg);
                }
            } else {
                if (data.redirect) {
                    window.location.href = data.redirect
                } else {
                    $.toast(data.errMsg);
                }
            }
        })
    }

    function addItems(pageIndex, pageSize) {
        var url = listUrl + '?pageIndex=' + pageIndex + '&pageSize=' + pageSize + '&productCategoryId=' +
            productCategoryId + '&productName=' + productName + '&shopId=' + shopId;
        // set loading status to true, stop fetching from backend
        loading = true;
        $.getJSON(url, function(data) {
            if (data.success) {
                maxItem = data.count;
                var html = '';
                data.productList.map(function (item, index) {
                    html += '' + '<div class="card" data-product-id='
                        + item.productId + '>'
                        + '<div class="card-header">' + item.productName
                        + '</div>' + '<div class="card-content">'
                        + '<div class="list-block media-list">' + '<ul>'
                        + '<li class="item-content">'
                        + '<div class="item-media">' + '<img src="'
                        + getContextPath() + item.imgAddr + '" width="44">'
                        + '</div>' + '<div class="item-inner">'
                        + '<div class="item-subtitle">' + item.productDesc
                        + '</div>' + '</div>' + '</li>' + '</ul>'
                        + '</div>' + '</div>' + '<div class="card-footer">'
                        + '<p class="color-gray">'
                        + new Date(item.lastEditTime).Format("yyyy-MM-dd")
                        + ' update</p>' + '<span>Click to Check Details</span>' + '</div>'
                        + '</div>';
                });
                $('.list-div').append(html);
                var total = $('.list-div .card').length;
                if (total >= maxItem) {
                    $('.infinite-scroll-preloader').hide();
                } else {
                    $('.infinite-scroll-preloader').show();
                }
                pageNum+=1;
                // set loading status back to false, make fetching data from backend possible
                loading = false;
                $.refreshScroller();
            } else {
                $.toast(data.errMsg);
                $('.infinite-scroll-preloader').hide();
                loading = false;
            }
        });
    }

    $(document).on('infinite', '.infinite-scroll-bottom', function () {
        if (loading) {
            return;
        } else {
            addItems(pageNum, pageSize);
        }
    });

    // get search restrictions
    $('#shopdetail-button-div').on('click', '.button', function(e) {
        productCategoryId = e.target.dataset.productSearchId;
        if (productCategoryId) {
            if ($(e.target).hasClass('button-fill')) {
                $(e.target).removeClass('button-fill');
                productCategoryId = '';
            } else {
                $(e.target).addClass('button-fill').siblings().removeClass('button-fill');
            }
            $('.list-div').empty();
            pageNum = 1;
            // reset the shop list
            addItems(pageNum, pageSize);
        }
    });

    // redirect to product detail page
    $('.list-div').on('click', '.card', function(e) {
        var productId = e.currentTarget.dataset.productId;
        window.location.href = '/o2o/frontend/productdetail?productId=' + productId;
    });

    // when search key words changes, reset the pageNum and search result
    $('#search').on('change', function (e) {
        productName = e.target.value;
        $('.list-div').empty();
        pageNum = 1;
        addItems(pageNum, pageSize);
    });

    $('#me').click(function () {
        $.openPanel('#panel-right-demo');
    });

    $.init();
})