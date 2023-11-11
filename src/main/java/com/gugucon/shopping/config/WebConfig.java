package com.gugucon.shopping.config;

import com.gugucon.shopping.member.controlller.converter.StringToBirthYearRangeConverter;
import com.gugucon.shopping.member.controlller.converter.StringToGenderConverter;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToBirthYearRangeConverter());
        registry.addConverter(new StringToGenderConverter());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**", "/js/**", "/css/**")
            .addResourceLocations("classpath:/static/assets/", "classpath:/static/css/", "classpath:/static/js/")
            .setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS));
    }
}
