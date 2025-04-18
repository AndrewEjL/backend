package e_was.backend.Model.StatusModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "cities")
public class Cities extends MyStatus {
    @Column(name = "states_type")
    private int statesType;

    public int getStatesType() {
        return statesType;
    }

    public void setStatesType(int statesType) {
        this.statesType = statesType;
    }
}
