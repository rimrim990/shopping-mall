package com.gugucon.shopping.config;

import com.gugucon.shopping.member.controlller.converter.StringToBirthYearRangeConverter;
import com.gugucon.shopping.member.controlller.converter.StringToGenderConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.VersionResourceResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final int MAX_RESOURCE_CACHE_SECONDS = 365 * 24 * 60 * 60;

    private final String RESOURCE_VERSION;

    public WebConfig(@Value("${resource.version}") String resourceVersion) {
        this.RESOURCE_VERSION = resourceVersion;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToBirthYearRangeConverter());
        registry.addConverter(new StringToGenderConverter());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 이미지 파일은 동일 경로에 대해 절대 변하지 않음 - 최대 기간 설정
        registry.addResourceHandler("/assets/**")
            .addResourceLocations("classpath:/static/assets/")
            .setCachePeriod(MAX_RESOURCE_CACHE_SECONDS);

        // css 파일은 컨텐츠 내용을 해싱한 값을 버전으로 사용한다
        registry.addResourceHandler("/css/**")
            .addResourceLocations("classpath:/static/css/")
            .setCachePeriod(MAX_RESOURCE_CACHE_SECONDS)
            .resourceChain(true)
            .addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"));

        // js 파일은 고정된 버전 정보를 버전으로 사용한다
        registry.addResourceHandler("/js/**")
            .addResourceLocations("classpath:/static/js/")
            .setCachePeriod(MAX_RESOURCE_CACHE_SECONDS)
            .resourceChain(true)
            .addResolver(new VersionResourceResolver().addFixedVersionStrategy(RESOURCE_VERSION, "/**"));
    }
}
