package org.mybatis.my.tx;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;


public class SqlSessionTemplateTest1 {

    @Test
    public void testCase() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("org/mybatis/my/tx/application-context.xml");
        SqlSessionTemplate template = (SqlSessionTemplate) context.getBean("readSqlSession");
        StudentMapper mapper = template.getMapper(StudentMapper.class);
        Student student = mapper.selectById(1L);
        System.out.println(student);
    }

    @Test
    public void testTx() {
        ApplicationContext context = new ClassPathXmlApplicationContext("org/mybatis/my/tx/application-context.xml");
        SqlSessionTemplate template = (SqlSessionTemplate) context.getBean("readSqlSession");
        StudentMapper mapper = template.getMapper(StudentMapper.class);

        //手动提交
        DataSourceTransactionManager txManager = (DataSourceTransactionManager) context.getBean("txManager");
        TransactionDefinition td = new DefaultTransactionDefinition();
        TransactionStatus txStatus = txManager.getTransaction(td);
        DefaultTransactionStatus txStatus1 = (DefaultTransactionStatus) txStatus;
        ConnectionHolder holder = (ConnectionHolder) TransactionSynchronizationManager.getResource(txManager.getDataSource());
        System.out.println(holder.getConnection());
        try {
            mapper.insert(new Student().setName("小明").setNum(2L));

            System.out.println(mapper.getAllStudent());
            //回滚
            int i = 1 / 0;
            txManager.commit(txStatus);
        } catch (Exception e) {
            System.out.println(mapper.getAllStudent());
            System.out.println(e);
            txManager.rollback(txStatus);
            System.out.println(mapper.getAllStudent());
        }
        //TransactionTemplate
//        TransactionTemplate txTemplate = new TransactionTemplate(txManager);
//        txTemplate.execute((status) -> {
//            mapper.insert(new Student().setName("小明").setNum(2L));
//            System.out.println(mapper.getAllStudent());
//            return null;
//        });


    }
}
