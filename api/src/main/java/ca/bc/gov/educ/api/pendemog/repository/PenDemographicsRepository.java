package ca.bc.gov.educ.api.pendemog.repository;

import ca.bc.gov.educ.api.pendemog.model.PenDemographicsEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface Pen demographics repository.
 */
@Repository
public interface PenDemographicsRepository extends CrudRepository<PenDemographicsEntity, String>, PenDemographicsRepositoryCustom, JpaSpecificationExecutor<PenDemographicsEntity> {
  /**
   * Find by stud no optional.
   *
   * @param pen the pen
   * @return the optional
   */
  Optional<PenDemographicsEntity> findByStudNo(String pen);
}
