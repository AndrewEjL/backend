package e_was.backend.Model.PickupTransaction;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pickup_transaction")
public class MyTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pickup_transaction_id")
    @JsonProperty("pickup_transaction_id")
    private int pickupTransactionID;

    @Column(name = "pickup_item_id")
    @JsonProperty("pickup_item_id")
    private int pickupItemID;

    @Column(name = "user_donor_id")
    @JsonProperty("user_donor_id")
    private int userDonorID;

    @Column(name = "user_recipient_id")
    @JsonProperty("user_recipient_id")
    private int userRecipientID;
    
    @Column(name = "organization_id")
    @JsonProperty("organization_id")
    private int organizationID;

    @Column(name = "pickup_status_id")
    @JsonProperty("pickup_status_id")
    private int pickupStatusID;

    @Column(name = "weight")
    @JsonProperty("weight")
    private double weight;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete = false;

    @Column(name = "delete_date")
    private Timestamp deleteDate;

    @Column(name = "is_update", nullable = false)
    private Boolean isUpdate = false;

    @Column(name = "update_date")
    private Timestamp updateDate;

    public int getPickupTransactionID() {
        return pickupTransactionID;
    }

    public void setPickupTransactionID(int pickupTransactionID) {
        this.pickupTransactionID = pickupTransactionID;
    }

    public int getPickupItemID() {
        return pickupItemID;
    }

    public void setPickupItemID(int pickupItemID) {
        this.pickupItemID = pickupItemID;
    }

    public int getUserRecipientID() {
        return userRecipientID;
    }

    public void setUserRecipientID(int userRecipientID) {
        this.userRecipientID = userRecipientID;
    }

    public int getPickupStatusID() {
        return pickupStatusID;
    }

    public int getUserDonorID() {
        return userDonorID;
    }

    public void setUserDonorID(int userDonorID) {
        this.userDonorID = userDonorID;
    }

    public void setPickupStatusID(int pickupStatusID) {
        this.pickupStatusID = pickupStatusID;
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

    public int getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(int organizationID) {
        this.organizationID = organizationID;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    
}
