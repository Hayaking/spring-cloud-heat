package com.security.security.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.security.security.mapper.DbbakMapper;
import com.security.security.service.DbbakService;
import com.security.security.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import pojo.Dbbak;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

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

    @Autowired
    private LogService logService;

    @Value("${spring.datasource.url}")
    private String exportDatabaseName;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Override
    public void export() {
        String os = System.getProperty( "os.name" );
        String rootPath = os.contains( "Windows" ) ?
                ClassUtils.getDefaultClassLoader()
                        .getResource( "" )
                        .getPath()
                        .substring( 1 ).replace( "/", "\\" ) :
                ClassUtils.getDefaultClassLoader()
                        .getResource( "" )
                        .getPath();
        String path = rootPath + "databaseBackup" + File.separator;
        String fileName = new SimpleDateFormat( "yyyy-MM-dd_hh-mm-ss" )
                .format( new Date() ) + ".sql";
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            String command = getExportCommand( path, fileName );
            System.out.println( command );
            process = runtime.exec( command );
            System.out.println( process.waitFor() );
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        boolean flag = dbbakMapper.insert( new Dbbak() {{
            setCreateTime( LocalDateTime.now() );
            setPath( path + fileName );
        }} ) == 1;
        if (flag) {
//            logService.addOneLog( "数据库备份成功,文件名：" + fileName );
        }
    }

    @Override
    public IPage<Dbbak> listDbbak(int pageNum, int pageSize) {
        QueryWrapper<Dbbak> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("createTime");
        return dbbakMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
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
        String os = System.getProperty( "os.name" );
        // 获取数据库名
        String dbName = exportDatabaseName.split( "/" )[3].split( "\\?" )[0];
        // 获取主机号
        String host = exportDatabaseName.split( "/" )[2].split( ":" )[0];
        // 使用的端口号
        String port = exportDatabaseName.split( "/" )[2].split( ":" )[1];

        File fileDir = new File( path );
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
//        File myFile = new File( path, fileName );
//        try {
//            myFile.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        String root = null;
        if (os.contains( "Windows" )) {
            root = ClassUtils.getDefaultClassLoader().getResource( "" ).getPath().replace( "/", "\\" ).substring( 1 );
            command.append( "cmd /c start /b " )
                    .append( root )
                    .append( "scripts" )
                    .append( File.separator )
                    .append( "back.bat " );
        } else {
            root = ClassUtils.getDefaultClassLoader().getResource( "" ).getPath();
            command.append( "/bin/sh " )
                    .append( root )
                    .append( "scripts" )
                    .append( File.separator )
                    .append( "back.sh " );
        }
        command.append(host).append(" ")
                .append(port).append(" ")
                .append( username ).append( " " )
                .append( password ).append( " " )
                .append( dbName ).append( " " )
                .append( path ).append( fileName );
        return command.toString();
    }

}
