package com.dynamodb_springboot.dynamodb.controller;

import com.dynamodb_springboot.dynamodb.model.job.JobModel;
import com.dynamodb_springboot.dynamodb.service.BaseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/dynamoDb/job")
@Tag(name = "job", description = "DynamoDb Job Management APIs")
public class JobController extends BaseController<JobModel>{

    @Autowired
    public JobController(BaseService<JobModel> service) {
        super(service);
    }
}
