package cn.kunakun.thread;

import cn.kunakun.pojo.Student;

public class StudentThreadLocal {

    private static final ThreadLocal<Student> LOCAL = new ThreadLocal<Student>();

    private StudentThreadLocal() {

    }

    public static void set(Student student) {
        LOCAL.set(student);
    }

    public static Student get() {
        return LOCAL.get();
    }

}
