package com.consumer.consumer.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.consumer.consumer.bean.DruidParam;
import com.consumer.consumer.bean.vo.ChartResponse;
import com.consumer.consumer.bean.vo.ComponentVO;
import com.consumer.consumer.bean.vo.PipelineVo;
import com.consumer.consumer.bean.vo.StationVO;
import pojo.Component;
import pojo.Pipeline;
import pojo.Station;

import java.util.List;

public interface StationService extends IService<Station> {

    Page<StationVO> getPage(Integer pageNum, Integer pageSize, String key);

    Boolean saveWithComponent(StationVO station);

    ChartResponse druid(DruidParam druidParam);

    Page<ComponentVO> getSensorList(Page<ComponentVO> page, Integer id);

    StationVO getBaseInfo(Integer id);

    boolean deleteBatch(List<Integer> idList);

    boolean unbindByComponentId(Integer id);

    Page<PipelineVo> getPipelineList(Page<Pipeline> objectPage, Integer id);
}
