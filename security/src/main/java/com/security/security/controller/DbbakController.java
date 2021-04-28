package com.security.security.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.security.security.service.DbbakService;
import msg.Message;
import msg.MessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pojo.Dbbak;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;


@RequestMapping(value = "dbback")
@RestController
public class DbbakController {

    @Autowired
    private DbbakService dbbakService;

    @GetMapping("/backups")
    public void backups() {
        dbbakService.export();
    }


    @GetMapping(value = "/page/{pageNum}/{pageSize}")
    public IPage<Dbbak> page(@PathVariable Integer pageNum,
                             @PathVariable Integer pageSize) {
        return dbbakService.listDbbak(pageNum, pageSize);
    }

    @DeleteMapping(value = "/batch")
    public Message delete(@RequestBody List<Integer> idList) {
        boolean isDelete = dbbakService.removeByIds(idList);
        return MessageFactory.message(isDelete);
    }

    @PostMapping(value = "/{id}/download")
    public ResponseEntity<byte[]> download(@PathVariable String id) {
        ResponseEntity<byte[]> response = null;
        try (
                FileInputStream fis = dbbakService.download(id);
                FileChannel channel = fis.getChannel()
        ) {
            ByteBuffer body = ByteBuffer.allocate(fis.available());
            channel.read(body);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Access-Control-Expose-Headers", "Content-Disposition");
            headers.add("Content-Disposition", "attachment;filename=" + id + ".sql");
            response = new ResponseEntity<>(body.array(), headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

}
