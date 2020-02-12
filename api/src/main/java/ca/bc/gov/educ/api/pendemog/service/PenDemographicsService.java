package ca.bc.gov.educ.api.pendemog.service;

import ca.bc.gov.educ.api.pendemog.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.pendemog.model.PenDemographicsEntity;
import ca.bc.gov.educ.api.pendemog.repository.PenDemographicsRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PenDemographicsService {

  @Getter(AccessLevel.PRIVATE)
  private final PenDemographicsRepository penDemographicsRepository;

  @Autowired
  public PenDemographicsService(final PenDemographicsRepository penDemographicsRepository) {
    this.penDemographicsRepository = penDemographicsRepository;
  }

  public PenDemographicsEntity getPenDemographicsByPen(String pen) {
    val result = getPenDemographicsRepository().findByPen(pen);
    if (result.isPresent()) {
      return result.get();
    }
    throw new EntityNotFoundException(PenDemographicsEntity.class, "pen", pen);
  }
}
