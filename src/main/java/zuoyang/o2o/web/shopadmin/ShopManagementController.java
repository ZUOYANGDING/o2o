package zuoyang.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import zuoyang.o2o.dto.ShopExecution;
import zuoyang.o2o.entity.PersonInfo;
import zuoyang.o2o.entity.Shop;
import zuoyang.o2o.enums.ShopStateEnum;
import zuoyang.o2o.exception.ShopOperationException;
import zuoyang.o2o.service.ShopService;
import zuoyang.o2o.util.HttpServletRequestUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController {
    private final ShopService shopService;

    public ShopManagementController(ShopService shopService) {
        this.shopService = shopService;
    }

    @PostMapping("/registershop")
    @ResponseBody
    public Map<String, Object> registerShop(HttpServletRequest request) {
        // get shop info from frontend, transfer it into shop entity, use jackson-databind
        Map<String, Object> modelMap = new HashMap<>();
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
            //TODO apply session to get user
            PersonInfo user = new PersonInfo();
            user.setUserId(1L);
            shop.setPersonInfo(user);
            ShopExecution shopExecution = null;
            try {
                // the source code of getOriginalFileName will return "" if fileItem.getName() is null
                String originalFileName = shopImg.getOriginalFilename();
                if (originalFileName.isEmpty()) {
                    throw new RuntimeException("the original file name is empty");
                }
                shopExecution = shopService.addShop(shop, shopImg.getInputStream(), originalFileName);
                if (shopExecution.getState() == ShopStateEnum.CHECK.getState()) {
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
            modelMap.put("errMsg", "Input the shop info please");
            return modelMap;
        }
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
