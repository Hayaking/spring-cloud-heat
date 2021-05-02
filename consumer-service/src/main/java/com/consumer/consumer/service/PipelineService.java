package com.consumer.consumer.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.consumer.consumer.bean.vo.PipelineVo;
import pojo.Pipeline;

public interface PipelineService extends IService<Pipeline> {


    boolean savePipeline(PipelineVo pipeline);

    Page<PipelineVo> getPage(Integer pageNum, Integer pageSize, String key);
}
