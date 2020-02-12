package ca.bc.gov.educ.api.pendemog.mappers;

import ca.bc.gov.educ.api.pendemog.model.PenDemographicsEntity;
import ca.bc.gov.educ.api.pendemog.struct.PenDemographics;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
@SuppressWarnings("squid:S1214")
public interface PenDemographicsMapper {
  PenDemographicsMapper mapper = Mappers.getMapper(PenDemographicsMapper.class);


  @Mapping(dateFormat = "yyyyMMdd", target = "createDate")
  @Mapping(dateFormat = "yyyyMMdd", target = "studBirth")
  @Mapping(target = "pen", source = "studNo")
  PenDemographics toStructure(PenDemographicsEntity penDemographicsEntity);

  @Mapping(dateFormat = "yyyy-MM-dd", target = "createDate")
  @Mapping(dateFormat = "yyyy-MM-dd", target = "studBirth")
  @Mapping(target = "studNo", source = "pen")
  PenDemographicsEntity toModel(PenDemographics penDemographics);
}
