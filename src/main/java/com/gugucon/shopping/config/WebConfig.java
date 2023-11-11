package com.gugucon.shopping.config;

import com.gugucon.shopping.member.controlller.converter.StringToBirthYearRangeConverter;
import com.gugucon.shopping.member.controlller.converter.StringToGenderConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToBirthYearRangeConverter());
        registry.addConverter(new StringToGenderConverter());
    }
}
