package com.consumer.consumer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.consumer.consumer.mapper.mysql.PipelineMapper;
import com.consumer.consumer.service.PipelineService;
import org.springframework.stereotype.Service;
import pojo.Pipeline;

@Service
public class PipelineServiceImpl extends ServiceImpl<PipelineMapper, Pipeline> implements PipelineService {

}
