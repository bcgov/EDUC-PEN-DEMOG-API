package ca.bc.gov.educ.api.pendemog.repository;

import ca.bc.gov.educ.api.pendemog.model.PenDemographicsEntity;

import java.util.Date;
import java.util.List;

public interface PenDemographicsRepositoryCustom {
  List<PenDemographicsEntity> searchPenDemographics(String studSurName, String studGiven, String studMiddle, Date studBirth, String studSex);
}
