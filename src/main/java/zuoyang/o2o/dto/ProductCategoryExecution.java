package zuoyang.o2o.dto;

import lombok.Getter;
import lombok.Setter;
import zuoyang.o2o.entity.ProductCategory;
import zuoyang.o2o.enums.ProductCategoryStateEnum;

import java.util.List;

@Getter
@Setter
public class ProductCategoryExecution {
    private int state;
    private String stateInfo;
    private List<ProductCategory> productCategoryList;

    public ProductCategoryExecution() {
    }

    /**
     *
     * @param productCategoryStateEnum
     */
    public ProductCategoryExecution(ProductCategoryStateEnum productCategoryStateEnum) {
        this.state = productCategoryStateEnum.getState();
        this.stateInfo = productCategoryStateEnum.getStateInfo();
    }

    /**
     *
     * @param productCategoryStateEnum
     * @param productCategoryList
     */
    public ProductCategoryExecution(ProductCategoryStateEnum productCategoryStateEnum,
                                    List<ProductCategory> productCategoryList) {
        this.state = productCategoryStateEnum.getState();
        this.stateInfo = productCategoryStateEnum.getStateInfo();
        this.productCategoryList = productCategoryList;
    }
}
