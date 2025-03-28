package e_was.backend.Model.User;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name ="user_recipient")
public class UserRecipient extends MyUser{
    @Column(name = "organization_id", nullable = false)
    @JsonProperty("organization_id")
    private int organizationID;

    public int getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(int organization_id) {
        this.organizationID = organization_id;
    }
}
