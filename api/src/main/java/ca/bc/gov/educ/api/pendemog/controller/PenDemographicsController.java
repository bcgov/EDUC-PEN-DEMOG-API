package ca.bc.gov.educ.api.pendemog.controller;

import ca.bc.gov.educ.api.pendemog.endpoint.PenDemographicsEndpoint;
import ca.bc.gov.educ.api.pendemog.exception.InvalidParameterException;
import ca.bc.gov.educ.api.pendemog.filter.FilterOperation;
import ca.bc.gov.educ.api.pendemog.filter.PenDemogFilterSpecs;
import ca.bc.gov.educ.api.pendemog.mappers.PenDemographicsMapper;
import ca.bc.gov.educ.api.pendemog.model.PenDemographicsEntity;
import ca.bc.gov.educ.api.pendemog.service.PenDemographicsService;
import ca.bc.gov.educ.api.pendemog.struct.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * The type Pen demographics controller.
 */
@RestController
@EnableResourceServer
@Slf4j
public class PenDemographicsController implements PenDemographicsEndpoint {
  @Getter(AccessLevel.PRIVATE)
  private final PenDemographicsService penDemographicsService;
  private static final PenDemographicsMapper mapper = PenDemographicsMapper.mapper;
  
  @Getter(AccessLevel.PRIVATE)
  private final PenDemogFilterSpecs penDemogFilterSpecs;

  /**
   * Instantiates a new Pen demographics controller.
   *
   * @param penDemographicsService the pen demographics service
   * @param penDemogFilterSpecs    the pen demog filter specs
   */
  @Autowired
  public PenDemographicsController(final PenDemographicsService penDemographicsService, PenDemogFilterSpecs penDemogFilterSpecs) {
    this.penDemographicsService = penDemographicsService;
    this.penDemogFilterSpecs = penDemogFilterSpecs;
  }

  @Override
  public PenDemographics getPenDemographicsByPen(String pen) {
    log.debug("Retrieving Pen Data");
    pen = StringUtils.rightPad(pen, 10);
    return mapper.toStructure(getPenDemographicsService().getPenDemographicsByPen(pen));
  }

  @Override
  public List<PenDemographics> searchPenDemographics(String studSurName, String studGiven, String studMiddle, String studBirth, String studSex) {
    validateDateOfBirth(studBirth);
    return getPenDemographicsService().searchPenDemographics(studSurName, studGiven, studMiddle, studBirth, studSex).stream().map(mapper::toStructure).collect(Collectors.toList());
  }

  @Override
  public CompletableFuture<Page<PenDemographics>> findAll(Integer pageNumber, Integer pageSize, String sortCriteriaJson, String searchCriteriaListJson) {
    final ObjectMapper objectMapper = new ObjectMapper();
    final List<Sort.Order> sorts = new ArrayList<>();
    Specification<PenDemographicsEntity> penRegBatchSpecs = null;
    try {
      getSortCriteria(sortCriteriaJson, objectMapper, sorts);
      if (StringUtils.isNotBlank(searchCriteriaListJson)) {
        List<Search> searches =  objectMapper.readValue(searchCriteriaListJson, new TypeReference<>() {
        });
        int i =0;
        for(var search: searches){
          penRegBatchSpecs = getSpecifications(penRegBatchSpecs, i, search);
          i++;
        }

      }
    } catch (JsonProcessingException e) {
      throw new InvalidParameterException(e.getMessage());
    }
    return getPenDemographicsService().findAll(penRegBatchSpecs, pageNumber, pageSize, sorts).thenApplyAsync(penDemographicsEntities -> penDemographicsEntities.map(mapper::toStructure));
  }

  private Specification<PenDemographicsEntity> getSpecifications(Specification<PenDemographicsEntity> penDemographicsEntitySpecification, int i, Search search) {
    if(i==0){
      penDemographicsEntitySpecification = getPenDemographicsEntitySpecification(search.getSearchCriteriaList());
    }else {
      if(search.getCondition() == Condition.AND){
        penDemographicsEntitySpecification = penDemographicsEntitySpecification.and(getPenDemographicsEntitySpecification(search.getSearchCriteriaList()));
      }else {
        penDemographicsEntitySpecification = penDemographicsEntitySpecification.or(getPenDemographicsEntitySpecification(search.getSearchCriteriaList()));
      }
    }
    return penDemographicsEntitySpecification;
  }
  private void getSortCriteria(String sortCriteriaJson, ObjectMapper objectMapper, List<Sort.Order> sorts) throws JsonProcessingException {
    if (StringUtils.isNotBlank(sortCriteriaJson)) {
      Map<String, String> sortMap = objectMapper.readValue(sortCriteriaJson, new TypeReference<>() {
      });
      sortMap.forEach((k, v) -> {
        if ("ASC".equalsIgnoreCase(v)) {
          sorts.add(new Sort.Order(Sort.Direction.ASC, k));
        } else {
          sorts.add(new Sort.Order(Sort.Direction.DESC, k));
        }
      });
    }
  }

  private Specification<PenDemographicsEntity> getPenDemographicsEntitySpecification(List<SearchCriteria> criteriaList) {
    Specification<PenDemographicsEntity> penRequestBatchEntitySpecification = null;
    if (!criteriaList.isEmpty()) {
      int i = 0;
      for (SearchCriteria criteria : criteriaList) {
        if (criteria.getKey() != null && criteria.getOperation() != null && criteria.getValueType() != null) {
          Specification<PenDemographicsEntity> typeSpecification = getTypeSpecification(criteria.getKey(), criteria.getOperation(), criteria.getValue(), criteria.getValueType());
          penRequestBatchEntitySpecification = getSpecificationPerGroup(penRequestBatchEntitySpecification, i, criteria, typeSpecification);
          i++;
        } else {
          throw new InvalidParameterException("Search Criteria can not contain null values for key, operation and value type");
        }
      }
    }
    return penRequestBatchEntitySpecification;
  }

  private Specification<PenDemographicsEntity> getSpecificationPerGroup(Specification<PenDemographicsEntity> penDemographicsEntitySpecification, int i, SearchCriteria criteria, Specification<PenDemographicsEntity> typeSpecification) {
    if (i == 0) {
      penDemographicsEntitySpecification = Specification.where(typeSpecification);
    } else {
      if(criteria.getCondition() == Condition.AND){
        penDemographicsEntitySpecification = penDemographicsEntitySpecification.and(typeSpecification);
      }else {
        penDemographicsEntitySpecification = penDemographicsEntitySpecification.or(typeSpecification);
      }
    }
    return penDemographicsEntitySpecification;
  }

  private Specification<PenDemographicsEntity> getTypeSpecification(String key, FilterOperation filterOperation, String value, ValueType valueType) {
    Specification<PenDemographicsEntity> penDemographicsEntitySpecification = null;
    if (valueType == ValueType.STRING) {
      penDemographicsEntitySpecification = getPenDemogFilterSpecs().getStringTypeSpecification(key, value, filterOperation);
    }
    return penDemographicsEntitySpecification;
  }
  private void validateDateOfBirth(String studBirth) {
    if (StringUtils.isNotBlank(studBirth)) {
      try {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        dateFormat.setLenient(false);
        dateFormat.parse(studBirth);
      } catch (final ParseException ex) {
        throw new InvalidParameterException(studBirth, "Student  Date of Birth should be in yyyyMMdd format.");
      }
    }
  }

}
