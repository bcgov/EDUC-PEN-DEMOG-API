package ca.bc.gov.educ.api.pendemog.controller;

import ca.bc.gov.educ.api.pendemog.filter.FilterOperation;
import ca.bc.gov.educ.api.pendemog.mappers.PenDemographicsMapper;
import ca.bc.gov.educ.api.pendemog.repository.PenDemographicsRepository;
import ca.bc.gov.educ.api.pendemog.struct.PenDemographics;
import ca.bc.gov.educ.api.pendemog.struct.Search;
import ca.bc.gov.educ.api.pendemog.struct.SearchCriteria;
import ca.bc.gov.educ.api.pendemog.struct.ValueType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static ca.bc.gov.educ.api.pendemog.struct.Condition.AND;
import static ca.bc.gov.educ.api.pendemog.struct.Condition.OR;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@SuppressWarnings("squid:S00100")
public class PenDemographicsControllerTest {
  @Autowired
  private MockMvc mvc;
  private static final PenDemographicsMapper mapper = PenDemographicsMapper.mapper;
  @Autowired
  PenDemographicsRepository repository;
  @Autowired
  PenDemographicsController controller;

  @Before
  public void setUp() throws IOException {
    MockitoAnnotations.openMocks(this);
    final File file = new File(
        Objects.requireNonNull(getClass().getClassLoader().getResource("PenDemogSampleData.json")).getFile()
    );
    List<PenDemographics> entities = new ObjectMapper().readValue(file, new TypeReference<>() {
    });
    repository.saveAll(entities.stream().map(penDemographics -> {
      if (penDemographics.getStudGiven() != null) {
        penDemographics.setStudGiven(penDemographics.getStudGiven().toUpperCase());
      }
      if (penDemographics.getStudSurname() != null) {
        penDemographics.setStudSurname(penDemographics.getStudSurname().toUpperCase());
      }
      if (penDemographics.getStudMiddle() != null) {
        penDemographics.setStudMiddle(penDemographics.getStudMiddle().toUpperCase());
      }
      if (penDemographics.getStudSex() != null) {
        penDemographics.setStudSex(penDemographics.getStudSex().toUpperCase());
      }
      if (penDemographics.getStudBirth() != null) {
        penDemographics.setStudBirth(penDemographics.getStudBirth().replaceAll("-", ""));
      }
      return mapper.toModel(penDemographics);
    }).collect(Collectors.toList()));
  }

  @After
  public void tearDown() {
    repository.deleteAll();
  }

