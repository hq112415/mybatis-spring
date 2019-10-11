package org.mybatis.my.tx;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


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

    }
}
