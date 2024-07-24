package ca.uwindsor.appliedcomputing.final_project.dto;

public class ValidationData {

    private String productName;
    private String price;
    private String imageUrl;
    private String productUrl;
    private String productDescription;
    private String category;
    private String store;

    private boolean isValidName;
    private boolean isValidPrice;
    private boolean isValidImageUrl;
    private boolean isValidProductUrl;
    private boolean isValidDescription;
    private boolean isValidCategory;
    private boolean isValidStore;

    // Getters and setters

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public boolean isValidName() {
        return isValidName;
    }

    public void setValidName(boolean validName) {
        isValidName = validName;
    }

    public boolean isValidPrice() {
        return isValidPrice;
    }

    public void setValidPrice(boolean validPrice) {
        isValidPrice = validPrice;
    }

    public boolean isValidImageUrl() {
        return isValidImageUrl;
    }

    public void setValidImageUrl(boolean validImageUrl) {
        isValidImageUrl = validImageUrl;
    }

    public boolean isValidProductUrl() {
        return isValidProductUrl;
    }

    public void setValidProductUrl(boolean validProductUrl) {
        isValidProductUrl = validProductUrl;
    }

    public boolean isValidDescription() {
        return isValidDescription;
    }

    public void setValidDescription(boolean validDescription) {
        isValidDescription = validDescription;
    }

    public boolean isValidCategory() {
        return isValidCategory;
    }

    public void setValidCategory(boolean validCategory) {
        isValidCategory = validCategory;
    }

    public boolean isValidStore() {
        return isValidStore;
    }

    public void setValidStore(boolean validStore) {
        isValidStore = validStore;
    }
}

