package e_was.backend.Respository.Organization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import e_was.backend.Model.Organization.MyOrganizationStat;

@Repository
public interface OrganizationStatsRepository extends JpaRepository<MyOrganizationStat, Integer> {

}
