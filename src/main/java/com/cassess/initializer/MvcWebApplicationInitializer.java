//package com.cassess.initializer;
//
//import com.cassess.config.DBConfig;
//import com.cassess.config.MvcConfig;
//import com.cassess.config.SecurityConfig;
//import com.cassess.config.ServiceConfig;
//import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
//
//public class MvcWebApplicationInitializer  extends AbstractAnnotationConfigDispatcherServletInitializer {
//
//    @Override
//    protected Class<?>[] getRootConfigClasses() {
//        return new Class[]{SecurityConfig.class, ServiceConfig.class, DBConfig.class};
//    }
//
//    @Override
//    protected Class<?>[] getServletConfigClasses() {
//        return new Class[] {MvcConfig.class};
//    }
//
//    @Override
//    protected String[] getServletMappings() {
//        return new String[]{"/"};
//    }
//
//}