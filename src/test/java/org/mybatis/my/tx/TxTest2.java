package org.mybatis.my.tx;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:org/mybatis/my/tx/application-context.xml"})
public class TxTest2 {

    @Resource
    private StudentMapper studentMapper;
    @Resource
    private DataSourceTransactionManager txManager;

    @Test
    public void testTx1() {
        System.out.println(studentMapper.getAllStudent());
    }

}
