package com.dynamodb_springboot.dynamodb.service;

import com.dynamodb_springboot.dynamodb.model.job.JobModel;
import com.dynamodb_springboot.dynamodb.repository.BaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JobService extends BaseService<JobModel>{

    // add jobValidator
    @Autowired
    public JobService(BaseRepository<JobModel> repository) {
        super(repository);
    }
}
