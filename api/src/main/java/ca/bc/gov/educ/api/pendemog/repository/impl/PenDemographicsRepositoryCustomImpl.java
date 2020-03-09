package ca.bc.gov.educ.api.pendemog.repository.impl;

import ca.bc.gov.educ.api.pendemog.model.PenDemographicsEntity;
import ca.bc.gov.educ.api.pendemog.repository.PenDemographicsRepositoryCustom;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PenDemographicsRepositoryCustomImpl implements PenDemographicsRepositoryCustom {

  @Getter(AccessLevel.PRIVATE)
  private final EntityManager entityManager;

  @Autowired
  PenDemographicsRepositoryCustomImpl(final EntityManager em) {
    this.entityManager = em;
  }

  @Override
  public List<PenDemographicsEntity> searchPenDemographics(String studSurName, String studGiven, String studMiddle, String studBirth, String studSex) {
    final List<Predicate> predicates = new ArrayList<>();
    final CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
    final CriteriaQuery<PenDemographicsEntity> criteriaQuery = criteriaBuilder.createQuery(PenDemographicsEntity.class);
    Root<PenDemographicsEntity> penDemographicsEntityRoot = criteriaQuery.from(PenDemographicsEntity.class);
    predicates.add(criteriaBuilder.equal(penDemographicsEntityRoot.get("studSurname"), studSurName.toUpperCase()));
    predicates.add(criteriaBuilder.equal(penDemographicsEntityRoot.get("studGiven"), studGiven.toUpperCase()));
    predicates.add(criteriaBuilder.equal(penDemographicsEntityRoot.get("studMiddle"), studMiddle.toUpperCase()));
    predicates.add(criteriaBuilder.equal(penDemographicsEntityRoot.get("studBirth"), studBirth));
    predicates.add(criteriaBuilder.equal(penDemographicsEntityRoot.get("studSex"), studSex.toUpperCase()));
    criteriaQuery.where(predicates.toArray(new Predicate[0]));
    return entityManager.createQuery(criteriaQuery).setMaxResults(50).getResultList();
  }
}
