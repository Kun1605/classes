package cn.kunakun.mapper.provider;

import cn.kunakun.pojo.Student;
import cn.kunakun.utils.StringBuilderHolder;


public class StudentMapperProvider {
    public String queryStudentById(Student student){
        StringBuilder sql = StringBuilderHolder.getGlobal();
        sql.append(" SELECT ");
        sql.append(" t_student.id, ");
        sql.append(" t_student.student_no AS studentNo, ");
        sql.append(" t_student.`password`, ");
        sql.append(" t_student.class_id AS classId, ");
        sql.append(" t_student.`name`, ");
        sql.append(" t_student.sex, ");
        sql.append(" t_student.phone, ");
        sql.append(" t_student.email, ");
        sql.append(" t_student.avatar, ");
        sql.append(" t_student.birth, ");
        sql.append(" t_student.hobbies, ");
        sql.append(" t_student.from_city, ");
        sql.append(" t_student.type, ");
        sql.append(" t_student.qq, ");
        sql.append(" t_student.wx, ");
        sql.append(" t_student.position, ");
        sql.append(" t_student.`status`, ");
        sql.append(" t_student.create_time, ");
        sql.append(" t_student.update_time ");
        sql.append(" FROM ");
        sql.append(" t_student ");
        sql.append(" WHERE ");
        sql.append(" t_student.`name` = '杨坤' ");
        return sql.toString();
    }
}
