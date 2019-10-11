package org.mybatis.my.tx;


public interface StudentMapper {

    Student selectById(Long id);

    void insert(Student stu);
}
