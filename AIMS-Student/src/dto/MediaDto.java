package dto;

public class MediaDto {
    protected int id;
    protected String title;
    private String category;
    protected long price;
    protected long value;
    protected long quantity;
    protected String type;
    protected String imageURL;

    public MediaDto(String title, String type, String category, long value, long price, long quantity, String imageURL) {
        this.title = title;
        this.value = value;
        this.price = price;
        this.quantity = quantity;
        this.type = type;
        this.category = category;
        this.imageURL = imageURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
