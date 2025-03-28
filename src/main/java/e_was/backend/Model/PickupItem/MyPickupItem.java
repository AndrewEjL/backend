package e_was.backend.Model.PickupItem;

import jakarta.persistence.Table;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@Table(name = "pickup_items")
public class MyPickupItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pickup_items_id")
    @JsonProperty("pickup_items_id")
    private int pickupItemID;

    @Column(name = "user_donor_id")
    @JsonProperty("user_donor_id")
    private int userDonorID;

    @Column(name = "item_name", nullable = false)
    @JsonProperty("item_name")
    private String itemName;

    @Column(name = "item_type_id")
    @JsonProperty("item_type_id")
    private int itemTypeID;

    @Column(name = "device_condition_id")
    @JsonProperty("device_condition_id")
    private int deviceConditionID;
    
    @Column(name = "dimension_length")
    @JsonProperty("dimension_length")
    private double dimensionLength;

    @Column(name = "dimension_width")
    @JsonProperty("dimension_width")
    private double dimensionWidth;

    @Column(name = "dimension_height")
    @JsonProperty("dimension_height")
    private double dimensionHeight;

    @Column(name = "pickup_location")
    @JsonProperty("pickup_location")
    private String pickupLocation;

    @Column(name = "item_status_id")
    @JsonProperty("item_status_id")
    private int itemStatusID;

    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete = false;

    @Column(name = "delete_date")
    private Timestamp deleteDate;

    @Column(name = "is_update", nullable = false)
    private Boolean isUpdate = false;

    @Column(name = "update_date")
    private Timestamp updateDate;

    public int getPickupItemID() {
        return pickupItemID;
    }

    public void setPickupItemID(int pickupItemID) {
        this.pickupItemID = pickupItemID;
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

    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    public Timestamp getDeleteDate() {
        return deleteDate;
    }

    public void setDeleteDate(Timestamp deleteDate) {
        this.deleteDate = deleteDate;
    }

    public Boolean getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(Boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public int getItemStatusID() {
        return itemStatusID;
    }

    public void setItemStatusID(int itemStatusID) {
        this.itemStatusID = itemStatusID;
    }

    
}
