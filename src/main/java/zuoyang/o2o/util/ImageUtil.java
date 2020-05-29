package zuoyang.o2o.util;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
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
     * @param classicFile
     * @param targetPath
     * @return
     */
    public static String generateThumbnail(File classicFile, String targetPath) {
        String fileName = getRandomFileName();
        String extension = getFileExtension(classicFile);

        // create the directory to store the image if it does not exist
        makeDirPath(targetPath);
        // create relative file path with fileName and file extension
        String relativePath = targetPath + fileName + extension;
        log.debug("current relative file path: " + relativePath);
        // create complete file path to store the image
        File destFile = new File(basePath + relativePath);
        log.debug("current complete file path: " + destFile);

        try {
            // path of the watermark should be rewrite when deploy the project
            Thumbnails.of(classicFile).size(200, 200).
                    watermark(Positions.BOTTOM_RIGHT,
                            ImageIO.read(new File("/Users/zuoyangding/IdeaProjects/o2o/src/main/resources/watermark.jpg")),
                            0.25f).
                    outputQuality(0.9f).toFile(destFile);
        } catch (Exception e) {
            log.error(e.toString());
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
     * @param classicFile
     * @return
     */
    public static String getFileExtension(File classicFile) {
        String originFileName = classicFile.getName();
        return originFileName.substring(originFileName.lastIndexOf("."));
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

    public static void main(String[] args) throws IOException {
        Thumbnails.of(new File("/Users/zuoyangding/work/image/fgo.jpg")).size(200, 200).
                watermark(Positions.BOTTOM_RIGHT,
                        ImageIO.read(new File(basePath + "/watermark.jpg")), 0.25f).
                outputQuality(0.8f).toFile("/Users/zuoyangding/work/image/fgonew.jpg");
    }
}
