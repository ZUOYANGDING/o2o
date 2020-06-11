$(function () {
    var categoryListUrl = '/o2o/shopadmin/getproductcategorylist';
    var addCategoryUrl = '/o2o/shopadmin/addproductcategory';
    var deleteCategoryUrl = '/o2o/shopadmin/deleteproductcategory';
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
});