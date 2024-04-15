package top.kthirty.excel;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import top.kthirty.core.test.BaseKthirtyTest;
import top.kthirty.core.test.KthirtyTest;
import top.kthirty.core.tool.excel.ExcelUtil;
import top.kthirty.core.tool.excel.support.ExcelHelper;
import top.kthirty.core.tool.excel.support.ExcelParams;
import top.kthirty.core.tool.excel.support.ExcelStyle;
import top.kthirty.system.SystemApplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@KthirtyTest(appName = "system", classes = SystemApplication.class)
@Slf4j
public class TestExcel extends BaseKthirtyTest {

    @Test
    public void test1() {
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

    private List<TestUser> getUser(int i) {
        List<TestUser> list = new ArrayList<>();
        for (int i1 = 0; i1 < i; i1++) {
            TestUser testUser = new TestUser();
            testUser.setName("张三"+i+"-"+i1);
            testUser.setAge(20);
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
                    .setBalance(RandomUtil.randomDouble(1, 100)));
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
                    .setBeforeBalance(RandomUtil.randomDouble(1, 100))
                    .setAfterBalance(RandomUtil.randomDouble(1, 100))
            );
        }
        return list;
    }

}