  @Test
  public void testGetPenDemographicsByPen_GivenPenExistInDB_ShouldReturnStatusOk() throws Exception {

    this.mvc.perform(get("/7613009916")
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "READ_PEN_DEMOGRAPHICS")))
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.pen", is("7613009916")))
        .andExpect(jsonPath("$.studSurname", is("Fratczak".toUpperCase())))
        .andExpect(jsonPath("$.studGiven", is("Jaquelin".toUpperCase())))
        .andExpect(jsonPath("$.studBirth", is("20010519")));

  }

  @Test
  public void testGetPenDemographicsByPen_GivenPenDoesNotExistInDB_ShouldReturnStatusNotFound() throws Exception {

    this.mvc.perform(get("/7613009911")
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "READ_PEN_DEMOGRAPHICS")))
        .accept(MediaType.APPLICATION_JSON)).andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  public void testSearchPenDemographics_GivenNoQueryParameters_ShouldReturnStatusOkWithNoResults() throws Exception {

    this.mvc.perform(get("/")
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "READ_PEN_DEMOGRAPHICS")))
        .accept(MediaType.APPLICATION_JSON)).andDo(print())
        .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
  }


  @Test
  public void testSearchPenDemographics_GivenOnlySexInQueryParam_ShouldReturnStatusOkAndNoResults() throws Exception {

    this.mvc.perform(get("/?studSex=Female")
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "READ_PEN_DEMOGRAPHICS")))
        .accept(MediaType.APPLICATION_JSON)).andDo(print())
        .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  public void testSearchPenDemographics_GivenOnlyDOBInQueryParam_ShouldReturnStatusOk() throws Exception {

    this.mvc.perform(get("/?studBirth=20010519")
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "READ_PEN_DEMOGRAPHICS")))
        .accept(MediaType.APPLICATION_JSON)).andDo(print())
        .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
  }

  @Test
  public void testSearchPenDemographics_GivenWrongFormatDOBInQueryParam_ShouldReturnStatusBadRequest() throws Exception {

    this.mvc.perform(get("/?studBirth=2001-05-19")
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "READ_PEN_DEMOGRAPHICS")))
        .accept(MediaType.APPLICATION_JSON)).andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testSearchPenDemographics2_GivenWrongFormatDOBInQueryParam_ShouldReturnStatusBadRequest() throws Exception {

    this.mvc.perform(get("/?studBirth=00000000")
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "READ_PEN_DEMOGRAPHICS")))
        .accept(MediaType.APPLICATION_JSON)).andDo(print())
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testReadPenDemographicsPaginated_GivenNoSearchCriteria_ShouldReturnStatusOk() throws Exception {
    MvcResult result = this.mvc
        .perform(get("/paginated")
            .with(jwt().jwt((jwt) -> jwt.claim("scope", "READ_PEN_DEMOGRAPHICS")))
            .contentType(APPLICATION_JSON))
        .andReturn();
    this.mvc.perform(asyncDispatch(result)).andDo(print()).andExpect(status().isOk());
  }

  @Test
  public void testReadPenDemographicsPaginated_GivenPageSize100_ShouldReturnStatusOkAndAtMost100Records() throws Exception {
    MvcResult result = this.mvc
        .perform(get("/paginated")
            .with(jwt().jwt((jwt) -> jwt.claim("scope", "READ_PEN_DEMOGRAPHICS")))
            .param("pageSize", "100")
            .contentType(APPLICATION_JSON))
        .andReturn();
    this.mvc.perform(asyncDispatch(result)).andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.content", hasSize(100)));
  }

  @Test
  public void testReadPenDemographicsPaginated_GivenPageNumber_ShouldReturnStatusOkAndPage() throws Exception {
    MvcResult result = this.mvc
        .perform(get("/paginated")
            .with(jwt().jwt((jwt) -> jwt.claim("scope", "READ_PEN_DEMOGRAPHICS")))
            .param("pageNumber", "1")
            .contentType(APPLICATION_JSON))
        .andReturn();
    this.mvc.perform(asyncDispatch(result)).andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.content", hasSize(10))).andExpect(jsonPath("$.pageable.pageNumber", is(1)));
  }

  @Test
  public void testReadPenDemographicsPaginated_GivenSortByStudSurname_ShouldReturnStatusOk() throws Exception {
    Map<String, String> sortMap = new HashMap<>(1);
    sortMap.put("studSurname", "DESC");
    MvcResult result = this.mvc
        .perform(get("/paginated")
            .with(jwt().jwt((jwt) -> jwt.claim("scope", "READ_PEN_DEMOGRAPHICS")))
            .param("sort", new ObjectMapper().writeValueAsString(sortMap))
            .contentType(APPLICATION_JSON))
        .andReturn();
    this.mvc.perform(asyncDispatch(result)).andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.content", hasSize(10))).andExpect(jsonPath("$.sort.sorted", is(true)));
  }

  @Test
  public void testReadPenDemographicsPaginated_givenDifferentFilters_ShouldReturnStatusOk() throws Exception {
    Map<String, String> sortMap = new HashMap<>(1);
    sortMap.put("studSurname", "DESC");
    SearchCriteria criteria = SearchCriteria.builder().key("studSurname").operation(FilterOperation.EQUAL).value("Fratczak".toUpperCase()).valueType(ValueType.STRING).build();
    List<SearchCriteria> criteriaList = new LinkedList<>();
    criteriaList.add(criteria);
    SearchCriteria criteria3 = SearchCriteria.builder().key("studGiven").operation(FilterOperation.EQUAL).value("Jaquelin".toUpperCase()).valueType(ValueType.STRING).build();
    List<SearchCriteria> criteriaList1 = new LinkedList<>();
    criteriaList1.add(criteria3);
    List<Search> searches = new LinkedList<>();
    searches.add(Search.builder().searchCriteriaList(criteriaList).build());
    searches.add(Search.builder().condition(AND).searchCriteriaList(criteriaList1).build());
    ObjectMapper objectMapper = new ObjectMapper();
    String criteriaJSON = objectMapper.writeValueAsString(searches);
    MvcResult result = this.mvc
        .perform(get("/paginated")
            .with(jwt().jwt((jwt) -> jwt.claim("scope", "READ_PEN_DEMOGRAPHICS")))
            .param("sort", new ObjectMapper().writeValueAsString(sortMap)).param("searchCriteriaList", criteriaJSON)
            .contentType(APPLICATION_JSON))
        .andReturn();
    this.mvc.perform(asyncDispatch(result)).andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.content", hasSize(1))).andExpect(jsonPath("$.sort.sorted", is(true)));
  }

  @Test
  public void testReadPenDemographicsPaginated_givenDifferentFilters2_ShouldReturnStatusOk() throws Exception {
    Map<String, String> sortMap = new HashMap<>(1);
    sortMap.put("studSurname", "DESC");
    SearchCriteria criteria = SearchCriteria.builder().key("studSurname").operation(FilterOperation.EQUAL).value("Fratczak".toUpperCase()).valueType(ValueType.STRING).build();
    SearchCriteria criteria2 = SearchCriteria.builder().key("studSex").condition(AND).operation(FilterOperation.EQUAL).value("Female".toUpperCase()).valueType(ValueType.STRING).build();
    List<SearchCriteria> criteriaList = new LinkedList<>();
    criteriaList.add(criteria);
    criteriaList.add(criteria2);
    SearchCriteria criteria3 = SearchCriteria.builder().key("studGiven").operation(FilterOperation.EQUAL).value("Sigmund".toUpperCase()).valueType(ValueType.STRING).build();
    SearchCriteria criteria4 = SearchCriteria.builder().condition(OR).key("studBirth").operation(FilterOperation.EQUAL).value("1938-03-26").valueType(ValueType.STRING).build();
    List<SearchCriteria> criteriaList1 = new LinkedList<>();
    criteriaList1.add(criteria3);
    criteriaList1.add(criteria4);
    List<Search> searches = new LinkedList<>();
    searches.add(Search.builder().searchCriteriaList(criteriaList).build());
    searches.add(Search.builder().condition(OR).searchCriteriaList(criteriaList1).build());
    ObjectMapper objectMapper = new ObjectMapper();
    String criteriaJSON = objectMapper.writeValueAsString(searches);
    MvcResult result = this.mvc
        .perform(get("/paginated")
            .with(jwt().jwt((jwt) -> jwt.claim("scope", "READ_PEN_DEMOGRAPHICS")))
            .param("sort", new ObjectMapper().writeValueAsString(sortMap)).param("searchCriteriaList", criteriaJSON)
            .contentType(APPLICATION_JSON))
        .andReturn();
    this.mvc.perform(asyncDispatch(result)).andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.content", hasSize(2))).andExpect(jsonPath("$.sort.sorted", is(true)));
  }
  @Test
  public void testReadPenDemographicsPaginated_givenInvalidJSONString_ShouldReturnStatusOk() throws Exception {
    Map<String, String> sortMap = new HashMap<>(1);
    sortMap.put("studSurname", "DESC");
    SearchCriteria criteria = SearchCriteria.builder().key("studSurname").operation(FilterOperation.EQUAL).value("Fratczak".toUpperCase()).valueType(ValueType.STRING).build();
    List<SearchCriteria> criteriaList = new LinkedList<>();
    criteriaList.add(criteria);
    SearchCriteria criteria3 = SearchCriteria.builder().key("studGiven").operation(null).value("Jaquelin".toUpperCase()).valueType(ValueType.STRING).build();
    List<SearchCriteria> criteriaList1 = new LinkedList<>();
    criteriaList1.add(criteria3);
    List<Search> searches = new LinkedList<>();
    searches.add(Search.builder().searchCriteriaList(criteriaList).build());
    searches.add(Search.builder().condition(AND).searchCriteriaList(criteriaList1).build());
    ObjectMapper objectMapper = new ObjectMapper();
    String criteriaJSON = objectMapper.writeValueAsString(searches);
    this.mvc.perform(get("/paginated")
        .with(jwt().jwt((jwt) -> jwt.claim("scope", "READ_PEN_DEMOGRAPHICS")))
        .param("sort", new ObjectMapper().writeValueAsString(sortMap)).param("searchCriteriaList", criteriaJSON)
        .contentType(APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  public void testReadPenDemographicsPaginated_givenInvalidJSONString2_ShouldReturnStatusOk() throws Exception {
    this.mvc.perform(get("/paginated")
            .with(jwt().jwt((jwt) -> jwt.claim("scope", "READ_PEN_DEMOGRAPHICS")))
            .param("searchCriteriaList", "criteria_invalid")
        .contentType(APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest());
  }

}
