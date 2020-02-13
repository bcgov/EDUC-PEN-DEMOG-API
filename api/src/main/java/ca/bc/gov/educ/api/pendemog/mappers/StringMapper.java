package ca.bc.gov.educ.api.pendemog.mappers;

import org.apache.commons.lang3.StringUtils;

public class StringMapper {

  public String map(String value) {
    if (StringUtils.isNotBlank(value)) {
      return value.trim();
    }
    return value;
  }
}
