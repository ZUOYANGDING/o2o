package zuoyang.o2o.dto;

import lombok.Getter;
import lombok.Setter;
import zuoyang.o2o.entity.Product;
import zuoyang.o2o.enums.ProductStateEnum;

import java.util.List;

@Getter
@Setter
public class ProductExecution {
    private int state;
    private String stateInfo;
    private Product product;
    private List<Product> productList;

    public ProductExecution(){}

    public ProductExecution(ProductStateEnum productStateEnum) {
        this.state = productStateEnum.getState();
        this.stateInfo = productStateEnum.getStateInfo();
    }

    /**
     * search operation for product
     * @param productStateEnum
     * @param product
     */
    public ProductExecution(ProductStateEnum productStateEnum, Product product) {
        this.state = productStateEnum.getState();
        this.stateInfo = productStateEnum.getStateInfo();
        this.product = product;
    }

    /**
     * search operation for product list
     * @param productStateEnum
     * @param productList
     */
    public ProductExecution(ProductStateEnum productStateEnum, List<Product> productList) {
        this.state = productStateEnum.getState();
        this.stateInfo = productStateEnum.getStateInfo();
        this.productList = productList;
    }
}
