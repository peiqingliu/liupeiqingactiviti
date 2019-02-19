package com.liupeiqing.springboot.activiti.controller;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liupeqing
 * @date 2019/2/18 19:00
 */
@RestController
@RequestMapping("/activiti")
public class ActivitiController {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    @RequestMapping("/helloWorld")
    public void firstActiviti(){

        //根据bpmn文件部署流程
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("processes/TestProcess.bpmn")
                .deploy();
        //获取流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId())
                .singleResult();
        //启动流程定义，返回流程实例
        ProcessInstance pi = runtimeService.startProcessInstanceById(processDefinition.getId());

        String processId = pi.getId();
        System.out.println("流程创建成功，当前流程实例ID："+processId);

        Task task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
        System.out.println("执行前，任务名称："+task.getName());
        taskService.complete(task.getId());
        task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
        System.out.println("task为null，任务执行完毕："+task);
    }

    @RequestMapping("/singleAssignee")
    public void setSingleAssignee(){
        //根据bpmn文件部署流程
        repositoryService.createDeployment().addClasspathResource("processes/singleAssignee.bpmn").deploy();
        // 设置User Task1受理人变量
        Map<String,Object> variables = new HashMap<>();
        variables.put("user1", "007");
        //采用key来启动流程定义并设置流程变量，返回流程实例
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("singleAssignee", variables);
        String processId = pi.getId();
        System.out.println("流程创建成功，当前流程实例ID："+processId);
        // 注意 这里需要拿007来查询，key-value需要拿value来获取任务
        List<Task> tasks = taskService.createTaskQuery().taskAssignee("007").list();
        if (null != tasks && tasks.size() > 0){
            for (Task task : tasks){
                System.out.println("任务ID："+task.getId());
                System.out.println("任务的办理人："+task.getAssignee());
                System.out.println("任务名称："+task.getName());
                System.out.println("任务的创建时间："+task.getCreateTime());
                System.out.println("流程实例ID："+task.getProcessInstanceId());
                System.out.println("#######################################");
            }
        }
    }

}
