package com.security.security.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import pojo.Dbbak;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yyp
 * @since 2020-05-27
 */
public interface DbbakService extends IService<Dbbak> {
    /**
     * 数据库备份
     */
    void export();

    /**
     * 分页获取数据备份信息
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<Dbbak> listDbbak(int pageNum, int pageSize);

    FileInputStream download(String id) throws IOException;
}
