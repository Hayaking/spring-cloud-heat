package com.haya.user.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.haya.user.mapper.DbbakMapper;
import com.haya.user.service.DbbakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import pojo.Dbbak;
import pojo.User;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yyp
 * @since 2020-05-27
 */
@Service
public class DbbakServiceImpl extends ServiceImpl<DbbakMapper, Dbbak> implements DbbakService {

    @Autowired
    private DbbakMapper dbbakMapper;

    @Value("${spring.datasource.url}")
    private String exportDatabaseName;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Override
    public void export() {
        String  path = ClassUtils.getDefaultClassLoader().getResource("").getPath() +  "databaseBackup" + File.separator;
        String fileName = new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss").format(new Date()) + ".sql";
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec( getExportCommand( path, fileName ) );
            BufferedReader reader = new BufferedReader( new InputStreamReader( process.getInputStream() ) );
            System.out.println(reader.readLine());
            System.out.println(reader.readLine());

        } catch (IOException e) {
            e.printStackTrace();
        }

        Dbbak dbbak = new Dbbak();
        dbbak.setCreateTime(LocalDateTime.now());
//        dbbak.setUid(((User)SecurityUtils.getSubject().getPrincipal()).getId());
        dbbak.setPath(path + fileName);

        dbbakMapper.insert(dbbak);
    }

    @Override
    public IPage<Dbbak> listDbbak(int pageNum, int pageSize) {
        Page<Dbbak> page = new Page<>(pageNum, pageSize);
        List<Dbbak> list = dbbakMapper.getListWithUserName( page );
        page.setRecords( list );
        return page;
    }

    @Override
    public FileInputStream download(String id) throws IOException {
        QueryWrapper<Dbbak> wrapper = new QueryWrapper<>();
        wrapper.eq( "id", id );
        Dbbak dbbak = dbbakMapper.selectOne( wrapper );
        String path = dbbak.getPath();
        FileInputStream fis = new FileInputStream( new File( path ) );
        return fis;
    }

    private String getExportCommand(String path, String fileName) {

        StringBuffer command = new StringBuffer();

        // 获取数据库名
        String dbName = exportDatabaseName.split("/")[3].split("\\?")[0];
        // 获取主机号
        String host = exportDatabaseName.split("/")[2].split(":")[0];
        // 使用的端口号
        String port = exportDatabaseName.split("/")[2].split(":")[1];

        // 导出路径

        File fileDir = new File(path);
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }
        File myFile = new File(path,fileName);
        try {
            myFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String exportPath = path.substring( 1 ) + fileName;//导出路径

        //注意哪些地方要空格，哪些不要空格
        //密码是用的小p，而端口是用的大P。
        command.append("mysqldump -u").append(username)
                .append(" -p").append(password)
                .append(" -h ").append(host)
                .append(" -P").append(port)
                .append("  ").append(dbName)
                .append(" -r ").append(exportPath)
                .append( " ;" );
        System.out.println(command.toString());
        return command.toString();
    }

}
