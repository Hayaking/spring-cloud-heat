package com.consumer.consumer.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.consumer.consumer.service.PicService;
import com.consumer.consumer.service.SecurityClient;
import msg.MessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pojo.Pic;
import pojo.User;

import java.util.List;


@RestController
@RequestMapping("/pic")
public class PicController {
    @Autowired
    private PicService picService;

    @Autowired
    private SecurityClient securityClient;

    @GetMapping(value = "/project/{id}")
    public List<Pic> getImagePathByProjectId(@PathVariable("id") int pid) {
        return picService.getImagePath(pid);
    }

    @GetMapping(value = "/path/{id}")
    public Pic getImagePathById(@PathVariable("id") int pid) {
        return picService.getImagePathById(pid);
    }

    // 获取分页
    @GetMapping(value = "/page/{pageNum}/{pageSize}/{pid}")
    public Object getAllByPage(@PathVariable Integer pageNum,
                               @PathVariable Integer pageSize,
                               @PathVariable Integer pid) {
        return picService.getPicsList(new Page<>(pageNum, pageSize), pid);
    }

    // 删除
    @DeleteMapping(value = "/{id}")
    public Object deleteById(@PathVariable("id") Integer id) {
        return MessageFactory.message(picService.deleteById(id));
    }

    // 上传
    @PostMapping(value = "/uploadFiles/{projectId}")
    public Object uploadFiles(MultipartFile[] upfile, @PathVariable("projectId") int projectId) {
        User user = securityClient.getUserInfo();
        return MessageFactory.message(picService.uploadImage(projectId, upfile, user));
    }
}
