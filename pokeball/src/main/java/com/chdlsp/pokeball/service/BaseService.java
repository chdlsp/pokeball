package com.chdlsp.pokeball.service;


import com.chdlsp.pokeball.interfaces.CrudInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component // Autowired 를 처리하기 위해 수행
public abstract class BaseService<Request, Response, Entity> implements CrudInterface<Request, Response> {

    @Autowired(required = false) // 있을 수도 있고 없을 수도 있다
    protected JpaRepository<Entity, Long> baseRepository;

}
