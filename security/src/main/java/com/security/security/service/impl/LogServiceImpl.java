package com.security.security.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.security.security.mapper.LogMapper;
import com.security.security.service.LogService;
import org.springframework.stereotype.Service;
import pojo.Log;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

/**
 * @author haya
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {

    @Resource
    private LogMapper logMapper;

    @Override
    public IPage<Log> getPageByDateRange(Page<Log> page, String startDate, String endDate) {
        List<Log> list = logMapper.getPageByDateRange( page,
                LocalDate.parse( startDate ).atStartOfDay(),
                LocalDate.parse( endDate ).atStartOfDay() );
        page.setRecords( list );
        return page;
    }

}