package ca.bc.gov.educ.api.pendemog.filter;

import ca.bc.gov.educ.api.pendemog.model.PenDemographicsEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * The type Pen reg batch filter specs.
 */
@Service
@Slf4j
public class PenDemogFilterSpecs {

  private final FilterSpecifications<PenDemographicsEntity, String> stringFilterSpecifications;
  private final Converters converters;

  /**
   * Instantiates a new Pen reg batch filter specs.
   *
   * @param stringFilterSpecifications   the string filter specifications
   * @param converters                   the converters
   */
  public PenDemogFilterSpecs(FilterSpecifications<PenDemographicsEntity, String> stringFilterSpecifications,  Converters converters) {
    this.stringFilterSpecifications = stringFilterSpecifications;
    this.converters = converters;
  }


  /**
   * Gets string type specification.
   *
   * @param fieldName       the field name
   * @param filterValue     the filter value
   * @param filterOperation the filter operation
   * @return the string type specification
   */
  public Specification<PenDemographicsEntity> getStringTypeSpecification(String fieldName, String filterValue, FilterOperation filterOperation) {
    return getSpecification(fieldName, filterValue, filterOperation, converters.getFunction(String.class), stringFilterSpecifications);
  }


  private <T extends Comparable<T>> Specification<PenDemographicsEntity> getSpecification(String fieldName,
                                                                                          String filterValue,
                                                                                          FilterOperation filterOperation,
                                                                                          Function<String, T> converter,
                                                                                          FilterSpecifications<PenDemographicsEntity, T> specifications) {
    FilterCriteria<T> criteria = new FilterCriteria<>(fieldName, filterValue, filterOperation, converter);
    return specifications.getSpecification(criteria.getOperation()).apply(criteria);
  }
}
