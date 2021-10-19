import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

public class Product implements Serializable {
    private final ReentrantLock productMutex;

    private String name;
    private int price;
    private int quantity;


    public Product(String name, int price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.productMutex = new ReentrantLock();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean decreaseQuantity() {
        this.productMutex.lock();
        if (this.quantity <= 0) {
            this.productMutex.unlock();
            return false;
        } else {
            this.quantity--;
            this.productMutex.unlock();
            return true;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Product{" + '\n' +
                "name='" + name + '\'' + '\n' +
                ", price=" + price + '\n' +
                ", quantity=" + quantity + '\n' +
                '}';
    }
}

