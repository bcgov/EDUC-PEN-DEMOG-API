package ca.bc.gov.educ.api.pendemog.controller;

import ca.bc.gov.educ.api.pendemog.endpoint.PenDemographicsEndpoint;
import ca.bc.gov.educ.api.pendemog.mappers.PenDemographicsMapper;
import ca.bc.gov.educ.api.pendemog.service.PenDemographicsService;
import ca.bc.gov.educ.api.pendemog.struct.PenDemographics;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableResourceServer
@Slf4j
public class PenDemographicsController implements PenDemographicsEndpoint {

  @Getter(AccessLevel.PRIVATE)
  private final PenDemographicsService penDemographicsService;
  private static final PenDemographicsMapper mapper = PenDemographicsMapper.mapper;

  @Autowired
  public PenDemographicsController(final PenDemographicsService penDemographicsService) {
    this.penDemographicsService = penDemographicsService;
  }

  @Override
  public PenDemographics getPenDemographicsByPen(String pen) {
    log.debug("Retrieving Pen Data");
    return mapper.toStructure(getPenDemographicsService().getPenDemographicsByPen(pen));
  }

  @Override
  public String health() {
    log.trace("Health check OK, Returning OK.");
    return "OK";
  }
}
