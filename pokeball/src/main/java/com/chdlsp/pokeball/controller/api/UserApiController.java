package com.chdlsp.pokeball.controller.api;


import com.chdlsp.pokeball.controller.CrudController;
import com.chdlsp.pokeball.model.entity.User;
import com.chdlsp.pokeball.model.network.Header;
import com.chdlsp.pokeball.model.network.request.UserApiRequest;
import com.chdlsp.pokeball.model.network.response.UserApiResponse;
import com.chdlsp.pokeball.service.UserApiLogicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserApiController extends CrudController<UserApiRequest, UserApiResponse, User> {

    @Autowired
    private UserApiLogicService userApiLogicService;

    @GetMapping("")
    public Header<List<UserApiResponse>> search(@PageableDefault(sort = "id", direction = Sort.Direction.ASC, size = 15) Pageable pageable) {
        log.info("pageable : {}", pageable);
        return userApiLogicService.search(pageable);
    }
}
