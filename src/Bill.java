import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Bill implements Serializable {
    private Map<String, Integer> boughtProducts;

    public Bill() {
        boughtProducts = new HashMap<>();
    }

    public void addPurchasedProduct(String productName){
        if(!boughtProducts.containsKey(productName)){
            boughtProducts.put(productName, 1);
            return;
        }
        boughtProducts.put(productName, boughtProducts.get(productName) + 1);
    }

    public Map<String, Integer> getBoughtProducts() {
        return boughtProducts;
    }

    public void setBoughtProducts(Map<String, Integer> boughtProducts) {
        this.boughtProducts = boughtProducts;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "wantedProducts=" + boughtProducts +
                '}';
    }
}
