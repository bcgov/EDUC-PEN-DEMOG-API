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
    private final PenDemogRequestInterceptor penDemogRequestInterceptor;

  /**
   * Instantiates a new Pen demog mvc config.
   *
   * @param penDemogRequestInterceptor the pen demog request interceptor
   */
  @Autowired
    public PenDemogMVCConfig(final PenDemogRequestInterceptor penDemogRequestInterceptor){
        this.penDemogRequestInterceptor = penDemogRequestInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(penDemogRequestInterceptor).addPathPatterns("/**");
    }
}
