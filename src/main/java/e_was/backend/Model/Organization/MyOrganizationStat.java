package e_was.backend.Model.Organization;

import java.sql.Time;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "organization_stats")
public class MyOrganizationStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int ID;

    @Column(name = "organization_id")
    @JsonProperty("organization_id")
    private int organizationID;

    @Column(name = "collected")
    @JsonProperty("collected")
    private double collected;

    @Column(name = "processed")
    @JsonProperty("processed")
    private double processed;

    @Column(name = "recycled")
    @JsonProperty("recycled")
    private double recycled;

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

    public int getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(int organizationID) {
        this.organizationID = organizationID;
    }

    public double getCollected() {
        return collected;
    }

    public void setCollected(double collected) {
        this.collected = collected;
    }

    public double getProcessed() {
        return processed;
    }

    public void setProcessed(double processed) {
        this.processed = processed;
    }

    public double getRecycled() {
        return recycled;
    }

    public void setRecycled(double recycled) {
        this.recycled = recycled;
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
