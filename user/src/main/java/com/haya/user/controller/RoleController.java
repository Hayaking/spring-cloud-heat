package com.haya.user.controller;

import com.haya.user.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pojo.Role;

/**
 * @author haya
 */
@RestController
public class RoleController {
    @Autowired
    public RoleService roleService;

    @GetMapping(value = "role/all")
    public Object getAll() {
        return roleService.list();
    }

    @PostMapping(value = "role")
    public Object save(@RequestBody Role role) {
        return roleService.saveOrUpdate(role);
    }

    @DeleteMapping(value = "role/{id}")
    public Object deleteById(@PathVariable Integer id) {
        return roleService.removeById(id);
    }
}
