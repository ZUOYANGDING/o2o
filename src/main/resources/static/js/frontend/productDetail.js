$(function () {
    var productId = getQueryString('productId');
    var productUrl = '/o2o/frontend/listproductdetailpageinfo?productId=' + productId;

    $.getJSON(productUrl, function (data) {
        if (data.success) {
            var product = data.product;
            // product thumbnail
            $('#product-img').attr('src', getContextPath() + product.imgAddr);
            // product update time
            $('#product-time').text(new Date(product.lastEditTime).Format("yyyy-MM-dd"));
            // product name
            $('#product-name').text(product.productName);
            // product desc
            $('#product-desc').text(product.productDesc);

            if (product.normalPrice!==undefined && product.promotePrice!==undefined) {
                // show normal price and promote price and put a dash on promote price
                $('#price').show();
                $('#normalPrice').html('<del>' + '$' + product.normalPrice
                    + '</del>');
                $('#promotionPrice').text('$' + product.promotePrice);
            } else if (product.normalPrice!==undefined && product.promotePrice===undefined){
                $('#price').show();
                $('#normalPrice').html('<del>' + '$' + product.normalPrice
                    + '</del>');
            } else if (product.normalPrice===undefined && product.promotePrice!==undefined) {
                $('#promotionPrice').text('$' + product.promotePrice);
            }

            var imgListHtml = '';
            product.productImgList.map(function(item, index) {
                imgListHtml += '<div> <img src="'
                    + getContextPath() + item.imgAddress
                    + '" width="100%" /></div>';
            });
            $('#imgList').html(imgListHtml);
        }
    });

    $('#me').click(function () {
        $.openPanel('#panel-right-demo');
    });

    $.init();
})