package zuoyang.o2o.util;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.beans.factory.annotation.Value;
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
    private static String waterMarkPath = PathUtil.getWaterMarkPath();

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
     * deal with uploaded thumbnail, return the relative file path of the image stored
     * @param imageHolder
     * @param targetPath
     * @return
     */
    public static String generateThumbnail(ImageHolder imageHolder, String targetPath) {
        String fileName = getRandomFileName();
        String extension = getFileExtension(imageHolder.getImageName());

        // create the directory to store the image if it does not exist
        boolean create = makeDirPath(targetPath);
        // create relative file path with fileName and file extension
        String relativePath = targetPath + fileName + extension;
        log.debug("current relative file path: " + relativePath);
        // create complete file path to store the image
        File destFile = new File(basePath + relativePath);
        log.debug("current complete file path: " + destFile);

        try {
            Thumbnails.of(imageHolder.getImage()).size(200, 200).
                    watermark(Positions.BOTTOM_RIGHT,
                            ImageIO.read(new File(waterMarkPath)),
                            0.25f).
                    outputQuality(0.9f).toFile(destFile);
        } catch (Exception e) {
            log.error(e.toString());
            log.error("basePath: " + basePath);
            log.error("watermarkpath: " + waterMarkPath);
            // remove the directory for shop
            if (create) {
                String createdDirectory = basePath + targetPath;
                removeCreatedDirectory(createdDirectory);
            }
            throw new RuntimeException("failed to create image" + e.toString());
        }
        return relativePath;
    }

    /**
     * deal with upload detail image
     * @param imageHolder
     * @param targetPath
     * @return
     */
    public static String generateProductDetailImage(ImageHolder imageHolder, String targetPath) {
        String fileName = getRandomFileName();
        String extension = getFileExtension(imageHolder.getImageName());

        // make directory if it does not exit
        boolean create = makeDirPath(targetPath);
        // create relative path
        String relativePath = targetPath + fileName + extension;
        log.debug("current relative file path: " + relativePath);
        // create complete file path to store the image
        File destFile = new File(basePath + relativePath);
        log.debug("current complete file path: " + destFile);
        try {
            Thumbnails.of(imageHolder.getImage()).size(337, 640).
                    watermark(Positions.BOTTOM_RIGHT,
                            ImageIO.read(new File(waterMarkPath)),
                            0.25f).outputQuality(0.9f).toFile(destFile);
        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("basePath: " + basePath);
            log.error("watermarkpath: " + waterMarkPath);
            if (create) {
                String createdDirectory = basePath + targetPath;
                removeCreatedDirectory(createdDirectory);
            }
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
    public static boolean makeDirPath(String targetPath) {
        String fileDirtPath = basePath + targetPath;
        File dirPath = new File(fileDirtPath);
        boolean create = false;
        if (!dirPath.exists()) {
            dirPath.mkdirs();
            create = true;
        }
        return create;
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
