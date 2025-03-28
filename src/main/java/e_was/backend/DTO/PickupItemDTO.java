package e_was.backend.DTO;

public class PickupItemDTO {
    private int userDonorID;
    private String itemName;
    private int itemTypeID;
    private int deviceConditionID;
    private double dimensionLength;
    private double dimensionWidth;
    private double dimensionHeight;
    private String pickupLocation;
    private int quantity;
   
    public int getItemTypeID() {
        return itemTypeID;
    }

    public void setItemTypeID(int itemTypeID) {
        this.itemTypeID = itemTypeID;
    }

    public int getDeviceConditionID() {
        return deviceConditionID;
    }

    public void setDeviceConditionID(int deviceConditionID) {
        this.deviceConditionID = deviceConditionID;
    }

    public double getDimensionLength() {
        return dimensionLength;
    }

    public void setDimensionLength(double dimensionLength) {
        this.dimensionLength = dimensionLength;
    }

    public double getDimensionWidth() {
        return dimensionWidth;
    }

    public void setDimensionWidth(double dimensionWidth) {
        this.dimensionWidth = dimensionWidth;
    }

    public double getDimensionHeight() {
        return dimensionHeight;
    }

    public void setDimensionHeight(double dimensionHeight) {
        this.dimensionHeight = dimensionHeight;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getUserDonorID() {
        return userDonorID;
    }

    public void setUserDonorID(int userDonorID) {
        this.userDonorID = userDonorID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    
}
