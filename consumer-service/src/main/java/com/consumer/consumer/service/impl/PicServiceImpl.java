package com.consumer.consumer.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.consumer.consumer.mapper.mysql.PicMapper;
import com.consumer.consumer.service.PicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.web.multipart.MultipartFile;
import pojo.Pic;
import pojo.User;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yyp
 * @since 2020-05-26
 */
@Service
public class PicServiceImpl extends ServiceImpl<PicMapper, Pic> implements PicService {
    @Autowired
    PicMapper mapper;

    @Autowired
    HttpSession session;

    @Override
    public List<Pic> getImagePath(int pid) {
        return mapper.selectList(new QueryWrapper<Pic>().eq("pid", pid));
    }

    @Override
    public Pic getImagePathById(int id) {
        return this.getOne(new QueryWrapper<Pic>().eq("id", id));
    }

    @Override
    public boolean deleteById(int id) {
        String imagePath = this.getOne(new QueryWrapper<Pic>().eq("id", id)).getPath();
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/"
                + imagePath;
        if (imagePath != null && imagePath.length() != 0 && path.length() != 0) {
            if (this.remove(new QueryWrapper<Pic>().eq("id", id))) {
                //删除本地图片
                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Boolean uploadImage(int projectId, MultipartFile[] upfile, User user) {
        for (MultipartFile file : upfile) {
            if (!upload(file, projectId, String.valueOf(user.getId()))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public IPage<Pic> getPicsList(Page<Pic> page, int pid) {
        page.setRecords( mapper.searchPicsListPage( page, pid ) );
        return page;
    }


    /**
     * 上传一张图片 每一个项目单独存到一个文件夹中
     *
     * @param upfile
     * @param projectId
     * @param uid
     * @return
     */
    public boolean upload(MultipartFile upfile, int projectId, String uid) {
        String fileName = upfile.getOriginalFilename();
        String newImageName = getNewImageName(fileName);
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/images/" + projectId;
        String newPath = path + "\\" + newImageName;
        File file = new File(path);
        File uplodFile = new File(newPath);
        try {
            if (!file.exists()) {
                file.mkdirs();
            }
            if (!uplodFile.exists()) {
                uplodFile.createNewFile();
            }
            upfile.transferTo(uplodFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        //设置一个Pics
        Pic pics = new Pic(){{

            setName(fileName.substring(0, fileName.lastIndexOf(".")));
            setPath("images/" + projectId + "/" + newImageName);
            setPid(projectId);

        }};
        return this.save(pics);
    }

    /**
     * 生成新的图片名字 以当前时间加上一个2位的随机数字组成
     *
     * @param oldName 传入原图片的名字
     * @return
     */
    public String getNewImageName(String oldName) {
        String imageName = oldName.substring(oldName.lastIndexOf("."));
        int random = 10 + (int) (Math.random() * 90);
        String dateTime = LocalDateTime.now().toString();
        String newImageName = dateTime.replace(".", ":") + random + imageName;
        return newImageName.replace(":", "-");
    }
}
