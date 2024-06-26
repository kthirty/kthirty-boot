package top.kthirty.develop.gen;

import com.mybatisflex.codegen.Generator;
import com.mybatisflex.codegen.config.EntityConfig;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.mybatisflex.codegen.constant.GenTypeConst;
import com.mybatisflex.codegen.generator.GeneratorFactory;
import com.zaxxer.hikari.HikariDataSource;
import top.kthirty.core.db.base.entity.LogicEntity;
import top.kthirty.core.db.base.mapper.BaseMapper;
import top.kthirty.core.db.base.service.BaseService;
import top.kthirty.core.web.base.BaseController;

import java.util.HashMap;
import java.util.Map;

public class CodeGen {
    public static void main(String[] args) {

        String basePath = "D:\\System\\public\\kthirty-boot\\kthirty-service\\kthirty-system";

        String tablePrefix = "sys_";
        String basePackage = "top.kthirty.system.";

        Map<String,String> moduleTable = new HashMap<>();
        moduleTable.put("dict","sys_dict,sys_dict_item");

        moduleTable.forEach((model,tables) -> gen(basePath,basePackage+model,tablePrefix,tables.split(",")));
    }

    private static void gen(String basePath, String basePackage, String tablePrefix, String[] tables) {
        //配置数据源
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://nas.kthirty.top:5006/kthirty");
        dataSource.setUsername("root");
        dataSource.setPassword(System.getenv("KTHIRTY_PASSWORD"));

        GeneratorFactory.registerGenerator(GenTypeConst.ENTITY,new CustomEntityGenerator());
        // 创建配置内容
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setSourceDir(basePath+"/src/main/java/");
        globalConfig.setMapperXmlPath(basePath+"/src/main/java/"+basePackage.replace(".","/")+"/mapper");
        globalConfig.setBasePackage(basePackage);

        //设置表前缀和只生成哪些表
        globalConfig.setControllerRestStyle(true);
        globalConfig.setTablePrefix(tablePrefix);
        globalConfig.setGenerateTable(tables);


        //设置生成 entity 并启用 Lombok
        globalConfig.setEntityGenerateEnable(true);
        globalConfig.getEntityConfig()
                .setImplInterfaces()
                .setOverwriteEnable(true)
                .setWithLombok(true)
                .setSuperClass(LogicEntity.class)
                .setSwaggerVersion(EntityConfig.SwaggerVersion.DOC)
                .setWithLombok(true);

        //设置生成 mapper
        globalConfig.enableMapper().setSuperClass(BaseMapper.class);
        globalConfig.enableMapperXml().setOverwriteEnable(true).setFileSuffix("Mapper");
//

        // service
        globalConfig.enableService();
        globalConfig.enableServiceImpl();
        globalConfig.getServiceConfig().setSuperClass(BaseService.class);
        // controller
        globalConfig.enableController().setSuperClass(BaseController.class)
                .setOverwriteEnable(true);

        Generator generator = new Generator(dataSource, globalConfig);
        generator.generate();
    }
}
