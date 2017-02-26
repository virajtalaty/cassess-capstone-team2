package com.cassess.web.controller;

import com.cassess.persist.entity.Token;
import com.cassess.persist.entity.User;
import com.cassess.persist.repo.TokenRepo;
import com.cassess.persist.repo.UserRepo;
import com.cassess.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SecurityController {

    @Autowired
    private UserRepo userRepo;

    @RequestMapping(value = "/security/account", method = RequestMethod.GET)
    public @ResponseBody
    User getUserAccount() {
        User user = userRepo.findByLogin(SecurityUtils.getCurrentLogin());
        user.setPassword(null);
        return user;
    }

}