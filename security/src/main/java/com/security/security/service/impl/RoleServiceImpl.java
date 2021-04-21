package com.security.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.security.security.mapper.RoleMapper;
import com.security.security.service.RoleService;
import org.springframework.stereotype.Service;
import pojo.Role;

/**
 * @author haya
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
}
