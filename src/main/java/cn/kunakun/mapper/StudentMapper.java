package cn.kunakun.mapper;

import java.util.List;

import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

import cn.kunakun.common.mapper.MyMapper;
import cn.kunakun.mapper.provider.StudentMapperProvider;
import cn.kunakun.pojo.Student;

@Repository
public interface StudentMapper extends MyMapper<Student> {
    @SelectProvider(type = StudentMapperProvider.class, method = "queryStudentById")
    @Results(value = {
            @Result(column = "classId",
                    property = "depart",
                    one = @One(select = "cn.kunakun.mapper.DepartMapper.selectByPrimaryKey",
                    fetchType = FetchType.EAGER))})
    List<Student> queryStudentById(Student student);

    List<Student> queryAllStudent(Student student);

}
