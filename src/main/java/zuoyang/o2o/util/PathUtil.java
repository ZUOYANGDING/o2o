package zuoyang.o2o.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PathUtil {
    private static String separator = System.getProperty("file.separator");
    private static String winPath;
    private static String linuxPath;
    private static String shopPath;

    // set up base path in windows os
    @Value("${win_base_path}")
    public void setWinPath() {
        PathUtil.winPath = winPath;
    }
    // set up base path in linux os
    @Value("${linux_base_path}")
    public void setLinuxPath() {
        PathUtil.linuxPath=linuxPath;
    }
    // set up relevant path for shop
    @Value("${shop_relevant_path}")
    public void setShopPath() {
        PathUtil.shopPath = shopPath;
    }

    public static String getImageBasePath() {
        String os = System.getProperty("os.name");
        String basePath = "";
        if (os.toLowerCase().startsWith("win")) {
            basePath = winPath;
        } else {
            basePath = linuxPath;
        }
        basePath = basePath.replace("/", separator);
        return basePath;
    }

    public static String getShopImagePath(Long shopId) {
        String shopImagePath = shopPath + shopId + separator;
        return shopImagePath.replace("/", separator);
    }
}
