package ca.bc.gov.educ.api.pendemog.config;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The type Pen demog mvc config.
 */
@Configuration
public class PenDemogMVCConfig implements WebMvcConfigurer {

    @Getter(AccessLevel.PRIVATE)
    private final RequestResponseInterceptor requestResponseInterceptor;

  /**
   * Instantiates a new Pen demog mvc config.
   *
   * @param requestResponseInterceptor the pen demog request interceptor
   */
  @Autowired
    public PenDemogMVCConfig(final RequestResponseInterceptor requestResponseInterceptor){
        this.requestResponseInterceptor = requestResponseInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestResponseInterceptor).addPathPatterns("/**");
    }
}
