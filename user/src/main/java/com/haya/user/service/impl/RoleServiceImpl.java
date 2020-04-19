package com.haya.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haya.user.mapper.RoleMapper;
import com.haya.user.service.RoleService;
import org.springframework.stereotype.Service;
import pojo.Role;

/**
 * @author haya
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>  implements RoleService {
}
