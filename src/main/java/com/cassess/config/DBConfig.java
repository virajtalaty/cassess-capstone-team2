package com.cassess.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.googlecode.genericdao.search.jpa.JPAAnnotationMetadataUtil;
import com.googlecode.genericdao.search.jpa.JPASearchProcessor;

@Configuration 
@EnableTransactionManagement(proxyTargetClass=true)
public class DBConfig {

	@Bean(name = "dataSource")
	public DataSource getDataSource() {
	        BasicDataSource dataSource = new BasicDataSource();
	        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
	        dataSource.setUrl("jdbc:mysql://localhost/cassess");
	        dataSource.setUsername("root");
	        dataSource.setPassword("root123");
	        return dataSource;
	}
	
	@Bean(name = "entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(getDataSource());
		emf.setPackagesToScan(new String[] { "com.cassess.*" });
		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		emf.setJpaVendorAdapter(vendorAdapter);
		emf.setJpaProperties(additionalProperties());
		return emf;
	}
	
	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf){
	    JpaTransactionManager transactionManager = new JpaTransactionManager();
	    transactionManager.setEntityManagerFactory(emf); 
	    return transactionManager;
	}
	
	@Bean
	public JPAAnnotationMetadataUtil metadataUtil() {
		JPAAnnotationMetadataUtil metadataUtil = new JPAAnnotationMetadataUtil();
		return metadataUtil;
	}
	
	@Bean
	public JPASearchProcessor searchProcessor(JPAAnnotationMetadataUtil metadataUtil){
		JPASearchProcessor searchProcessor = new JPASearchProcessor(metadataUtil);
		return searchProcessor;
	}
	 
	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
	    return new PersistenceExceptionTranslationPostProcessor();
	}
	
	Properties additionalProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.hbm2ddl.auto", "create");
		properties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
	  	return properties;
	}

} 
