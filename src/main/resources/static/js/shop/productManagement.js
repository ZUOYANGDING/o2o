$(function () {
    var listUrl = '/o2o/shopadmin/getproductlistbyshop?pageIndex=1&pageSize=999';
    var statusUrl = '/o2o/shopadmin/modifyproduct';

    getProductList()

    function getProductList() {
        $.getJSON(listUrl, function(data) {
            if (data.success) {
                var productList = data.productList;
                var productHtml = '';
                if (productList != null) {
                    productList.map(function(product, index) {
                        var textOpt = "Off";
                        var contraryStatus = 0;
                        if (product.enableStatus === 0) {
                            textOpt = 'On';
                            contraryStatus = 1;
                        } else {
                            contraryStatus = 0;
                        }

                        productHtml += '' + '<div class="row row-product">' +
                            '<div class="col-33">' + product.productName + '</div>' +
                            '<div class="col-20">' + product.priority + '</div>' +
                            '<div class="col-40">' +
                            '<a href="#" class="edit" data-id="' + product.productId +
                            '" data-status="' + product.enableStatus + '">Edit</a>' +
                            '<a href="#" class="status" data-id="' + product.productId +
                            '" data-status="' + contraryStatus + '">' + textOpt + '</a>' +
                            '<a href="#" class="preview" data-id="' + product.productId +
                            '" data-status="' + product.enableStatus + '">Preview</a>' +
                            '</div>' +
                            '</div>'
                    });
                    $('.product-wrap').html(productHtml);
                } else {
                    $.toast("Get No Product " + data.errMsg);
                }
            } else {
                if (data.redirect) {
                    $.toast("failed to get product " + data.errMsg);
                    window.location.href = data.redirect;
                } else {
                    $.toast("failed to get product " + data.errMsg);
                }
            }
        });
    }

    $('.product-wrap').click().on('click', 'a', function(e) {
        var target = $(e.currentTarget);
        if (target.hasClass('edit')) {
            window.location.href = '/o2o/shopadmin/productoperation?productId=' + e.currentTarget.dataset.id;
        } else if (target.hasClass('status')) {
            changeItemStatus(e.currentTarget.dataset.id, e.currentTarget.dataset.status);
        } else if (target.hasClass('preview')) {
            window.location.href = '/o2o/frontend/productdetail?productId=' + e.currentTarget.dataset.id;
        }
    });

    function changeItemStatus(id, status) {
        var product = {};
        product.productId = id;
        product.enableStatus = status;
        $.confirm("Are you sure?", function() {
            $.ajax({
                url: statusUrl,
                type: 'POST',
                data: {
                    productStr: JSON.stringify(product),
                    statusChange: true
                },
                dataType: 'json',
                success: function(data) {
                    if (data.success) {
                        $.toast("Product status change success");
                        getProductList();
                    } else {
                        if (data.redirect) {
                            $.toast("failed to change status " +  data.errMsg);
                            window.location.href = data.redirect;
                        } else {
                            $.toast("failed to change status " + data.errMsg);
                        }
                    }
                }
            });
        });
    }
})