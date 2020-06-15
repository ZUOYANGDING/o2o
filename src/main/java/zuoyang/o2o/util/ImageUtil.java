package zuoyang.o2o.util;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import zuoyang.o2o.dto.ImageHolder;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Slf4j
public class ImageUtil {
    private static String basePath = PathUtil.getImageBasePath();
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random random = new Random();

    /**
     * Transfer the CommonsMultipartFile, which is fileItem in source code, to classic java file
     * @param thumbnails
     * @return
     */
    public static File transformCommonsMultipartFileToClassicFile(CommonsMultipartFile thumbnails) {
        File classicFile = new File(thumbnails.getOriginalFilename());
        try {
            thumbnails.transferTo(classicFile);
        } catch (IOException e) {
            log.error(e.toString());
        }
        return classicFile;
    }

    /**
     * deal with uploaded image, return the relative file path of the image stored
     * @param imageHolder
     * @param targetPath
     * @return
     */
    public static String generateThumbnail(ImageHolder imageHolder, String targetPath) {
        String fileName = getRandomFileName();
        String extension = getFileExtension(imageHolder.getImageName());

        // create the directory to store the image if it does not exist
        makeDirPath(targetPath);
        // create relative file path with fileName and file extension
        String relativePath = targetPath + fileName + extension;
        log.debug("current relative file path: " + relativePath);
        // create complete file path to store the image
        File destFile = new File(basePath + relativePath);
        log.debug("current complete file path: " + destFile);

        try {
            // TODO path of the watermark should be rewrite when deploy the project
            Thumbnails.of(imageHolder.getImage()).size(200, 200).
                    watermark(Positions.BOTTOM_RIGHT,
                            ImageIO.read(new File("/Users/zuoyangding/IdeaProjects/o2o/src/main/resources/watermark.jpg")),
                            0.25f).
                    outputQuality(0.9f).toFile(destFile);
        } catch (Exception e) {
            log.error(e.toString());
            log.error("watermarkpath: " + basePath +"/watermark.jpg");
            // remove the directory for shop
            String createdDirectory = basePath + targetPath;
            removeCreatedDirectory(createdDirectory);
            throw new RuntimeException("failed to create image" + e.toString());
        }
        return relativePath;
    }


    /**
     * deal with uploaded image, return the relative file path of the image stored
     * only different to shop thumbnail is, that when create file failed do not delete the directory,
     * since the directory belongs to shop, not the product
     * @param imageHolder
     * @param targetPath
     * @return
     */
    // TODO might can be rewrite generateThumbnail and this function into overload format
    public static String generateThumbnailForProduct(ImageHolder imageHolder, String targetPath) {
        String fileName = getRandomFileName();
        String extension = getFileExtension(imageHolder.getImageName());

        // create the directory to store the image if it does not exist
        makeDirPath(targetPath);
        // create relative file path with fileName and file extension
        String relativePath = targetPath + fileName + extension;
        log.debug("current relative file path: " + relativePath);
        // create complete file path to store the image
        File destFile = new File(basePath + relativePath);
        log.debug("current complete file path: " + destFile);

        try {
            // TODO path of the watermark should be rewrite when deploy the project
            Thumbnails.of(imageHolder.getImage()).size(200, 200).
                    watermark(Positions.BOTTOM_RIGHT,
                            ImageIO.read(new File("/Users/zuoyangding/IdeaProjects/o2o/src/main/resources/watermark.jpg")),
                            0.25f).
                    outputQuality(0.9f).toFile(destFile);
        } catch (Exception e) {
            log.error(e.toString());
            log.error("watermarkpath: " + basePath +"/watermark.jpg");
            throw new RuntimeException("failed to create image" + e.toString());
        }
        return relativePath;
    }

    /**
     * create random fileName
     * @return
     */
    public static String getRandomFileName() {
        int randomNumber = random.nextInt(89999) + 10000;
        String currentTime = simpleDateFormat.format(new Date());
        return randomNumber + currentTime;
    }

    /**
     * get the suffix extension of the unloaded file
     * @param fileName
     * @return
     */
    public static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * create directories for target file. If we need store as /Users/userName/foo/foo1/foofile.jpg
     * the directories foo and foo1 should be created here
     * @param targetPath
     */
    public static void makeDirPath(String targetPath) {
        String fileDirtPath = basePath + targetPath;
        File dirPath = new File(fileDirtPath);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
    }

    /**
     * delete the created directory when store image failed
     * @param createdDirectory
     */
    public static void removeCreatedDirectory(String createdDirectory) {
        log.info("delete the created directory: " + createdDirectory);
        File rDirectory = new File(createdDirectory);
        if (rDirectory.exists()) {
            if (rDirectory.isDirectory()) {
                for (File f : rDirectory.listFiles()) {
                    f.delete();
                }
            }
            rDirectory.delete();
        }
    }

    /**
     * delete the file or directory for modify frontend object
     * @param path
     */
    public static void deleteFileOrDirectory(String path) {
        log.info("file or directory to delete: " + basePath + path);
        File fileOrDirectory = new File(basePath + path);
        if (fileOrDirectory.exists()) {
            if (fileOrDirectory.isDirectory()) {
                for (File f : fileOrDirectory.listFiles()) {
                    f.delete();
                }
            }
            fileOrDirectory.delete();
        }
    }

    public static void main(String[] args) throws IOException {
        Thumbnails.of(new File("/Users/zuoyangding/work/image/fgo.jpg")).size(200, 200).
                watermark(Positions.BOTTOM_RIGHT,
                        ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f).
                outputQuality(0.8f).toFile("/Users/zuoyangding/work/image/fgonew.jpg");
    }
}
