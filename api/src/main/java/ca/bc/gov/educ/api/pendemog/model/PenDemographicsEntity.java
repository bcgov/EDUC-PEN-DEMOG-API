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
@Table(name = "PEN_DEMOG")
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
  private Date studBirth;

  @Column(name = "STUD_SEX")
  private String studSex;

  @Column(name = "STUD_STATUS")
  private String studStatus;

  @Column(name = "CREATE_DATE")
  private Date createDate;

  @Column(name = "CREATE_USER_NAME")
  private String createUserName;

}
