package com.consumer.consumer.service;

import com.consumer.consumer.bean.ComponentFilter;
import com.consumer.consumer.bean.vo.CollectorVO;

import java.util.List;

public interface CollectorService {
    List<CollectorVO> getPage(ComponentFilter filter);
}
