import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProductRunnable implements Runnable {

    private final Random random;
    private final InventoryRepository repository;
    private final List<String> namesOfProducts;
    private int numberOfRuns;

    public ProductRunnable(InventoryRepository repository, List<String> namesOfProducts) {
        this.repository = repository;
        this.namesOfProducts = namesOfProducts;
        random = new Random();
        this.numberOfRuns = 1;
    }

    @Override
    public void run() {
        for (int runNumber = 0; runNumber < numberOfRuns; runNumber++) {
            List<WantedProduct> wantedProductList = generateListOfWantedProducts();
            List<String> boughtItems = new ArrayList<>();
            for (WantedProduct wantedProduct : wantedProductList) {
                for (int i = 0; i < wantedProduct.getQuantity(); i++) {
                    Boolean transactionSucceeded =
                            this.repository.buyProduct(wantedProduct.getProductName());
                    if (transactionSucceeded) {
                        boughtItems.add(wantedProduct.getProductName());
                    } else {

                        break;
                    }
                }
            }
            if (boughtItems.size() > 0) {
                this.createAndAddBillOfBoughtItems(boughtItems);
            }
        }
    }

    private void createAndAddBillOfBoughtItems(List<String> boughtProducts) {
        Bill bill = new Bill();
        for (String boughtProduct : boughtProducts) {
            bill.addPurchasedProduct(boughtProduct);
        }
        this.repository.addBill(bill);
    }

    private List<WantedProduct> generateListOfWantedProducts() {
        List<WantedProduct> wantedProductList = new ArrayList<>();

        for (String productName : namesOfProducts) {
            WantedProduct wantedProduct =
                    new WantedProduct(productName, random.nextInt(10));
            wantedProductList.add(wantedProduct);
        }

        return wantedProductList;
    }
}
