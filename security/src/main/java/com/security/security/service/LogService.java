package com.security.security.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import pojo.Log;

public interface LogService extends IService<Log> {
    IPage<Log> getPageByDateRange(Page<Log> page,
                                  String startDate, String endDate);
}
