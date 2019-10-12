package org.mybatis.my.tx;


import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface StudentMapper {

    Student selectById(Long id);

    void insert(Student stu);

    @Select("select * from student")
    List<Student> getAllStudent();
}
