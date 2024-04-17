package top.kthirty.excel;

import cn.hutool.core.bean.BeanPath;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import top.kthirty.core.test.BaseKthirtyTest;
import top.kthirty.core.test.KthirtyTest;
import top.kthirty.core.tool.dict.DictItem;
import top.kthirty.core.tool.dict.DictUtil;
import top.kthirty.core.tool.excel.ExcelUtil;
import top.kthirty.core.tool.excel.support.ExcelParams;
import top.kthirty.core.tool.excel.support.ExcelStyle;
import top.kthirty.system.SystemApplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.util.ArrayList;
import java.util.List;

@KthirtyTest(appName = "system", classes = SystemApplication.class)
@Slf4j
public class TestExcel extends BaseKthirtyTest {
    public static void main(String[] args) {
        List<TestUser> users = new ArrayList<>();
        BeanPath.create("[0].name").set(users,"test1");
        BeanPath.create("[0].accounts.name").set(users,"testAccount1");
        log.info(JSONUtil.toJsonPrettyStr(users));
    }

    @Test
    public void test1() {
        FileUtil.del("/System/Desktop/test.xlsx");
        List<TestUser> list = getUser(2);
        BufferedOutputStream outputStream = FileUtil.getOutputStream("/System/Desktop/test.xlsx");
        ExcelParams excelParams = new ExcelParams();
        excelParams.setStyle(ExcelStyle.SINGLE);
        ExcelUtil.exp(list, TestUser.class, outputStream, excelParams);
    }
    @Test
    public void test2(){
        BufferedInputStream inputStream = FileUtil.getInputStream("/System/Desktop/test.xlsx");
        ExcelParams excelParams = new ExcelParams();
        excelParams.setStyle(ExcelStyle.SINGLE);
        List<TestUser> users = ExcelUtil.imp(inputStream, TestUser.class, excelParams);
        log.info("读取数据:\n{}", JSONUtil.toJsonPrettyStr(users));
    }
    @Test
    public void test3(){
        FileUtil.del("/System/Desktop/test.xlsx");
        List<TestUser> list = getUser(2);
        BufferedOutputStream outputStream = FileUtil.getOutputStream("/System/Desktop/test.xlsx");
        ExcelParams excelParams = new ExcelParams();
        excelParams.setStyle(ExcelStyle.SINGLE);
        ExcelUtil.exp(list, TestUser.class, outputStream, excelParams);

        BufferedInputStream inputStream = FileUtil.getInputStream("/System/Desktop/test.xlsx");
        List<TestUser> users = ExcelUtil.imp(inputStream, TestUser.class, excelParams);
        String org = JSONUtil.toJsonPrettyStr(list);
        String read = JSONUtil.toJsonPrettyStr(users);
        log.info("equals {}" ,StrUtil.equals(org,read));
        log.info(org);
        log.info(read);
    }

    @Test
    public void test4(){
        List<DictItem> sexDict= new ArrayList<>();
        sexDict.add(DictItem.builder().value("1").label("男").build());
        sexDict.add(DictItem.builder().value("2").label("女").build());
        sexDict.add(DictItem.builder().value("3").label("未知").build());
        DictUtil.add("sex",sexDict);
        String filePath = "/System/Desktop/test1.xlsx";
        ExcelParams excelParams = new ExcelParams();
        excelParams.setStyle(ExcelStyle.MULTIPLE);
//        FileUtil.del(filePath);
//        List<TestUser> list = getUser(5);
//        BufferedOutputStream outputStream = FileUtil.getOutputStream(filePath);

//        ExcelUtil.exp(list, TestUser.class, outputStream, excelParams);

        BufferedInputStream inputStream = FileUtil.getInputStream(filePath);
        List<TestUser> users = ExcelUtil.imp(inputStream, TestUser.class, excelParams);
        String read = JSONUtil.toJsonPrettyStr(users);
        log.info(read);
    }

    private List<TestUser> getUser(int i) {
        List<TestUser> list = new ArrayList<>();
        for (int i1 = 0; i1 < i; i1++) {
            TestUser testUser = new TestUser();
            testUser.setName("张三"+i+"-"+i1);
            testUser.setAge(20);
            testUser.setSex(RandomUtil.randomEle(new String[]{"1","2","3"}));
            testUser.setAccounts(getTestAccount(2));
            testUser.setAddresses(getAddress(3));
            list.add(testUser);
        }
        return list;
    }

    private List<TestAccount> getTestAccount(int count) {
        List<TestAccount> list = new ArrayList<>();
        for (int i1 = 0; i1 < count; i1++) {
            list.add(new TestAccount()
                    .setRecords(getRecord(5))
                    .setName("张"+i1)
                    .setBalance(1D * RandomUtil.randomInt(1, 100)));
        }
        return list;
    }

    private List<TestAddress> getAddress(int count) {
        List<TestAddress> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(new TestAddress()
                    .setDescription("Description"+i)
                    .setLocation("Location"+i));
        }
        return list;
    }

    private List<TestAccountRecord> getRecord(int count) {
        List<TestAccountRecord> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(new TestAccountRecord().setRemarks("Remarks"+i)
                    .setBeforeBalance(1D * RandomUtil.randomInt(1, 100))
                    .setAfterBalance(1D * RandomUtil.randomInt(1, 100))
            );
        }
        return list;
    }

}
