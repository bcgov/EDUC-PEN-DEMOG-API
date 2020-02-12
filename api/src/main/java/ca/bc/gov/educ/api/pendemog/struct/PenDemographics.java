package ca.bc.gov.educ.api.pendemog.struct;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PenDemographics {
  private String pen;
  private String studSurname;
  private String studGiven;
  private String studMiddle;
  private String usualSurname;
  private String usualGiven;
  private String usualMiddle;
  private String studBirth;
  private String studSex;
  private String studDemogCode;
  private String studStatus;
  private String penLocalId;
  private String penMincode;
  private String postal;
  private String studTrueNo;
  private String mergeToUserName;
  private String mergeToCode;
  private Date createDate;
  private String createUserName;
  private Date updateDate;
  private String updateUserName;
  private String studGrade;
  private Integer studGradeYear;
  private Date updateDemogDate;
  private Date mergeToDate;
}
