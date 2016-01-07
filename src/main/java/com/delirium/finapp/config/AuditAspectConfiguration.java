package com.delirium.finapp.config;

import com.delirium.finapp.auditing.AuditAspect;
import com.delirium.finapp.auditing.handler.AuditTypesHandler;
import com.delirium.finapp.auditing.handler.AuditTypesHandlerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.delirium.finapp.auditing",
    entityManagerFactoryRef = "auditEmFactory",
    transactionManagerRef = "auditTransactionManager")
@EnableAspectJAutoProxy
public class AuditAspectConfiguration implements EnvironmentAware {
    private final Logger log = LoggerFactory.getLogger(AuditAspectConfiguration.class);
    private RelaxedPropertyResolver propertyResolver;
    private Environment env;

    @Autowired(required = false)
    private PersistenceUnitManager persistenceUnitManager;

    @Bean
    public AuditAspect loggingAspect() {
        return new AuditAspect();
    }

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
        this.propertyResolver = new RelaxedPropertyResolver(env, "spring.auditing.");
    }

    @Bean
    public AuditTypesHandler auditTypesHandler() {
        return new AuditTypesHandlerImpl();
    }

//    @Bean(name = "auditDatasource")
//    public DataSource dataSource() {
//        log.debug("Configuring Datasource");
//        if (propertyResolver.getProperty("url") == null
//            && propertyResolver.getProperty("databaseName") == null) {
//            log.error("Your database connection pool configuration is incorrect! The application"
//                    + "cannot start. Please check your Spring profile, current profiles are: {}",
//                Arrays.toString(env.getActiveProfiles()));
//
//            throw new ApplicationContextException(
//                "Database connection pool is not configured correctly");
//        }
//        HikariConfig config = DatabaseConfiguration.createHikariConfig(propertyResolver, env);
//        try {
//            return new HikariDataSource(config);
//        } catch (Exception e) {
//            log.error("Couldn't create audit datasource, exception: {}", e.getMessage());
//            throw new FinappLoadingException(e);
//        }
//    }

    @Bean(name = "emfb2")
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder1() {
        return DatabaseConfiguration.createEntityManagerFacultyBuilder(this.persistenceUnitManager, jpaProperties());
    }

    @Bean(name = "auditEmFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory1(
        @Qualifier("datasource") DataSource dataSource,
//        @Qualifier("auditDatasource") DataSource dataSource,
        @Qualifier("emfb2") EntityManagerFactoryBuilder factoryBuilder) {
        //        RelaxedPropertyResolver relaxedPropertyResolver = new RelaxedPropertyResolver(env,
        //        "spring.jpa.properties.");
        //        Map<String, Object> vendorProperties = relaxedPropertyResolver.getSubProperties(null);
        return factoryBuilder.dataSource(dataSource).packages("com.delirium.finapp.auditing")
            //        .properties(vendorProperties)
            .persistenceUnit("audit").build();
    }

    @Bean(name = "auditTransactionManager")
    public PlatformTransactionManager auditTransactionManager(
        @Qualifier("auditEmFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    @ConfigurationProperties("spring.jpa")
    public JpaProperties jpaProperties() {
        return new JpaProperties();
    }
}
