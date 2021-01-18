package sample;

public class Product {
    private int ProductId;
    private String Title;
    private double Price;
    private String UpdatedDate;
    private int Discount;

    public Product(int productId, String title, double price, String updatedDate, int discount) {
        ProductId = productId;
        Title = title;
        Price = price;
        UpdatedDate = updatedDate;
        Discount = discount;
    }

    public int getProductId() {
        return ProductId;
    }

    public String getTitle() {
        return Title;
    }

    public double getPrice() {
        return Price;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public int getDiscount() {
        return Discount;
    }

}
