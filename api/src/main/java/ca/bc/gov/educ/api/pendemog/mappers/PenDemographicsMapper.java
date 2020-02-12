package ca.bc.gov.educ.api.pendemog.mappers;

import ca.bc.gov.educ.api.pendemog.model.PenDemographicsEntity;
import ca.bc.gov.educ.api.pendemog.struct.PenDemographics;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PenDemographicsMapper {
  PenDemographicsMapper mapper = Mappers.getMapper(PenDemographicsMapper.class);

  PenDemographicsEntity toModel(PenDemographics penDemographics);

  PenDemographics toStructure(PenDemographicsEntity penDemographicsEntity);
}
