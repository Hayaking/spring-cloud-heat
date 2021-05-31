package com.consumer.consumer.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.consumer.consumer.bean.DruidParam;
import com.consumer.consumer.bean.vo.ChartResponse;
import com.consumer.consumer.bean.vo.ComponentVO;
import com.consumer.consumer.bean.vo.PipelineVo;
import com.consumer.consumer.bean.vo.StationVO;
import com.consumer.consumer.service.StationService;
import msg.Message;
import msg.MessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author haya
 */
@RestController
@RequestMapping("/station")
public class StationController {
    @Autowired
    private StationService stationService;

    @GetMapping(value = {
            "/page/{pageNum}/{pageSize}/{key}",
            "/page/{pageNum}/{pageSize}"
    })
    public Message getAllByPage(@PathVariable Integer pageNum,
                                @PathVariable Integer pageSize,
                                @PathVariable(required = false) String key) {
        Page<StationVO> page = stationService.getPage( pageNum, pageSize, key );
        return MessageFactory.message( true, page );
    }

    @PostMapping("/save")
    public Message save(@RequestBody StationVO station) {
        Boolean flag = stationService.saveWithComponent( station );
        return MessageFactory.message( flag );
    }

    @PostMapping("/druid")
    public ChartResponse druid(@RequestBody DruidParam druidParam) {
        return stationService.druid( druidParam );
    }

    @GetMapping("/sensor/list/{id}/{pageNum}/{pageSize}")
    public Message getSensorList(@PathVariable Integer id,
                                 @PathVariable Integer pageNum,
                                 @PathVariable Integer pageSize) {
        Page<ComponentVO> list = stationService.getSensorList(new Page<>(pageNum,pageSize),id);
        return MessageFactory.message(true, list);
    }

    @GetMapping("/pipeline/list/{id}/{pageNum}/{pageSize}")
    public Message getPipelineList(@PathVariable Integer id,
                                 @PathVariable Integer pageNum,
                                 @PathVariable Integer pageSize) {
        Page<PipelineVo> list = stationService.getPipelineList(new Page<>(pageNum,pageSize),id);
        return MessageFactory.message(true, list);
    }

    @GetMapping("/baseInfo/{id}")
    public Message baseInfo(@PathVariable Integer id) {
        StationVO baseInfo = stationService.getBaseInfo(id);
        return MessageFactory.message(true, baseInfo);
    }

    @DeleteMapping("/batch")
    public Message deleteBatch(@RequestBody List<Integer> idList) {
        boolean res = stationService.deleteBatch(idList);
        return MessageFactory.message(res);
    }

    @GetMapping("/component/unbind/{id}")
    public Message unbind(@PathVariable Integer id) {
        boolean flag = stationService.unbindByComponentId( id );
        return MessageFactory.message(flag);
    }

}
