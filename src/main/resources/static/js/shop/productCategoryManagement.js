$(function () {
    var categoryListUrl = '/o2o/shopadmin/getproductcategorylist';
    var addCategoryUrl = '/o2o/shopadmin/addproductcategories';
    var deleteCategoryUrl = '/o2o/shopadmin/deleteproductcategory';
    getList();
    function getList() {
        $.getJSON(categoryListUrl, function(data) {
            if (data.success) {
                var categoryList = data.productCategoryList;
                var tempHtml = '';
                categoryList.map(function(category, index) {
                    tempHtml += '<div class="row row-product-category now">' +
                        '<div class="col-33">' +
                        category.productCategoryName +
                        '</div><div class="col-33">' +
                        category.priority +
                        '</div><div class="col-33"><a href="#" class="button delete" data-id="' +
                        category.productCategoryId +
                        '">Delete</a></div>' +
                        '</div>'
                });
                $('.category-wrap').html(tempHtml);
            } else {
                $.toast("Failed to get product category: " + data.errMsg);
            }
        });
    }
    $('#new').click(function() {
        var addHtml = '<div class="row row-product-category temp">' +
            '<div class="col-33"><input class="category-input category" type="text" placeholder="Category Name"></div>'
            + '<div class="col-33"><input class="category-input priority" type="number" placeholder="Priority"></div>'
            + '<div class="col-33"><a href="#" class="button delete">Delete</a></div>'
        $('.category-wrap').append(addHtml);
    });

    $('#submit').click(function () {
        var newAdded = $('.temp');
        var productCategoryList = [];
        newAdded.map(function(index, productCategory) {
            var tempObj = {};
            tempObj.productCategoryName = $(productCategory).find('.category').val();
            tempObj.priority = $(productCategory).find('.priority').val();
            if (tempObj.productCategoryName && tempObj.priority) {
                productCategoryList.push(tempObj)
            }
        });
        $.ajax({
            url: addCategoryUrl,
            type: 'POST',
            data: JSON.stringify(productCategoryList),
            contentType: 'application/json',
            success: function(data) {
                if (data.success) {
                    $.toast('Product adding success');
                    getList();
                } else {
                    if (data.redirect) {
                        window.location.href = data.redirect;
                    } else {
                        $.toast(data.errMsg);
                    }
                }
            }
        });
    });

    $('.category-wrap').on('click', '.row-product-category.temp .delete', function () {
        console.log($(this).parent().parent());
        $(this).parent().parent().remove();
    });

    $('.category-wrap').on('click', '.row-product-category.now .delete', function (e) {
        var target = e.currentTarget;
        $.confirm('Are you sure to delete it?', function () {
            $.ajax({
                url: deleteCategoryUrl,
                type: 'POST',
                data: {
                    productCategoryId: target.dataset.id
                },
                dataType: 'json',
                success: function(data) {
                    if (data.success) {
                        $.toast("Category has been delete successfully");
                        getList();
                    } else {
                        if (data.redirect) {
                            window.location.href = data.redirect;
                        } else {
                            $.toast(data.errMsg);
                        }
                    }
                }
            });
        });
    });
});