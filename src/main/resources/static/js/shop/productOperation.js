$(function(){
    var productId = getQueryString('productId');
    var isEdit = productId ? true : false;
    var infoUrl = '/o2o/shopadmin/getproductbyid?productId=' + productId;
    var addProductUrl = '/o2o/shopadmin/addproduct';
    var modifyProductUrl = '/o2o/shopadmin/modifyproduct';
    var categoryUrl = '/o2o/shopadmin/getproductcategorylist'

    if (isEdit) {
        getProductInfo(productId);
    } else {
        getCategory();
    }

    /**
     * get productInfo
     * @param productId
     */
    function getProductInfo(productId) {
        $.getJSON(infoUrl, function(data) {
           if (data.success) {
               var product = data.product;
               var productCategoryList = data.productCategoryList;
               $('#product-name').val(product.productName);
               $('#product-desc').val(product.productDesc);
               $('#priority').val(product.priority);
               $('#normal-price').val(product.normalPrice);
               $('#promotion-price').val(product.promotePrice);

               var categoryHtml = '';
               var selectedCategory = product.productCategory.productCategoryId;
               productCategoryList.map(function (item, index) {
                   var isSelected = selectedCategory === item.productCategoryId ? 'selected' : '';
                   categoryHtml += '<option data-value="' + item.productCategoryId + '"' + isSelected + '>' +
                   item.productCategoryName + '</option>';
               });
               $('#category').html(categoryHtml);
           }
        });
    }


    function getCategory() {
        $.getJSON(categoryUrl, function (data) {
            if (data.success) {
                var productCategoryList = data.productCategoryList;
                var optionHtml ='';
                productCategoryList.map(function (item, index) {
                    optionHtml += '<option data-value="' + item.productCategoryId + '">' + item.productCategoryName +
                        '</option>'
                });
                $('#category').html(optionHtml);
            } else {
                $.toast("Error Message: " + errMsg);
            }
        })
    }

    $('.detail-img-div').on('change', '.detail-img:last-child', function() {
        if ($('.detail-img').length < 6) {
            $('#detail-img').append('<input type="file" class="detail-img">');
        }
    })

    $('#submit').click(function () {
        var product = {};
        if (isEdit) {
            product.productId = productId;
        }

        // basic product info
        product.productName = $('#product-name').val();
        product.productDesc = $('#product-desc').val();
        product.normalPrice = $('#normal-price').val();
        product.promotePrice = $('#promotion-price').val();
        product.priority = $('#priority').val();
        product.productCategory = {
            productCategoryId: $('#category').find('option').not(function () {
                return !this.selected;
            }).data('value')
        }

        // thumbnail for product
        var thumbnail;
        var formData = new FormData();
        if ($('#small-img')[0].files.length>0) {
            thumbnail = $('#small-img')[0].files[0];
            formData.append('thumbnail', thumbnail);
        }

        // detail image for product
        $('.detail-img').map(function(index, item) {
            if ($('.detail-img')[index].files.length>0) {
                formData.append('productImg'+index, $('.detail-img')[index].files[0]);
            }
        });

        formData.append('productStr', JSON.stringify(product));

        // Captcha
        var verifyCodeActual = $('#j_captcha').val();
        if (!verifyCodeActual) {
            $.toast('Input the verification code please!');
            return;
        } else {
            formData.append('verifyCodeActual', verifyCodeActual);
        }

        $.ajax({
            url: (isEdit ? modifyProductUrl : addProductUrl),
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            success: function(data) {
                if (data.success) {
                    $.toast("success submit");
                    $('#captcha_img').click();
                } else {
                    if (data.redirect) {
                        $.toast("failed to submit" +  data.errMsg);
                        window.location.href = data.redirect;
                    } else {
                        $.toast("failed to submit" +  data.errMsg);
                        $('#captcha_img').click();
                    }
                }
            }
        });
    });
});