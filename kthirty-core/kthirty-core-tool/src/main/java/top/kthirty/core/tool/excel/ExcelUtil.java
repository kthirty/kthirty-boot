package top.kthirty.core.tool.excel;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelWriter;
import lombok.extern.slf4j.Slf4j;
import top.kthirty.core.tool.excel.exp.ExportHandler;
import top.kthirty.core.tool.excel.handler.DefaultCellStyleEditor;
import top.kthirty.core.tool.excel.handler.DictCellReader;
import top.kthirty.core.tool.excel.handler.DictCellWriter;
import top.kthirty.core.tool.excel.imp.ImportHandler;
import top.kthirty.core.tool.excel.support.*;
import top.kthirty.core.tool.excel.test.TestAccount;
import top.kthirty.core.tool.excel.test.TestUser;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@SuppressWarnings("unchecked")
public class ExcelUtil {
    /**
     * @author Kthirty
     * @since 2023/11/26
     * @param inputStream 文件输入流
     * @param clazz 实体类Class
     * @param params 导入参数
     * @return java.util.List<E>
     */
    public static <E> List<E> imp(InputStream inputStream, Class<E> clazz, ExcelParams params) {
        params = ObjUtil.defaultIfNull(params,new ExcelParams());
        params.defaultGroups(ExcelGroup.IMPORT);
        params.defaultStyle(ExcelStyle.MULTIPLE);
        params.defaultCellReader(new DictCellReader());
        // 开始读取Excel，使用Handler处理
        ImportHandler<E> importHandler = ReflectUtil.newInstance(params.getStyle().getImportHandlerClass());
        List<E> result = importHandler.read(inputStream, clazz, params);
        IoUtil.closeIfPosible(inputStream);
        return result;
    }
    /**
     * 导出Excel，支持字典解析
     * @author Kthirty
     * @since 2023/11/26
     * @param list 数据列表
     * @param clazz 实体类型
     * @param outputStream 输出流（不会自动关闭）
     * @param params 额外参数
     */
    public static <E> void exp(List<E> list,Class<E> clazz, OutputStream outputStream, ExcelParams params){
        params = ObjUtil.defaultIfNull(params,new ExcelParams());
        params.defaultGroups(ExcelGroup.IMPORT);
        params.defaultStyle(ExcelStyle.MULTIPLE);
        params.defaultCellWriter(new DictCellWriter());
        params.defaultCellStyleEditor(new DefaultCellStyleEditor());
        ExportHandler<E> exportHandler = ReflectUtil.newInstance(params.getStyle().getExportHandlerClass());
        ExcelWriter writer = exportHandler.write(list, clazz, params);
        writer.flush(outputStream);
        IoUtil.close(writer);
    }

    public static void main(String[] args) {
        testExp();
    }
    public static void testWrite(){
        ExcelWriter writer = cn.hutool.poi.excel.ExcelUtil.getWriter(true);
        List<TestUser> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TestUser testUser = new TestUser();
            testUser.setAge(i);
            testUser.setUsername("username" + i);
            testUser.setRealName("姓名" + i);
            users.add(testUser);
        }
        writer.write(users);
        writer.setSheet("sheet2");
        writer.write(users);
        BufferedOutputStream outputStream = FileUtil.getOutputStream("D:\\System\\Desktop\\test-exp.xlsx");
        writer.flush(outputStream);
    }

    public static void testExp(){
        List<TestUser> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TestUser testUser = new TestUser();
            testUser.setAge(i);
            testUser.setUsername("username"+i);
            testUser.setRealName("姓名"+i);
            List<TestAccount> accounts = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                TestAccount testAccount = new TestAccount();
                testAccount.setType(RandomUtil.randomEle(new String[]{"wx","zfb","xj"}));
                testAccount.setBalance(RandomUtil.randomDouble(100,1000));
                accounts.add(testAccount);
            }
            testUser.setAccounts(accounts);
            users.add(testUser);
        }
        BufferedOutputStream outputStream = FileUtil.getOutputStream("D:\\System\\Desktop\\test-exp.xlsx");
        ExcelUtil.exp(users,TestUser.class,outputStream,null);
    }
    public static void testImp(){
        String path = "D:\\System\\Desktop\\test.xlsx";
        BufferedInputStream inputStream = FileUtil.getInputStream(path);
        List<TestUser> imp = ExcelUtil.imp(inputStream, TestUser.class, null);
        System.out.println(JSONUtil.toJsonPrettyStr(imp));
    }
}