/*
 * Copyright 2018 Alfresco, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.cloud.services.query.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.activiti.cloud.api.model.shared.CloudVariableInstance;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name="Variable")
@Table(	name = "VARIABLE",
		indexes = { 
				@Index(name = "variable_processInstanceId_idx", columnList = "processInstanceId", unique = false), 
				@Index(name = "variable_taskId_idx", columnList = "taskId", unique = false),
				@Index(name = "variable_name_idx", columnList = "name", unique = false), 
				@Index(name = "variable_executionId_idx", columnList = "executionId", unique = false) 
		})
public class VariableEntity extends ActivitiEntityMetadata implements CloudVariableInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private String name;

    private String processInstanceId;

    private String taskId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date createTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date lastUpdatedTime;

    private String executionId;

    @Convert(converter = VariableValueJsonConverter.class)
    @Column(columnDefinition="text")
    private VariableValue<?> value;

    private Boolean markedAsDeleted = false;

    @JsonIgnore
    @ManyToOne(optional = true, fetch=FetchType.LAZY)
    @JoinColumn(name = "taskId", referencedColumnName = "id", insertable = false, updatable = false, nullable = true
            , foreignKey = @javax.persistence.ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
    private TaskEntity task;

    @JsonIgnore
    @ManyToOne(optional = true, fetch=FetchType.LAZY)
    @JoinColumn(name = "processInstanceId", referencedColumnName = "id", insertable = false, updatable = false
            , foreignKey = @javax.persistence.ForeignKey(value = ConstraintMode.NO_CONSTRAINT, name = "none"))
    private ProcessInstanceEntity processInstance;

    public VariableEntity() {
    }

    public VariableEntity(Long id,
                          String type,
                          String name,
                          String processInstanceId,
                          String serviceName,
                          String serviceFullName,
                          String serviceVersion,
                          String appName,
                          String appVersion,
                          String taskId,
                          Date createTime,
                          Date lastUpdatedTime,
                          String executionId) {
        super(serviceName,
              serviceFullName,
              serviceVersion,
              appName,
              appVersion);
        this.id = id;
        this.type = type;
        this.name = name;
        this.processInstanceId = processInstanceId;
        this.taskId = taskId;
        this.createTime = createTime;
        this.lastUpdatedTime = lastUpdatedTime;
        this.executionId = executionId;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    @Override
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public String getExecutionId() {
        return executionId;
    }

    public void setExecutionId(String executionId) {
        this.executionId = executionId;
    }

    public <T> void setValue(T value) {
        this.value = new VariableValue<>(value);
    }

    @Override
    public <T> T getValue() {
        return (T) value.getValue();
    }

    public ProcessInstanceEntity getProcessInstance() {
        return this.processInstance;
    }

    public void setProcessInstance(ProcessInstanceEntity processInstanceEntity) {
        this.processInstance = processInstanceEntity;
    }

    public TaskEntity getTask() {
        return this.task;
    }

    public void setTask(TaskEntity taskEntity) {
        this.task = taskEntity;
    }

    public Boolean getMarkedAsDeleted() {
        return markedAsDeleted;
    }

    public void setMarkedAsDeleted(Boolean markedAsDeleted) {
        this.markedAsDeleted = markedAsDeleted;
    }

    @Override
    public boolean isTaskVariable() {
        return taskId != null;
    }
}