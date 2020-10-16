package ca.bc.gov.educ.api.pendemog.endpoint;

import ca.bc.gov.educ.api.pendemog.struct.PenDemographics;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * The interface Pen demographics endpoint.
 */
@RequestMapping("/")
@OpenAPIDefinition(info = @Info(title = "API for Pen Demographics.", description = "This Read API is for Reading demographics data of a student in BC from open vms system.", version = "1"), security = {@SecurityRequirement(name = "OAUTH2", scopes = {"READ_PEN_DEMOGRAPHICS"})})
public interface PenDemographicsEndpoint {

  /**
   * Gets pen demographics by pen.
   *
   * @param pen the pen
   * @return the pen demographics by pen
   */
  @GetMapping("/{pen}")
  @PreAuthorize("#oauth2.hasScope('READ_PEN_DEMOGRAPHICS')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "404", description = "NOT FOUND.")})
  PenDemographics getPenDemographicsByPen(@PathVariable String pen);

  /**
   * Search pen demographics list.
   *
   * @param studSurname the stud surname
   * @param studGiven   the stud given
   * @param studMiddle  the stud middle
   * @param studBirth   the stud birth
   * @param studSex     the stud sex
   * @return the list
   */
  @GetMapping
  @PreAuthorize("#oauth2.hasScope('READ_PEN_DEMOGRAPHICS')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK")})
  List<PenDemographics> searchPenDemographics(@RequestParam(name="studSurName", defaultValue = " ") String studSurname,
                                              @RequestParam(name="studGiven", defaultValue = " ") String studGiven,
                                              @RequestParam(name="studMiddle", defaultValue = " ") String studMiddle,
                                              @RequestParam(name="studBirth", defaultValue = " ") String studBirth,
                                              @RequestParam(name="studSex", defaultValue = " ") String studSex);

  /**
   * Find all pen demographics given by  search criteria.
   *
   * @param pageNumber             the page number
   * @param pageSize               the page size
   * @param sortCriteriaJson       the sort criteria json
   * @param searchCriteriaListJson the search criteria list json
   * @return the completable future
   */
  @GetMapping("/paginated")
  @Async
  @PreAuthorize("#oauth2.hasScope('READ_PEN_DEMOGRAPHICS')")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK"), @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR.")})
  @Transactional(readOnly = true)
  @Tag(name = "Endpoint to support queries with sort, filter and pagination.", description = "This API endpoint exposes flexible way to query the entity by leveraging JPA specifications.")
  CompletableFuture<Page<PenDemographics>> findAll(@RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
                                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                   @RequestParam(name = "sort", defaultValue = "") String sortCriteriaJson,
                                                   @ArraySchema(schema = @Schema( name = "searchCriteriaList",
                                                       description = "searchCriteriaList if provided should be a JSON string of Search Array",
                                                       implementation = ca.bc.gov.educ.api.pendemog.struct.Search.class))
                                                   @RequestParam(name = "searchCriteriaList", required = false) String searchCriteriaListJson);



}
