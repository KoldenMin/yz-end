package com.example.yz1.config;

import com.example.yz1.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${file.upload.resume.path}")
    private String resumePath;


    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/user/register",
                        "/user/salt",
                        "/avatars/**",
                        "/resumes/**",
                        "/user/login",
                        "/error",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/v2/**",
                        "/swagger-ui.html/**"
                );
    }

    /**
     * 跨域配置
     */
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOriginPatterns("*")
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                .allowCredentials(true)
//                .allowedHeaders("*")
//                .maxAge(3600);
//    }

    /**
     * 配置静态资源映射
     * 将上传的头像文件目录映射到可访问的URL路径
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 将物理路径 uploads/avatars 映射到 URL路径 /avatars
        registry.addResourceHandler("/avatars/**")
                .addResourceLocations("file:" + uploadPath + "/");
        registry.addResourceHandler("/resumes/**")
                .addResourceLocations("file:" + resumePath + "/");
    }
}