package com.consumer.consumer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.consumer.consumer.bean.ComponentFilter;
import com.consumer.consumer.bean.DruidParam;
import com.consumer.consumer.bean.dto.HeatDataDTO;
import com.consumer.consumer.bean.enu.MetricEnum;
import com.consumer.consumer.bean.vo.ChartResponse;
import com.consumer.consumer.bean.vo.ComponentBaseInfoVO;
import com.consumer.consumer.bean.vo.ComponentVO;
import com.consumer.consumer.mapper.druid.DruidMapper;
import com.consumer.consumer.mapper.mysql.ComponentMapper;
import com.consumer.consumer.service.ComponentService;
import com.consumer.consumer.util.DoubleFormatUtil;
import com.consumer.consumer.util.MapUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import pojo.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.consumer.consumer.bean.enu.MetricEnum.*;
import static java.util.Arrays.asList;

@Service
public class ComponentServiceImpl extends ServiceImpl<ComponentMapper, Component> implements ComponentService {
    @Autowired
    private ComponentMapper componentMapper;
    @Autowired
    private DruidMapper druidMapper;

    private static final Map<Integer, String> childTypeMap = new HashMap<Integer, String>() {{
        put(1, "一次供水管道");
        put(2, "一次回水管道");
        put(3, "二次供水管道");
        put(4, "二次回水管道");
        put(5, "循环管道");
    }};

    @Override
    public void drawChart() {

    }

    @Override
    public Page<ComponentVO> getComponentPage(ComponentFilter filter) {
        QueryWrapper<Component> wrapper = new QueryWrapper<>();
        wrapper.in("type", asList(filter.getType()));
        if (!StringUtils.isEmpty(filter.getName())) {
            wrapper.like("name", filter.getName());
        }

        Page<Component> page = new Page<>(filter.getPageNum(), filter.getPageSize());
        Page<Component> componentPage = componentMapper.selectPage(page, wrapper);
        Page<ComponentVO> resultPage = new Page<ComponentVO>() {{
            setTotal(componentPage.getTotal());
            setCurrent(componentPage.getCurrent());
            setSize(componentPage.getSize());
            setPages(componentPage.getPages());
        }};
        List<Component> componentList = componentPage.getRecords();
        List<Integer> ids = componentList
                .stream()
                .map(Component::getId)
                .collect(Collectors.toList());
        // 查druid
        List<HeatDataDTO> dataList = druidMapper.selectByComponentIdList(ids, filter.getType());
        Map<Integer, List<HeatDataDTO>> dataMap = dataList.stream().collect(Collectors.groupingBy(HeatDataDTO::getId));
        List<ComponentVO> resultList = componentList.stream()
                .map(component -> {
                    ComponentVO vo = new ComponentVO();
                    BeanUtils.copyProperties(component, vo);
                    vo.setAliasName(StringUtils.isEmpty(component.getName()) ? "--" : component.getName());
                    List<HeatDataDTO> metricDataList = dataMap.get(component.getId());
                    if (!CollectionUtils.isEmpty(metricDataList)) {
                        List<HeatDataDTO> values = MapUtil.getLastTimeMetric(metricDataList);
                        values.forEach(data -> {
                            String metricName = data.getMetricName();
                            Double metricValue = data.getMetricValue();
                            if (pipeline_water_flow.name().equals(metricName)) {
                                vo.setFlow(DoubleFormatUtil.halfUp(metricValue));
                            } else if (pipeline_water_temperature.name().equals(metricName)) {
                                vo.setTemperature(DoubleFormatUtil.halfUp(metricValue));
                            } else if (pipeline_water_pressure.name().equals(metricName)) {
                                vo.setPressure(DoubleFormatUtil.halfUp(metricValue));
                            } else if (station_water_temperature.name().equals(metricName)) {
                                vo.setTemperature(DoubleFormatUtil.halfUp(metricValue));
                            } else if (station_water_flow.name().equals(metricName)) {
                                vo.setFlow(DoubleFormatUtil.halfUp(metricValue));
                            } else if (station_water_pressure.name().equals(metricName)) {
                                vo.setPressure(DoubleFormatUtil.halfUp(metricValue));
                            } else if (pump_watt.name().equals(metricName)) {
                                vo.setWatt(DoubleFormatUtil.halfUp(metricValue));
                            } else if (pump_voltage.name().equals(metricName)) {
                                vo.setVoltage(DoubleFormatUtil.halfUp(metricValue));
                            } else if (pump_state.name().equals(metricName)) {
                                vo.setState(metricValue.intValue());
                            }
                        });
                    }
                    return vo;
                }).collect(Collectors.toList());
        resultPage.setRecords(resultList);
        return resultPage;
    }

