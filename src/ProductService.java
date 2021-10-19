import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProductService {
    private final static int THREAD_COUNT = 100;
    private final static int PRODUCT_PRICE_LIMIT = 1000;
    private final static int PRODUCT_QUANTITY_LIMIT = 10000;
    private final static int NUMBER_OF_CHECKS = 10;

    private final List<String> PRODUCT_NAMES = List.of(
            "Bread", "Milk", "Sausage", "Bacon", "Cheese", "Oreo", "Lion");

    private final Random random;

    public ProductService() {
        this.random = new Random();
    }

    public void run() {
        InventoryRepository repository = new InventoryRepository();
        generateRepository(repository);
        InventoryRepository initialRepositoryCopy = (InventoryRepository) Copy.deepCopy(repository);

        List<Thread> threads = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        for (int numberOfRuns = 0; numberOfRuns < NUMBER_OF_CHECKS; numberOfRuns++) {
            for (int i = 0; i < THREAD_COUNT; i++) {
                ProductRunnable productRunnable = new ProductRunnable(repository, PRODUCT_NAMES);
                Thread thread = new Thread(productRunnable);
                threads.add(thread);
                thread.start();
            }


            for (Thread thread : threads) {
                try {
                    thread.join(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            checkBillsAndInventory(repository, initialRepositoryCopy);

        }

        checkBillsAndInventory(repository, initialRepositoryCopy);

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - startTime;
        System.out.println(timeElapsed);

//        System.out.println(repository);
    }

    private void generateRepository(InventoryRepository repository) {
        for (String productName : PRODUCT_NAMES) {
            int productPrice = random.nextInt(PRODUCT_PRICE_LIMIT);
            int productQuantity = random.nextInt(PRODUCT_QUANTITY_LIMIT);
            Product product = new Product(productName, productPrice, productQuantity);
            repository.addProduct(product);
        }
    }

    private void checkBillsAndInventory(InventoryRepository repository,
                                        InventoryRepository initialRepository) {
        InventoryRepository auxiliaryRepository = (InventoryRepository) Copy.deepCopy(repository);
        for (Bill bill : auxiliaryRepository.getBillList()) {
            for (String boughtProduct : bill.getBoughtProducts().keySet()) {
                auxiliaryRepository.restockProduct(boughtProduct, bill.getBoughtProducts().get(boughtProduct));
            }
        }

        for (String productInInventory : auxiliaryRepository.getInventory().keySet()) {
            if (auxiliaryRepository.getInventory().get(productInInventory).getQuantity() != initialRepository.getInventory().get(productInInventory).getQuantity()) {
                throw new RuntimeException(String.format("Product quantities do NOT match! %s %s - %s %s",
                        productInInventory,
                        auxiliaryRepository.getInventory().get(productInInventory).getQuantity(),
                        productInInventory,
                        initialRepository.getInventory().get(productInInventory).getQuantity()));
            }
        }

//        System.out.println("Final Profit:");
//        repository.calculateSales();
    }


}
