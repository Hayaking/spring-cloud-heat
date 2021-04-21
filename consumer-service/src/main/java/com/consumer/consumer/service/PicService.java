package com.consumer.consumer.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import pojo.Pic;
import pojo.User;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yyp
 * @since 2020-05-26
 */
public interface PicService extends IService<Pic> {
    /**
     * 根据项目id获取所有的图片路径
     * @param id
     * @return
     */
    List<Pic> getImagePath(int id);

    /**
     * 根据图片id获取图片路径
     * @param id
     * @return
     */
    Pic getImagePathById(int id);

    /**
     * 根据id删除一张图片记录
     * @param id
     * @return
     */
    boolean deleteById(int id);

    /**
     * 上传多个图片文件
     * @param upfile
     * @return
     */
    Boolean uploadImage(int projectId, MultipartFile[] upfile, User user);

    /**
     * 分页获得图片
     *
     * @param page
     * @return
     */
    IPage<Pic> getPicsList(Page<Pic> page, int pid);

}
