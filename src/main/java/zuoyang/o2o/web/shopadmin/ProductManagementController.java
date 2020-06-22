package zuoyang.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import zuoyang.o2o.dto.ImageHolder;
import zuoyang.o2o.dto.ProductExecution;
import zuoyang.o2o.entity.Product;
import zuoyang.o2o.entity.ProductCategory;
import zuoyang.o2o.entity.Shop;
import zuoyang.o2o.enums.ProductStateEnum;
import zuoyang.o2o.exception.ProductOperationException;
import zuoyang.o2o.service.ProductCategoryService;
import zuoyang.o2o.service.ProductService;
import zuoyang.o2o.util.CodeUtil;
import zuoyang.o2o.util.HttpServletRequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/shopadmin")
public class ProductManagementController {
    private final ProductService productService;
    private final ProductCategoryService productCategoryService;
    private static final int MAX_IMAGE_UPLOAD = 6;

    public ProductManagementController(ProductService productService, ProductCategoryService productCategoryService) {
        this.productService = productService;
        this.productCategoryService = productCategoryService;
    }

    @GetMapping("/getproductbyid")
    @ResponseBody
    private Map<String, Object> getProductById(@RequestParam Long productId) {
        Map<String, Object> modelMap = new HashMap<>();
        if (productId >= 0) {
            try {
                Product product = productService.getProductInfo(productId);
                List<ProductCategory> productCategoryList;
                if (product == null) {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", "failed to get product info");
                    return modelMap;
                } else {
                    productCategoryList = productCategoryService.getProductCategoryList(product.getShop().getShopId());
                }
                modelMap.put("success", true);
                modelMap.put("product", product);
                modelMap.put("productCategoryList", productCategoryList);
                return modelMap;
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", "failed to get product info " + e.getMessage());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "productId is null");
            return modelMap;
        }
    }

    @GetMapping("/getproductlistbyshop")
    @ResponseBody
    private Map<String, Object> getProductListByShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        int pageIndex = HttpServletRequestUtil.getInteger(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInteger(request, "pageSize");
        if (pageIndex>-1 && pageSize>0 && currentShop!=null && currentShop.getShopId()!=null) {
            Product productCondition = new Product();
            long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
            String productName = HttpServletRequestUtil.getString(request, "productName");
            compactProductInfo(productCondition, currentShop, productName, productCategoryId);
            try {
                ProductExecution productExecution =
                        productService.getProductList(productCondition, pageIndex, pageSize);
                if (productExecution.getState() == ProductStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                    modelMap.put("productList", productExecution.getProductList());
                    modelMap.put("productCount", productExecution.getCount());
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", productExecution.getStateInfo());
                    modelMap.put("redirect", false);
                }
                return modelMap;
            } catch (ProductOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
                modelMap.put("redirect", false);
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("redirect", true);
            modelMap.put("url", "/o2o/shopadmin/shoplist");
            return modelMap;
        }
    }

    @PostMapping("/addproduct")
    @ResponseBody
    private Map<String, Object> addProduct(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        // check verification code
        Boolean checkCode = CodeUtil.checkVerifyCode(request);
        if (!checkCode) {
            modelMap.put("redirect", false);
            modelMap.put("success", false);
            modelMap.put("errMsg", "Verify Code does not match");
            return modelMap;
        }

        ObjectMapper mapper = new ObjectMapper();
        ImageHolder thumbnail = new ImageHolder();
        List<ImageHolder> productDetailImgList = new ArrayList<>();
        Product product;
        CommonsMultipartResolver commonsMultipartResolver =
                new CommonsMultipartResolver(request.getSession().getServletContext());

        // get product basic info from frontend
        try {
            String productStr = HttpServletRequestUtil.getString(request, "productStr");
            product = mapper.readValue(productStr, Product.class);
        } catch (Exception e) {
            modelMap.put("redirect", false);
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        // get product thumbnail and detail images, and transfer them to ImageHolder
        try {
            if (commonsMultipartResolver.isMultipart(request)) {
                MultipartHttpServletRequest multipartHttpServletRequest  = (MultipartHttpServletRequest) request;

                // get the size of image file stream from frontend, 0 means null image stream from frontend
                int multipartFilesSize = multipartHttpServletRequest.getMultiFileMap().size();
                if (multipartFilesSize > 1) {
                    getImages(thumbnail, productDetailImgList,
                            multipartHttpServletRequest, multipartFilesSize);
                } else {
                    throw new ProductOperationException("Missing thumbnail or detail image for product adding");
                }
            }
        } catch (Exception e) {
            modelMap.put("redirect", false);
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        // store product and images into backend database
        if (product!=null && thumbnail.getImage()!=null && productDetailImgList.size()>0) {
            Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
            if (currentShop!=null && currentShop.getShopId()!=null) {
                try {
                    product.setShop(currentShop);
                    ProductExecution productExecution =
                            productService.addProduct(product, thumbnail, productDetailImgList);
                    if (productExecution.getState() == ProductStateEnum.SUCCESS.getState()) {
                        modelMap.put("success", true);
                    } else {
                        modelMap.put("success", false);
                        modelMap.put("redirect", false);
                        modelMap.put("errMsg", productExecution.getStateInfo());
                    }
                    return modelMap;
                } catch (ProductOperationException e) {
                    modelMap.put("success", false);
                    modelMap.put("redirect", false);
                    modelMap.put("errMsg", e.getMessage());
                    return modelMap;
                }
            } else {
                modelMap.put("success", false);
                modelMap.put("redirect", true);
                modelMap.put("url", "/o2o/shopadmin/shoplist");
                return modelMap;
            }
        } else {
            modelMap.put("redirect", false);
            modelMap.put("success", false);
            modelMap.put("errMsg", "Please input all required information for product");
            return modelMap;
        }
    }

    @PostMapping("modifyproduct")
    @ResponseBody
    private Map<String, Object> modifyProduct(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        // check if its ban product or modify
        boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
        // check verification code
        boolean checkCode = CodeUtil.checkVerifyCode(request);
        if (!statusChange && !checkCode) {
            modelMap.put("redirect", false);
            modelMap.put("success", false);
            modelMap.put("errMsg", "Verify Code does not match");
            return modelMap;
        }

        ObjectMapper mapper = new ObjectMapper();
        ImageHolder thumbnail = new ImageHolder();
        List<ImageHolder> productDetailImgList = new ArrayList<>();
        Product product;
        CommonsMultipartResolver commonsMultipartResolver =
                new CommonsMultipartResolver(request.getSession().getServletContext());

        // get basic info from frontend
        try {
            String productStr = HttpServletRequestUtil.getString(request, "productStr");
            product = mapper.readValue(productStr, Product.class);
        } catch (Exception e) {
            modelMap.put("redirect", false);
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        // get product thumbnail and detail images, and transfer them to ImageHolder if need to change them
        try {
            if (commonsMultipartResolver.isMultipart(request)) {
                MultipartHttpServletRequest multipartHttpServletRequest  = (MultipartHttpServletRequest) request;

                // get the size of image file stream from frontend, 0 means null image stream from frontend
                int multipartFilesSize = multipartHttpServletRequest.getMultiFileMap().size();
                if (multipartFilesSize > 0) {
                    getImages(thumbnail, productDetailImgList,
                            multipartHttpServletRequest, multipartFilesSize);
                } else {
                    log.info("no update for thumbnail or detail image");
                }
            }
        } catch (Exception e) {
            modelMap.put("redirect", false);
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        // store product and images into backend database
        if (product != null && product.getProductId()!=null) {
            Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
            if (currentShop!=null && currentShop.getShopId()!=null) {
                try {
                    product.setShop(currentShop);
                    ProductExecution productExecution =
                            productService.modifyProduct(product, thumbnail, productDetailImgList);
                    if (productExecution.getState() == ProductStateEnum.SUCCESS.getState()) {
                        modelMap.put("success", true);
                    } else {
                        modelMap.put("success", false);
                        modelMap.put("redirect", false);
                        modelMap.put("errMsg", productExecution.getStateInfo());
                    }
                    return modelMap;
                } catch (ProductOperationException e) {
                    modelMap.put("success", false);
                    modelMap.put("redirect", false);
                    modelMap.put("errMsg", e.getMessage());
                    return modelMap;
                }
            } else {
                modelMap.put("success", false);
                modelMap.put("redirect", true);
                modelMap.put("url", "/o2o/shopadmin/shoplist");
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("redirect", false);
            modelMap.put("errMsg", "empty product or no matching product for update");
            return modelMap;
        }
    }


    /**
     *  deal with thumbnail and detail image
     * @param thumbnail
     * @param productDetailImgList
     * @param multipartHttpServletRequest
     * @param multipartFilesSize
     * @throws IOException
     * @throws ProductOperationException
     */
    private void getImages(ImageHolder thumbnail, List<ImageHolder> productDetailImgList,
                           MultipartHttpServletRequest multipartHttpServletRequest, int multipartFilesSize)
            throws IOException, ProductOperationException {
        // get thumbnail file
        CommonsMultipartFile thumbnailImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("thumbnail");
        if (thumbnailImg != null) {
            thumbnail.setImage(thumbnailImg.getInputStream());
            String originalFileName = thumbnailImg.getOriginalFilename();
            if (originalFileName.isEmpty()){
                throw new ProductOperationException("the thumbnail file name is empty");
            }
            thumbnail.setImageName(originalFileName);
            // thumbnail file has been got, size--
            multipartFilesSize--;
        } else {
            throw new ProductOperationException("the thumbnail is empty");
        }

        // get detail image file, max number is 6
        for (int i=0; i<multipartFilesSize; i++) {
            // prevent frontend stream provide null/bad detail image back
            CommonsMultipartFile detailImg =
                    (CommonsMultipartFile) multipartHttpServletRequest.getFile("productImg" + i);
            ImageHolder tempImageHolder = new ImageHolder();
            if (detailImg != null) {
                tempImageHolder.setImage(detailImg.getInputStream());
                String originalFileName = detailImg.getOriginalFilename();
                if (originalFileName.isEmpty()) {
                    throw new ProductOperationException("the detail image " + i + " file name is empty");
                }
                tempImageHolder.setImageName(originalFileName);
                productDetailImgList.add(tempImageHolder);
            } else {
                throw new ProductOperationException("the detail image " + i +" is empty");
            }
        }
    }

    /**
     * compact all search restriction for product
     * @param productCondition
     * @param currentShop
     * @param productName
     * @param productCategoryId
     */
    private void compactProductInfo(Product productCondition, Shop currentShop,
                                    String productName, long productCategoryId) {
        productCondition.setShop(currentShop);
        if (productCategoryId > -1) {
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }
        if (productName != null) {
            productCondition.setProductName(productName);
        }
    }
}
