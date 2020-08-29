package ca.bc.gov.educ.api.pendemog.service;

import ca.bc.gov.educ.api.pendemog.exception.EntityNotFoundException;
import ca.bc.gov.educ.api.pendemog.model.PenDemographicsEntity;
import ca.bc.gov.educ.api.pendemog.repository.PenDemographicsRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * The type Pen demographics service.
 */
@Service
public class PenDemographicsService {

  @Getter(AccessLevel.PRIVATE)
  private final PenDemographicsRepository penDemographicsRepository;

  /**
   * Instantiates a new Pen demographics service.
   *
   * @param penDemographicsRepository the pen demographics repository
   */
  @Autowired
  public PenDemographicsService(final PenDemographicsRepository penDemographicsRepository) {
    this.penDemographicsRepository = penDemographicsRepository;
  }

  /**
   * Gets pen demographics by pen.
   *
   * @param pen the pen
   * @return the pen demographics by pen
   */
  public PenDemographicsEntity getPenDemographicsByPen(String pen) {
    val result = getPenDemographicsRepository().findByStudNo(pen);
    if (result.isPresent()) {
      return result.get();
    }
    throw new EntityNotFoundException(PenDemographicsEntity.class, "pen", pen);
  }

  /**
   * Search pen demographics list.
   *
   * @param studSurName the stud sur name
   * @param studGiven   the stud given
   * @param studMiddle  the stud middle
   * @param studBirth   the stud birth
   * @param studSex     the stud sex
   * @return the list
   */
  public List<PenDemographicsEntity> searchPenDemographics(String studSurName, String studGiven, String studMiddle, String studBirth, String studSex) {
    return getPenDemographicsRepository().searchPenDemographics(studSurName, studGiven, studMiddle, studBirth, studSex);
  }

  /**
   * Find all completable future.
   *
   * @param penRegBatchSpecs the pen reg batch specs
   * @param pageNumber       the page number
   * @param pageSize         the page size
   * @param sorts            the sorts
   * @return the completable future
   */
  @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
  public CompletableFuture<Page<PenDemographicsEntity>> findAll(Specification<PenDemographicsEntity> penRegBatchSpecs, Integer pageNumber, Integer pageSize, List<Sort.Order> sorts) {
    Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sorts));
    var result = getPenDemographicsRepository().findAll(penRegBatchSpecs, paging);
    return CompletableFuture.completedFuture(result);
  }
}
