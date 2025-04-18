package e_was.backend.Model.Rewards;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "rewards")
public class MyRewards {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rewards_id")
    @JsonProperty("rewards_id")
    private int rewardsID;

    @Column(name = "rewards_name")
    @JsonProperty("rewards_name")
    private String rewardsName;

    @Column(name = "rewards_image")
    @JsonProperty("rewards_image")
    private String rewardsImage;

    @Column(name = "rewards_type")
    @JsonProperty("rewards_type")
    private int rewardsType;

    @Column(name = "rewards_status")
    @JsonProperty("rewards_status")
    private int rewardsStatus;

    @Column(name = "description")
    @JsonProperty("description")
    private String description;

    @Column(name = "point_needed")
    @JsonProperty("point_needed")
    private int pointNeeded;

    @Column(name = "quantity")
    @JsonProperty("quantity")
    private int quantity;

    @Column(name = "created_date", nullable = false)
    private Timestamp createdDate;

    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete = false;

    @Column(name = "delete_date")
    private Timestamp deleteDate;

    @Column(name = "is_update", nullable = false)
    private Boolean isUpdate = false;

    @Column(name = "update_date")
    private Timestamp updateDate;

    public int getRewardsID() {
        return rewardsID;
    }

    public void setRewardsID(int rewardsID) {
        this.rewardsID = rewardsID;
    }

    public String getRewardsName() {
        return rewardsName;
    }

    public void setRewardsName(String rewardsName) {
        this.rewardsName = rewardsName;
    }

    public String getRewardsImage() {
        return rewardsImage;
    }

    public void setRewardsImage(String rewardsImage) {
        this.rewardsImage = rewardsImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPointNeeded() {
        return pointNeeded;
    }

    public void setPointNeeded(int point_needed) {
        this.pointNeeded = point_needed;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
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

    public int getRewardsType() {
        return rewardsType;
    }

    public void setRewardsType(int rewardsType) {
        this.rewardsType = rewardsType;
    }
    
    public int getRewardsStatus() {
        return rewardsStatus;
    }

    public void setRewardsStatus(int rewardsStatus) {
        this.rewardsStatus = rewardsStatus;
    }
}
