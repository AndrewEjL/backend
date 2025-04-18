package e_was.backend.Model.UserRewards;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_donor_rewards")
public class MyUserRewards {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonProperty("id")
    private int ID;

    @Column(name = "user_donor_id")
    @JsonProperty("user_donor_id")
    private int userDonorID;

    @Column(name = "rewards_id")
    @JsonProperty("rewards_id")
    private int rewardsID;

    @Column(name = "rewards_pin")
    @JsonProperty("rewards_pin")
    private String rewardsPin;

    @Column(name = "rewards_use")
    @JsonProperty("rewards_use")
    private Boolean rewardsUse;

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

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public int getUserDonorID() {
        return userDonorID;
    }

    public void setUserDonorID(int userDonorID) {
        this.userDonorID = userDonorID;
    }

    public int getRewardsID() {
        return rewardsID;
    }

    public void setRewardsID(int rewardsID) {
        this.rewardsID = rewardsID;
    }

    public String getRewardsPin() {
        return rewardsPin;
    }

    public void setRewardsPin(String rewardsPin) {
        this.rewardsPin = rewardsPin;
    }

    public Boolean getRewardsUse() {
        return rewardsUse;
    }

    public void setRewardsUse(Boolean rewardsUse) {
        this.rewardsUse = rewardsUse;
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

    
}
