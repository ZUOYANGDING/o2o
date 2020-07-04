$(function(){
    // loading status
    var loading = false;
    // max items get from backend for search
    var maxItems = 999;
    // max return for each page
    var pageSize = 3;
    // url to get shop list
    var shopListUrl = '/o2o/frontend/listshops';
    // url to get shop category and area list
    var categoryAndAreaListUrl = '/o2o/frontend/listshopspageinfo';
    // start page index from 1
    var pageNum = 1;
    // get parent category from url
    var parentId = getQueryString('parentId');
    // child category selected
    var categorySelected = false;
    if (parentId) {
        categorySelected = true;
    }
    var shopCategoryId = '';
    var shopName = '';
    var areaId = '';
    // get shop categories and area list for search
    getDataForSearch();
    // get 3 shops from backend
    addItems(pageNum, pageSize);


    function getDataForSearch() {
        // if parentId exist, get all shop categories belong to this root category
        var url = categoryAndAreaListUrl + '?parentId=' + parentId;
        $.getJSON(url, function (data) {
            if (data.success) {
                // deal with shop categories from backend
                var shopCategoryList = data.shopCategoryList;
                var shopCategoryHtml = '';
                shopCategoryHtml += '<a href="#" class="button" data-category-id=""> All Shop Categories </a>';
                shopCategoryList.map(function(item, index) {
                    shopCategoryHtml += '<a href="#" class="button" data-category-id='
                        + item.shopCategoryId
                        + '>'
                        + item.shopCategoryName
                        + '</a>';
                });
                $('#shoplist-search-div').html(shopCategoryHtml);

                // deal with area list from backend
                var areaList = data.areaList;
                var selectOptions = '<option value="">All Areas</option>';
                areaList.map(function(item, index) {
                    selectOptions += '<option value="'
                        + item.areaId + '">'
                        + item.areaName + '</option>';
                });
                $('#area-search').html(selectOptions);
            } else {
                $.toast(data.errMsg);
            }
        });
    }

    function addItems(pageIndex, pageSize) {
        // controller level will deal with the empty parameters
        var url = shopListUrl + '?pageIndex=' + pageIndex + '&pageSize=' + pageSize + '&parentId=' + parentId +
            '&areaId=' + areaId + '&shopCategoryId=' + shopCategoryId + '&shopName=' + shopName;
        // set loading to true, avoid fetch data from backend again
        loading = true;
        $.getJSON(url, function(data) {
            if (data.success) {
                maxItems = data.count;
                var shopList = data.shopList;
                var shopListHtml = '';
                shopList.map(function (item, index) {
                    shopListHtml += '' + '<div class="card" data-shop-id="'
                        + item.shopId + '">' + '<div class="card-header">'
                        + item.shopName + '</div>'
                        + '<div class="card-content">'
                        + '<div class="list-block media-list">' + '<ul>'
                        + '<li class="item-content">'
                        + '<div class="item-media">' + '<img src="'
                        + getContextPath() + item.shopImg + '" width="44">' + '</div>'
                        + '<div class="item-inner">'
                        + '<div class="item-subtitle">' + item.shopDesc
                        + '</div>' + '</div>' + '</li>' + '</ul>'
                        + '</div>' + '</div>' + '<div class="card-footer">'
                        + '<p class="color-gray">'
                        + new Date(item.lastEditTime).Format("yyyy-MM-dd")
                        + ' update</p>' + '<span>click to check details</span>' + '</div>'
                        + '</div>';
                });
                $('.list-div').append(shopListHtml);
                // get num of shops until right now
                var totalShops = $('.list-div .card').length;
                if (totalShops >= maxItems) {
                    // when num of shops over the max, do not fetch shops from backend anymore
                    $('.infinite-scroll-preloader').hide();
                    return;
                } else {
                    $('.infinite-scroll-preloader').show();
                }
                pageNum+=1;
                // set the loading status to false, let fetch data progress resume
                loading = false;
                $.refreshScroller();
            } else {
                $.toast(data.errMsg);
                $('.infinite-scroll-preloader').hide();
                loading = false;
            }
        });
    }

    // scroller down the page to add more shops
    $(document).on('infinite', '.infinite-scroll-bottom', function() {
        if (loading) {
            return;
        } else {
            addItems(pageNum, pageSize);
        }
    });

    // click the shop card to redirect to shop detail page
    $('.shop-list').on('click', '.card', function (e) {
        var shopId = e.currentTarget.dataset.shopId;
        window.location.href = '/o2o/frontend/shopdetail?shopId=' + shopId;
    });

    // reset the search restriction
    $('#shoplist-search-div').on('click', '.button', function (e) {
        if (parentId!=null && categorySelected) {
            // a category under the root category
            shopCategoryId = e.target.dataset.categoryId;
            // clear the exist search restriction if it exists
            if ($(e.target).hasClass('button-fill')) {
                $(e.target).removeClass('button-fill');
                shopCategoryId = '';
            } else {
                $(e.target).addClass('button-fill').siblings().removeClass('button-fill');
            }
            //reset the shop list
            $('.list-div').empty();
            pageNum = 1;
            addItems(pageNum, pageSize);
        } else {
            // when parentId is null, set it to selected root
            parentId = e.target.dataset.categoryId;
            if ($(e.target).hasClass('button-fill')) {
                $(e.target).removeClass('button-fill');
                parentId = '';
            } else {
                $(e.target).addClass('button-fill').siblings().removeClass('button-fill');
            }
            //reset the shop list
            $('.list-div').empty();
            pageNum = 1;
            addItems(pageNum, pageSize);
        }
    });

    // reset shop list when input new shop name
    $('#search').on('change', function(e) {
        shopName = e.target.value;
        $('.list-div').empty();
        pageNum = 1;
        addItems(pageNum, pageSize);
    });

    // reset shop list when choose new area
    $('#area-search').on('change', function (e) {
        areaId = $('#area-search').val();
        $('.list-div').empty();
        pageNum = 1;
        addItems(pageNum, pageSize);
    });

    // click Me to open sidebar
    $('#me').click(function () {
        $.openPanel('#panel-right-demo');
    });

    $.init();
})
