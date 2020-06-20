package com.chdlsp.pokeball.controller;


import com.chdlsp.pokeball.interfaces.CrudInterface;
import com.chdlsp.pokeball.model.network.Header;
import com.chdlsp.pokeball.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
public abstract class CrudController<Request, Response, Entity> implements CrudInterface<Request, Response> {

    @Autowired(required = false)
    protected BaseService<Request, Response, Entity> baseService; // 상속받은 클래스에서만 접근 가능

    @Override
    @PostMapping("")
    public Header<Response> create(@RequestBody Header<Request> ApiRequest) {
        return baseService.create(ApiRequest);
    }

    @Override
    @GetMapping("{id}")
    public Header<Response> read(@PathVariable Long id) {
        return baseService.read(id);
    }

    @Override
    @PutMapping("")
    public Header<Response> update(@RequestBody Header<Request> ApiRequest) {
        return baseService.update(ApiRequest);
    }

    @Override
    @DeleteMapping("{id}")
    public Header delete(@PathVariable Long id) {
        return baseService.delete(id);
    }
}
