package ca.bc.gov.educ.api.pendemog.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.slf4j.MDC;
import org.springframework.lang.NonNull;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public final class LogHelper {
  private static final ObjectMapper mapper = new ObjectMapper();
  private static final String EXCEPTION = "Exception ";
  public static final String CORRELATION_ID = "correlationID";
  private LogHelper() {

  }

  public static void logServerHttpReqResponseDetails(@NonNull final HttpServletRequest request, final HttpServletResponse response) {
    try {
      final int status = response.getStatus();
      val totalTime = Instant.now().toEpochMilli() - (Long) request.getAttribute("startTime");
      final Map<String, Object> httpMap = new HashMap<>();
      httpMap.put("server_http_response_code", status);
      httpMap.put("server_http_request_method", request.getMethod());
      httpMap.put("server_http_query_params", request.getQueryString());
      val correlationID = request.getHeader(CORRELATION_ID);
      if (correlationID != null) {
        httpMap.put("correlation_id", correlationID);
      }
      httpMap.put("server_http_request_url", String.valueOf(request.getRequestURL()));
      httpMap.put("server_http_request_processing_time_ms", totalTime);
      httpMap.put("server_http_request_payload", String.valueOf(request.getAttribute("payload")));
      httpMap.put("server_http_request_remote_address", request.getRemoteAddr());
      MDC.putCloseable("httpEvent", mapper.writeValueAsString(httpMap));
      log.info("");
      MDC.clear();
    } catch (final Exception exception) {
      log.error(EXCEPTION, exception);
    }
  }


}
