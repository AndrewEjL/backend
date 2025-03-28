package e_was.backend.Model.User;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_donor")
public class UserDonor extends MyUser{
    @Column(name = "reward_points", nullable = false)
    @JsonProperty("reward_points")
    private double rewardPoints;

    public double getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(double rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

   
}
