package com.haya.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.haya.user.common.pojo.Dbbak;
import com.haya.user.service.DbbakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


@RestController
public class DbbakController {

    @Autowired
    private DbbakService dbbakService;

    @GetMapping("dbbak/backups")
    public void backups() {
        dbbakService.export();
    }


    @GetMapping(value = "dbbak")
    public IPage<Dbbak> page(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return dbbakService.listDbbak( pageNum, pageSize );
    }


    @PostMapping(value = "dbbak/{id}/download")
    public ResponseEntity<byte[]> download(@PathVariable String id) {
        ResponseEntity<byte[]> response = null;
        try (
                FileInputStream fis = dbbakService.download( id );
                FileChannel channel = fis.getChannel()
        ) {
            ByteBuffer body = ByteBuffer.allocate( fis.available() );
            channel.read( body );
            HttpHeaders headers = new HttpHeaders();
            headers.add( "Access-Control-Expose-Headers", "Content-Disposition" );
            headers.add( "Content-Disposition", "attachment;filename=" + id + ".sql" );
            response = new ResponseEntity<>( body.array(), headers, HttpStatus.OK );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

}
