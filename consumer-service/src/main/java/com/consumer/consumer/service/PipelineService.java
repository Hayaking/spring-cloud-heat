package com.consumer.consumer.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.consumer.consumer.bean.DruidParam;
import com.consumer.consumer.bean.vo.ChartResponse;
import com.consumer.consumer.bean.vo.ComponentVO;
import com.consumer.consumer.bean.vo.PipelineVo;
import com.consumer.consumer.bean.vo.StationVO;
import pojo.Pipeline;

import java.util.List;

public interface PipelineService extends IService<Pipeline> {


    boolean savePipeline(PipelineVo pipeline);

    Page<PipelineVo> getPage(Integer pageNum, Integer pageSize, String key);

    ChartResponse druid(DruidParam druidParam);

    PipelineVo getBaseInfo(Integer id);

    List<StationVO> getStationList(Integer id);

    Page<ComponentVO> getSensorList(Page<ComponentVO> page, Integer id);

    boolean deleteBatch(List<Integer> idList);

    boolean unbindByComponentId(Integer id);
}
