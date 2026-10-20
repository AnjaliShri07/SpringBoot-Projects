package com.dynamodb_springboot.dynamodb.validator;

import com.dynamodb_springboot.dynamodb.model.BaseModel;
import com.dynamodb_springboot.dynamodb.repository.BaseRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ReadWriteAbstractValidator<T extends BaseModel>{

    protected final BaseRepository<T> repository;

    protected ReadWriteAbstractValidator(BaseRepository<T> repository) {
        this.repository = repository;
    }
}
