package cn.kunakun.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExeclUtil {
    static int sheetsize = 5000;

    /**
     * @author Lyy
     * @param data
     *            导入到excel中的数据
     * @param out
     *            数据写入的文件
     * @param fields
     *            需要注意的是这个方法中的map中：每一列对应的实体类的英文名为键，excel表格中每一列名为值
     * @throws Exception
     */
    public static <T> void ListtoExecl(List<T> data, OutputStream out,
            Map<String, String> fields) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 如果导入数据为空，则抛出异常。
        if (data == null || data.size() == 0) {
            workbook.close();
            throw new Exception("导入的数据为空");
        }
        // 根据data计算有多少页sheet
        int pages = data.size() / sheetsize;
        if (data.size() % sheetsize > 0) {
            pages += 1;
        }
        // 提取表格的字段名（英文字段名是为了对照中文字段名的）
        String[] egtitles = new String[fields.size()];
        String[] cntitles = new String[fields.size()];
        Iterator<String> it = fields.keySet().iterator();
        int count = 0;
        while (it.hasNext()) {
            String egtitle = (String) it.next();
            String cntitle = fields.get(egtitle);
            egtitles[count] = egtitle;
            cntitles[count] = cntitle;
            count++;
        }
        // 添加数据
        for (int i = 0; i < pages; i++) {
            int rownum = 0;
            // 计算每页的起始数据和结束数据
            int startIndex = i * sheetsize;
            int endIndex = (i + 1) * sheetsize - 1 > data.size() ? data.size()
                    : (i + 1) * sheetsize - 1;
            // 创建每页，并创建第一行
            HSSFSheet sheet = workbook.createSheet();
            HSSFRow row = sheet.createRow(rownum);

            // 在每页sheet的第一行中，添加字段名
            for (int f = 0; f < cntitles.length; f++) {
                HSSFCell cell = row.createCell(f);
                cell.setCellValue(cntitles[f]);
            }
            rownum++;
            // 将数据添加进表格
            for (int j = startIndex; j < endIndex; j++) {
                row = sheet.createRow(rownum);
                T item = data.get(j);
                for (int h = 0; h < cntitles.length; h++) {
                    Field fd = item.getClass().getDeclaredField(egtitles[h]);
                    fd.setAccessible(true);
                    Object o = fd.get(item);
                    String value = o == null ? "" : o.toString();
                    HSSFCell cell = row.createCell(h);
                    cell.setCellValue(value);
                }
                rownum++;
            }
        }
        // 将创建好的数据写入输出流
        workbook.write(out);
        // 关闭workbook
        workbook.close();
    }
    /**
 　　* @author Lyy
 　　* @param entityClass excel中每一行数据的实体类
 　　* @param in          excel文件
 　　* @param fields     字段名字
 　　*             需要注意的是这个方法中的map中：
 　　*             excel表格中每一列名为键，每一列对应的实体类的英文名为值
　　 * @throws Exception
 　　*/
    public static <T> List<T> ExecltoList(InputStream in, Class<T> entityClass,
            Map<String, String> fields) throws Exception {

        List<T> resultList = new ArrayList<T>();

        HSSFWorkbook workbook = new HSSFWorkbook(in);

        // excel中字段的中英文名字数组
        String[] egtitles = new String[fields.size()];
        String[] cntitles = new String[fields.size()];
        Iterator<String> it = fields.keySet().iterator();
        int count = 0;
        while (it.hasNext()) {
            String cntitle = (String) it.next();
            String egtitle = fields.get(cntitle);
            egtitles[count] = egtitle;
            cntitles[count] = cntitle;
            count++;
        }

        // 得到excel中sheet总数
        int sheetcount = workbook.getNumberOfSheets();

        if (sheetcount == 0) {
            workbook.close();
            throw new Exception("Excel文件中没有任何数据");
        }

        // 数据的导出
        for (int i = 0; i < sheetcount; i++) {
            HSSFSheet sheet = workbook.getSheetAt(i);
            if (sheet == null) {
                continue;
            }
            // 每页中的第一行为标题行，对标题行的特殊处理
            HSSFRow firstRow = sheet.getRow(0);
            int celllength = firstRow.getLastCellNum();

            String[] excelFieldNames = new String[celllength];
            LinkedHashMap<String, Integer> colMap = new LinkedHashMap<String, Integer>();

            // 获取Excel中的列名
            for (int f = 0; f < celllength; f++) {
                HSSFCell cell = firstRow.getCell(f);
                excelFieldNames[f] = cell.getStringCellValue().trim();
                // 将列名和列号放入Map中,这样通过列名就可以拿到列号
                for (int g = 0; g < excelFieldNames.length; g++) {
                    colMap.put(excelFieldNames[g], g);
                }
            }
            // 由于数组是根据长度创建的，所以值是空值，这里对列名map做了去空键的处理
            colMap.remove(null);
            // 判断需要的字段在Excel中是否都存在
            // 需要注意的是这个方法中的map中：中文名为键，英文名为值
            boolean isExist = true;
            List<String> excelFieldList = Arrays.asList(excelFieldNames);
            for (String cnName : fields.keySet()) {
                if (!excelFieldList.contains(cnName)) {
                    isExist = false;
                    break;
                }
            }
            // 如果有列名不存在，则抛出异常，提示错误
            if (!isExist) {
                workbook.close();
                throw new Exception("Excel中缺少必要的字段，或字段名称有误");
            }
            // 将sheet转换为list
            for (int j = 1; j <= sheet.getLastRowNum(); j++) {
                HSSFRow row = sheet.getRow(j);
                // 根据泛型创建实体类
                T entity = entityClass.newInstance();
                // 给对象中的字段赋值
                for (Entry<String, String> entry : fields.entrySet()) {
                    // 获取中文字段名
                    String cnNormalName = entry.getKey();
                    // 获取英文字段名
                    String enNormalName = entry.getValue();
                    // 根据中文字段名获取列号
                    int col = colMap.get(cnNormalName);
                    // 获取当前单元格中的内容
                    String content = row.getCell(col).toString().trim();
                    // 给对象赋值
                    setFieldValueByName(enNormalName, content, entity);
                }
                resultList.add(entity);
            }
        }
        workbook.close();
        return resultList;
    }

    /**
     * @MethodName : setFieldValueByName
     * @Description : 根据字段名给对象的字段赋值
     * @param fieldName
     *            字段名
     * @param fieldValue
     *            字段值
     * @param o
     *            对象
     */
    private static void setFieldValueByName(String fieldName,
            Object fieldValue, Object o) throws Exception {

        Field field = getFieldByName(fieldName, o.getClass());
        if (field != null) {
            field.setAccessible(true);
            // 获取字段类型
            Class<?> fieldType = field.getType();

            // 根据字段类型给字段赋值
            if (String.class == fieldType) {
                field.set(o, String.valueOf(fieldValue));
            } else if ((Integer.TYPE == fieldType)
                    || (Integer.class == fieldType)) {
                field.set(o, Integer.parseInt(fieldValue.toString()));
            } else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
                field.set(o, Long.valueOf(fieldValue.toString()));
            } else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
                field.set(o, Float.valueOf(fieldValue.toString()));
            } else if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
                field.set(o, Short.valueOf(fieldValue.toString()));
            } else if ((Double.TYPE == fieldType)
                    || (Double.class == fieldType)) {
                field.set(o, Double.valueOf(fieldValue.toString()));
            } else if (Character.TYPE == fieldType) {
                if ((fieldValue != null)
                        && (fieldValue.toString().length() > 0)) {
                    field.set(o,
                            Character.valueOf(fieldValue.toString().charAt(0)));
                }
            } else if (Date.class == fieldType) {
                field.set(o, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .parse(fieldValue.toString()));
            } else {
                field.set(o, fieldValue);
            }
        } else {
            throw new Exception(o.getClass().getSimpleName() + "类不存在字段名 "
                    + fieldName);
        }
    }

    /**
     * @MethodName : getFieldByName
     * @Description : 根据字段名获取字段
     * @param fieldName
     *            字段名
     * @param clazz
     *            包含该字段的类
     * @return 字段
     */
    private static Field getFieldByName(String fieldName, Class<?> clazz) {
        // 拿到本类的所有字段
        Field[] selfFields = clazz.getDeclaredFields();

        // 如果本类中存在该字段，则返回
        for (Field field : selfFields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }

        // 否则，查看父类中是否存在此字段，如果有则返回
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null && superClazz != Object.class) {
            return getFieldByName(fieldName, superClazz);
        }

        // 如果本类和父类都没有，则返回空
        return null;
    }
}