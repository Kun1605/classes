package cn.kunakun.common.pojo;

import cn.kunakun.pojo.UserFunction;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * @author YangKun
 * 2018年1月13日 下午9:53:16
 */
public class PageVO<T> implements java.io.Serializable {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private String sort;
    private String order;
    private Integer offset = 0;
    private Integer limit = 10;
    private String filter;
    private String op;

    private Integer page = 0;
    private Integer rows = 10;

    private T t;

    public Integer getPage() {
        if (this.offset != 0) {
            return (offset / limit) + 1;
        }
        return page;
    }

    public Integer getRows() {
        return limit;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
    public String getOp() {
        return op;
    }
    public void setOp(String op) {
        this.op = op;
    }
    public void setPage(Integer page) {
        this.page = page;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }
    public void setT(T t) {
        this.t = t;
    }
    public T getT(T t)  {
        try {
            String s = jStoJson(getFilter());
            System.out.println(s);
            Object value = OBJECT_MAPPER.readValue(s, t.getClass());
            System.out.println(value.toString());
            return (T) value;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //把js换成json
    public static String jStoJson(String str) {
        if (!str.contains(".") ){
            return str;
        }
        String substringAfter = StringUtils.substringAfter(str, "{\"");
        String key = StringUtils.substringBefore(substringAfter,".");
        String value = StringUtils.replaceOnce(str, key+".", "");
        String result = "{\"" + key + "\":" + value + "}";
        return jStoJson(result);
    }

    public static void main(String[] args) {
        String jStoJson = jStoJson("{\"studentNo\":\"dsad\"}");
        System.out.println(jStoJson);
    }
}
