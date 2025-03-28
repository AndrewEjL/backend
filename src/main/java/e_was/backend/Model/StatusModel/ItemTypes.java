package e_was.backend.Model.StatusModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name ="item_types")
public class ItemTypes extends MyStatus{
    @Column(name = "item_type_points_gain")
    private double pointsGain;

    public double getPointsGain() {
        return pointsGain;
    }

    public void setPointsGain(double points_gain) {
        this.pointsGain = points_gain;
    }
}

