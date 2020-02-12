package ca.bc.gov.educ.api.pendemog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Immutable
@Table(name = "PEN_DEMOG_VW")
public class PenDemographicsEntity {

  @Id
  @Column(name = "STUD_NO")
  private String studNo;

  @Column(name = "STUD_SURNAME")
  private String studSurname;

  @Column(name = "STUD_GIVEN")
  private String studGiven;

  @Column(name = "STUD_MIDDLE")
  private String studMiddle;

  @Column(name = "USUAL_SURNAME")
  private String usualSurname;

  @Column(name = "USUAL_GIVEN")
  private String usualGiven;

  @Column(name = "USUAL_MIDDLE")
  private String usualMiddle;

  @Column(name = "STUD_BIRTH")
  private String studBirth;

  @Column(name = "STUD_SEX")
  private String studSex;

  @Column(name = "STUD_DEMOG_CODE")
  private String studDemogCode;

  @Column(name = "STUD_STATUS")
  private String studStatus;

  @Column(name = "PEN_LOCAL_ID")
  private String penLocalId;

  @Column(name = "PEN_MINCODE")
  private String penMincode;

  @Column(name = "POSTAL")
  private String postal;

  @Column(name = "STUD_TRUE_NO")
  private String studTrueNo;

  @Column(name = "MERGE_TO_USER_NAME")
  private String mergeToUserName;

  @Column(name = "MERGE_TO_CODE")
  private String mergeToCode;

  @Column(name = "CREATE_DATE")
  private Date createDate;

  @Column(name = "CREATE_USER_NAME")
  private String createUserName;

  @Column(name = "UPDATE_DATE")
  private Date updateDate;

  @Column(name = "UPDATE_USER_NAME")
  private String updateUserName;

  @Column(name = "STUD_GRADE")
  private String studGrade;

  @Column(name = "STUD_GRADE_YEAR")
  private Integer studGradeYear;

  @Column(name = "UPDATE_DEMOG_DATE")
  private Date updateDemogDate;

  @Column(name = "MERGE_TO_DATE")
  private Date mergeToDate;

}
