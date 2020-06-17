package zuoyang.o2o.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;

@Getter
@Setter
public class ImageHolder {
    private String imageName;
    private InputStream image;

    public ImageHolder(){}

    public ImageHolder(String imageName, InputStream image) {
        this.imageName = imageName;
        this.image = image;
    }
}
