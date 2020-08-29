package ca.bc.gov.educ.api.pendemog.repository;

import ca.bc.gov.educ.api.pendemog.model.PenDemographicsEntity;

import java.util.List;

/**
 * The interface Pen demographics repository custom.
 */
public interface PenDemographicsRepositoryCustom {
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
  List<PenDemographicsEntity> searchPenDemographics(String studSurName, String studGiven, String studMiddle, String studBirth, String studSex);
}
