package com.liupeiqing.springboot.activiti.config;

import org.activiti.engine.*;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.repository.DeploymentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.IOException;

/**
 * @author liupeqing
 * @date 2019/2/18 19:07
 */
@Configuration
public class ActivitiConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ResourcePatternResolver resourceLoader;

    /**
     * 初始化配置，将创建28张表
     * @return
     */
    @Bean
    public StandaloneProcessEngineConfiguration processEngineConfiguration() {
        StandaloneProcessEngineConfiguration configuration = new StandaloneProcessEngineConfiguration();
        configuration.setDataSource(dataSource);
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        configuration.setAsyncExecutorActivate(false);
        return configuration;
    }


    @Bean
    public ProcessEngine processEngine() {
        return processEngineConfiguration().buildProcessEngine();
    }

    @Bean
    public RepositoryService repositoryService() {
        return processEngine().getRepositoryService();
    }

    @Bean
    public RuntimeService runtimeService() {
        return processEngine().getRuntimeService();
    }


    @Bean
    public TaskService taskService() {
        return processEngine().getTaskService();
    }

    /**
     * 部署流程
     * @throws IOException
     */
//    @PostConstruct
//    public void initProcess() throws IOException {
//        DeploymentBuilder deploymentBuilder= repositoryService().createDeployment();
////        Resource resource = resourceLoader.getResource("classpath:/processes/EceProvinceProcess.bpmn");
////        deploymentBuilder .enableDuplicateFiltering().addInputStream(resource.getFilename(), resource.getInputStream()).name("deploymentTest").deploy();
//        deploymentBuilder .enableDuplicateFiltering().addClasspathResource("/TestProcess.bpmn").name("deploymentTest").deploy();
//    }
}