    @Override
    public ComponentBaseInfoVO getBaseInfo(Integer id) {
        ComponentBaseInfoVO baseInfo = new ComponentBaseInfoVO();
        Component component = componentMapper.selectById(id);
        BeanUtils.copyProperties(component, baseInfo);
        List<HeatDataDTO> dataList = druidMapper.getPipeLineBaseInfo(id);
        if (!CollectionUtils.isEmpty(dataList)) {
            Map<String, List<HeatDataDTO>> list = dataList.stream().collect(Collectors.groupingBy(HeatDataDTO::getMetricName));
            list.forEach((metricName, metricValues) -> {
                List<HeatDataDTO> values = MapUtil.getLastTimeMetric(metricValues);
                HeatDataDTO data = values.get(0);
                Double metricValue = data.getMetricValue();
                if (pipeline_water_temperature.name().equals(metricName)) {
                    baseInfo.setTemperature(DoubleFormatUtil.halfUp(metricValue));
                } else if (pipeline_water_flow.name().equals(metricName)) {
                    baseInfo.setFlow(DoubleFormatUtil.halfUp(metricValue));
                } else if (pipeline_water_pressure.name().equals(metricName)) {
                    baseInfo.setPressure(DoubleFormatUtil.halfUp(metricValue));
                } else if (pipeline_water_level.name().equals(metricName)) {
                    baseInfo.setLevel(DoubleFormatUtil.halfUp(metricValue));
                }
            });
        }
        return baseInfo;
    }

    @Override
    public ChartResponse druid(DruidParam druidParam) {
        String metricName = druidParam.getMetricName();
        if (component_up.name().equals(metricName)) {
            return drawComponentUp(druidParam);
        } else if (metricName.startsWith("station_")) {
            return drawStationChart(druidParam);
        } else if (pipeline_water_flow.name().equals(metricName)) {
            return draw(druidParam, null);
        } else if (pipeline_water_temperature.name().equals(metricName)) {
            return draw(druidParam, Collections.singletonList(
                    ChartResponse.YAxis.addTickUnit("立方米/分钟")
            ));
        } else if (pipeline_water_temperature_increase.name().equals(metricName)) {
            return draw(druidParam, Collections.singletonList(
                    ChartResponse.YAxis.addTickUnit("立方米/分钟")
            ));
        } else if (pipeline_out_temperature.name().equals(metricName)) {
            return draw(druidParam, Collections.singletonList(
                    ChartResponse.YAxis.addTickUnit("立方米/分钟")
            ));
        } else {
            return draw(druidParam, null);
        }
    }

    private ChartResponse drawStationChart(DruidParam druidParam) {
        ChartResponse response = new ChartResponse();
        List<HeatDataDTO> dataList = druidMapper.getStationChartDataList(druidParam);
        Map<Integer, List<HeatDataDTO>> dataMap = dataList.stream().collect(Collectors.groupingBy(HeatDataDTO::getChildType));
        dataMap.forEach((childType,list)->{
            ChartResponse.Serie serie = response.addSerie(childTypeMap.get(childType), ChartResponse.SerieType.line);
            list.forEach(data -> serie.addData(data.get__time().getTime(), DoubleFormatUtil.halfUp(data.getMetricValue())));
        });
        return response;
    }

    private ChartResponse drawComponentUp(DruidParam druidParam) {
        ChartResponse response = new ChartResponse();
        response.setYAxis(asList(ChartResponse.YAxis.addTickUnit("").setMax(1L)));
        List<HeatDataDTO> dataList = druidMapper.getComponentUP(druidParam);
        ChartResponse.Serie serie = response.addSerie(MetricEnum.getMetricChName(druidParam.getMetricName()), ChartResponse.SerieType.column);
        HashMap<Double, String> colorMap = new HashMap<Double, String>() {{
            put(0d, "#07F531");
            put(1d, "#FFF000");
            put(2d, "#FF0000");
        }};
        dataList.forEach(data -> {
            serie.addData(
                    data.get__time().getTime(),
                    1D,
                    colorMap.get(data.getMetricValue())
            );
        });
        return response;
    }

    // 管道流量
    private ChartResponse draw(DruidParam druidParam, List<ChartResponse.YAxis> units) {
        ChartResponse response = new ChartResponse();
        response.setYAxis(units);
        List<HeatDataDTO> dataList = druidMapper.getPipeLineWaterFlow(druidParam);
        ChartResponse.Serie serie = response.addSerie(MetricEnum.getMetricChName(druidParam.getMetricName()), ChartResponse.SerieType.line);
        dataList.forEach(data -> serie.addData(data.get__time().getTime(), DoubleFormatUtil.halfUp(data.getMetricValue())));
        return response;
    }
}
