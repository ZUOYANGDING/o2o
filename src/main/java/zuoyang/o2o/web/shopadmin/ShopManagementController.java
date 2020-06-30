package zuoyang.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import zuoyang.o2o.dto.ImageHolder;
import zuoyang.o2o.dto.ShopExecution;
import zuoyang.o2o.entity.Area;
import zuoyang.o2o.entity.PersonInfo;
import zuoyang.o2o.entity.Shop;
import zuoyang.o2o.entity.ShopCategory;
import zuoyang.o2o.enums.ShopStateEnum;
import zuoyang.o2o.exception.ShopOperationException;
import zuoyang.o2o.service.AreaService;
import zuoyang.o2o.service.ShopCategoryService;
import zuoyang.o2o.service.ShopService;
import zuoyang.o2o.util.CodeUtil;
import zuoyang.o2o.util.HttpServletRequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
    private final ShopService shopService;
    private final ShopCategoryService shopCategoryService;
    private final AreaService areaService;

    public ShopManagementController(ShopService shopService, ShopCategoryService shopCategoryService, AreaService areaService) {
        this.shopService = shopService;
        this.shopCategoryService = shopCategoryService;
        this.areaService = areaService;
    }

    /**
     * shop register controller
     * @param request
     * @return
     */
    @PostMapping("/registershop")
    @ResponseBody
    private Map<String, Object> registerShop(HttpServletRequest request) throws ShopOperationException, IOException {
        Map<String, Object> modelMap = new HashMap<>();
        // check the verify code
        Boolean checkVerifyCode = CodeUtil.checkVerifyCode(request);
        if (checkVerifyCode) {
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "Verify Code does not match");
            return modelMap;
        }


        // get json part of shop info from front end use jackson-databind
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        // get shop image from frontend, transfer it into CommonsMultipartFile
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.
                getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "upload image cannot be null");
            return modelMap;
        }

        // register shop
        if (shop!=null && shopImg!=null) {
            // user from frontend when login, right now this controller does not work
            // TODO verify the user has already login
            PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
            shop.setPersonInfo(user);
            ShopExecution shopExecution = null;
            try {
                // the source code of getOriginalFileName will return "" if fileItem.getName() is null
                String originalFileName = shopImg.getOriginalFilename();
                if (originalFileName.isEmpty()) {
                    throw new RuntimeException("the original file name is empty");
                }
                ImageHolder shopImageHolder = new ImageHolder(originalFileName, shopImg.getInputStream());
                shopExecution = shopService.addShop(shop, shopImageHolder);
                if (shopExecution.getState() == ShopStateEnum.CHECK.getState()) {
                    modelMap.put("success", true);
                    // shopList belong to this user
                    @SuppressWarnings("unchecked")
                    List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
                    if (shopList == null) {
                        shopList = new ArrayList<>();
                    }
                    shopList.add(shopExecution.getShop());
                    request.getSession().setAttribute("shopList", shopList);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", shopExecution.getState());
                }
            } catch (ShopOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", shopExecution.getState());
            } catch (IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "Input the shop info please");
            return modelMap;
        }
    }

    /**
     * shop info controller connect with json
     * @return
     */
    @GetMapping("/getshopinitinfo")
    @ResponseBody
    private Map<String , Object> getShopInfo() {
        Map<String, Object> shopInfoMap = new HashMap<>();
        try {
            // get all shop categories in second level (under root level, no shop link to root categories directly)
            List<ShopCategory> shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            List<Area> areaList = areaService.getAreaList();
            shopInfoMap.put("success", true);
            shopInfoMap.put("shopCategoryList", shopCategoryList);
            shopInfoMap.put("areaList", areaList);
        } catch (ShopOperationException e) {
            shopInfoMap.put("success", false);
            shopInfoMap.put("errMsg", e.getMessage());
        }
        return shopInfoMap;
    }

    /**
     *
     * @param request
     * @return
     */
    @GetMapping("/getshopbyid")
    @ResponseBody
    private Map<String, Object> getShopById(HttpServletRequest request) {
        Map<String, Object> shopMap = new HashMap<>();
        Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (shopId > -1) {
            try {
                Shop shop = shopService.getShopById(shopId);
                if (shop != null) {
                    List<Area> areaList = areaService.getAreaList();
                    if (areaList != null) {
                        shopMap.put("success", true);
                        shopMap.put("areaList", areaList);
                        shopMap.put("shop", shop);
                    } else {
                        shopMap.put("success", false);
                        shopMap.put("errMsg", "No area in database");
                    }
                } else {
                    shopMap.put("success", false);
                    shopMap.put("errMsg", "shop does not exist");
                }
            } catch (Exception e) {
                shopMap.put("success", false);
                shopMap.put("errMsg", e.getMessage());
            }
        } else {
            shopMap.put("success", false);
            shopMap.put("errMsg", "empty shopId");
        }
        return shopMap;
    }

    /**
     *
     * @param request
     * @return
     * @throws ShopOperationException
     * @throws IOException
     */
    @PostMapping("/modifyshop")
    @ResponseBody
    private Map<String, Object> modifyShop(HttpServletRequest request) throws ShopOperationException, IOException {
        Map<String, Object> modelMap = new HashMap<>();
        // check verification code
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "Verify Code does not match");
            return modelMap;
        }

        // get shop info in JSON from frontend
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper objectMapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = objectMapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }

        // deal with new photo if need
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver =
                new CommonsMultipartResolver(request.getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }

        //update shop info
        if (shop!=null && shop.getShopId()!=null) {
            // TODO verify the person's identity
            ShopExecution shopExecution = null;
            try {
                if (shopImg == null) {
                    shopExecution = shopService.modifyShop(shop, null);
                } else {
                    // the source code of getOriginalFileName will return "" if fileItem.getName() is null
                    String originalFileName = shopImg.getOriginalFilename();
                    if (originalFileName.isEmpty()) {
                        throw new RuntimeException("the original file name is empty");
                    }
                    ImageHolder shopImgHolder = new ImageHolder(originalFileName, shopImg.getInputStream());
                    shopExecution = shopService.modifyShop(shop, shopImgHolder);
                }
                if (shopExecution.getState() == ShopStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", shopExecution.getState());
                }
            } catch (ShopOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", shopExecution.getState());
            } catch (IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "No input for shopId");
            return modelMap;
        }
    }


    /**
     *
     * @param request
     * @return
     * @throws ShopOperationException
     */
    @GetMapping("/getshoplist")
    @ResponseBody
    private Map<String, Object> getShopList(HttpServletRequest request) throws ShopOperationException {
        Map<String, Object> modelMap = new HashMap<>();
        //TODO get user from session, after create the login module
        PersonInfo user = new PersonInfo();
        user.setUserId(1L);
        user.setName("test user name");
        try {
            Shop shopCondition = new Shop();
            shopCondition.setPersonInfo(user);
            // create 100 shops is enough to hold personal seller
            ShopExecution shopExecution = shopService.getShopList(shopCondition, 0, 100);
            if (shopExecution.getState() == ShopStateEnum.SUCCESS.getState()) {
                modelMap.put("success", true);
                modelMap.put("shopList", shopExecution.getShopList());
                modelMap.put("user", user);
            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", shopExecution.getState());
            }
        } catch (ShopOperationException e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    /**
     *
     * @param request
     * @return
     * @throws ShopOperationException
     */
    @GetMapping("/getshopmanagementinfo")
    @ResponseBody
    private Map<String, Object> getShopManagementInfo(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if (shopId <= 0) {
            // shopId<=0 means did not get shopId, try to get shopId from currentShop if the user has already login
            Object currentShop = request.getSession().getAttribute("currentShop");
            if (currentShop == null) {
                // if currentShop is null which means illegal operation at frontend, redirect the request
                modelMap.put("redirect", true);
                modelMap.put("url", "/o2o/shopadmin/shoplist");
            } else {
                Shop tempShop = (Shop) currentShop;
                modelMap.put("redirect", false);
                modelMap.put("shopId", tempShop.getShopId());
            }
        } else {
            Shop currentShop = new Shop();
            currentShop.setShopId(shopId);
            // put the shop into session, more convenience for later operation
            request.getSession().setAttribute("currentShop", currentShop);
            modelMap.put("redirect", false);
            modelMap.put("shopId", shopId);
        }
        return modelMap;
    }

    /**
     * depreciated
     * @param fin
     * @param shopImgFile
     */
    private void readInputStreamToFile(InputStream fin, File shopImgFile) {
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(shopImgFile);
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead=fin.read(buffer)) != -1) {
                fout.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            throw new RuntimeException("exception in readInputStreamToFile" + e.getMessage());
        } finally {
            try {
                if (fout != null) {
                    fout.close();
                }
                if (fin != null) {
                    fout.close();
                }
            } catch (Exception e) {
                throw new RuntimeException("IO exception to close in/out file" + e.getMessage());
            }
        }
    }

}
