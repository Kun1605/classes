package cn.kunakun;

import cn.kunakun.pojo.Student;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;


public class ResultMapUtil {

    // 获取bean的属性 根据属性评价 resultMap
    public static String getResultMap(Class<?> cls, String prefix) throws Exception {
        String prefix_ = prefix + "_";
        String str = "";
        // 每一行字符串 <result column="BID_SECTION_CODE" property="BID_SECTION_CODE"
        // jdbcType="VARCHAR" />
        String linestr = "";
        Field[] declaredFields = cls.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getType().getName().equals("java.lang.String")) {
                linestr = "<result column=\"" + prefix_ + field.getName() + "\" property=\"" + prefix+"."+  field.getName()
                        + "\" jdbcType=\"VARCHAR\" />";
            } else {
                linestr = "<result column=\"" + prefix_ + field.getName() + "\" property=\"" + prefix + "." + field.getName()
                        + "\" jdbcType=\"INTEGER\" />";
            }
            System.err.println(linestr);
        }

        return str;
    }

    // 获取bean的属性 根据属性评价 resultMap
    // 并将驼峰修改为'_'
    public static String getResultMapNew(Class<?> cls) throws Exception {
        String str = "";
        // 头部 <resultMap id="BaseResultMap" type="com.huajie.entity.sys.SysMenuinfo">
        str = "<resultMap id=\"" + cls.getSimpleName() + "ResultMap\" type=\"" + cls.getName() + "\"> \r\n";
        // 每一行字符串
        String linestr = "";
        Field[] declaredFields = cls.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getType().getName().equals("java.lang.String")) {
                linestr = "<result column=\"" + getUpCaseReplace(field.getName()) + "\" property=\"" + field.getName()
                        + "\" jdbcType=\"VARCHAR\" />";
                linestr += "\r\n";
            } else if (field.getType().getName().equals("java.lang.Long")) {
                linestr = "<result column=\"" + getUpCaseReplace(field.getName()) + "\" property=\"" + field.getName()
                        + "\" jdbcType=\"BIGINT\" />";
                linestr += "\r\n";
            } else if (field.getType().getName().equals("java.lang.Integer")) {
                linestr = "<result column=\"" + getUpCaseReplace(field.getName()) + "\" property=\"" + field.getName()
                        + "\" jdbcType=\"INTEGER\" />";
                linestr += "\r\n";
            } else if (field.getType().getName().equals("java.util.Date")) {
                linestr = "<result column=\"" + getUpCaseReplace(field.getName()) + "\" property=\"" + field.getName()
                        + "\" jdbcType=\"DATE\" />";
                linestr += "\r\n";
            } else if (field.getType().getName().equals("java.util.Date")) {
                linestr = "<result column=\"" + getUpCaseReplace(field.getName()) + "\" property=\"" + field.getName()
                        + "\" jdbcType=\"DATE\" />";
                linestr += "\r\n";
            } else if (field.getType().getName().equals("java.util.Boolean")) {
                linestr = "<result column=\"" + getUpCaseReplace(field.getName()) + "\" property=\"" + field.getName()
                        + "\" jdbcType=\"BOOLEAN \" />";
                linestr += "\r\n";
            } else if (field.getType().getName().equals("java.util.Float")) {
                linestr = "<result column=\"" + getUpCaseReplace(field.getName()) + "\" property=\"" + field.getName()
                        + "\" jdbcType=\"FLOAT \" />";
                linestr += "\r\n";
            } else if (field.getType().getName().equals("java.util.Double")) {
                linestr = "<result column=\"" + getUpCaseReplace(field.getName()) + "\" property=\"" + field.getName()
                        + "\" jdbcType=\"DOUBLE\" />";
                linestr += "\r\n";
            } else if (field.getType().getName().equals("java.sql.Time")) {
                linestr = "<result column=\"" + getUpCaseReplace(field.getName()) + "\" property=\"" + field.getName()
                        + "\" jdbcType=\"TIME\" />";
                linestr += "\r\n";
            } else if (field.getType().getName().equals("java.sql.Timestamp")) {
                linestr = "<result column=\"" + getUpCaseReplace(field.getName()) + "\" property=\"" + field.getName()
                        + "\" jdbcType=\"TIMESTAMP\" />";
                linestr += "\r\n";
            } else {
                continue;
            }
            str += linestr;
        }
        str += "</resultMap>";
        return str;
    }

    // 获取Base_Column_List sql语句字段
    public static String getColumnList(Class<?> cls) throws Exception {
        // 每一行字符串 <result column="BID_SECTION_CODE" property="BID_SECTION_CODE"
        // jdbcType="VARCHAR" />
        String linestr = "";
        Field[] declaredFields = cls.getDeclaredFields();
        for (Field field : declaredFields) {
            linestr = linestr + field.getName() + ",";
        }
        String str = linestr.substring(0, linestr.length() - 1);
        System.out.println(str);
        return str;
    }

    /**
     * 将字符串中的驼峰写法替换成'_'
     *
     * @param str
     * @return
     */
    private static String getUpCaseReplace(String str) {
        List<String> listChar = getUpCaseList(str);
        for (int i = 0; i < listChar.size(); i++) {
            str = str.replace(listChar.get(i), "_" + listChar.get(i).toLowerCase());
        }
        return str;
    }

    /**
     * @param str
     * @Description: 输出字符串中的大写字母
     */
    private static List<String> getUpCaseList(String str) {
        List<String> listChar = new ArrayList<String>();
        // 转为char数组
        char[] ch = str.toCharArray();
        // 得到大写字母
        for (int i = 0; i < ch.length; i++) {
            if (ch[i] >= 'A' && ch[i] <= 'Z') {
                listChar.add(String.valueOf(ch[i]));
            }
        }
        return listChar;
    }

    /**
     * @param str
     * @Description: 输出字符串中的大写字母
     */
    private static String getColumnListNew(Class<?> cls) throws Exception {
        // 每一行字符串 <result column="BID_SECTION_CODE" property="BID_SECTION_CODE"
        // jdbcType="VARCHAR" />
        String ac = "java.lang.Longjava.util.Datejava.lang.Longjava.sql.TimeStamp";
        String linestr = " <sql id=\"Base_Column_List\" >";
        Field[] declaredFields = cls.getDeclaredFields();
        for (Field field : declaredFields) {

            String typename = field.getType().getName();
            linestr = linestr + getUpCaseReplace(field.getName()) + ",";
        }
        String str = linestr.substring(0, linestr.length() - 1);
        System.out.println(str + " </sql>");
        return str;
    }

    public static void main(String[] args) throws Exception {
        Student student = new Student();
        getColumnListNew(student.getClass());

        System.out.println(getResultMapNew(student.getClass()));


    }

    public static void a() {
        System.out.print("a");
    }public static void b() {
        System.out.print("b");
    }public static void c() {
        System.out.print("c");
    }public static void d() {
        System.out.print("d");
    }
}
