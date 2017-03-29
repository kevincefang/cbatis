package cassandra;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: kevin.
 * @date: 2017/3/24 0024.
 * @description: 占位符替换工具类
 */
public final class PlaceholderUtils {

    /**
     * Prefix for system property placeholders: "#{"
     */
    private static final String PLACEHOLDER_PREFIX = "#{";
    /**
     * Suffix for system property placeholders: "}"
     */
    private static final String PLACEHOLDER_SUFFIX = "}";

    static <K,V> String  resolvePlaceholders(String text, Map<K, V> parameter) {
        if(text == null) throw new RuntimeException("自定义SQL文件内容为空,请检查配置信息!");
        if (parameter == null || parameter.isEmpty()) {
            return text;
        }
        StringBuilder buf = new StringBuilder(text);
        int startIndex = buf.indexOf(PLACEHOLDER_PREFIX);
        while (startIndex != -1) {
            int endIndex = buf.indexOf(PLACEHOLDER_SUFFIX, startIndex + PLACEHOLDER_PREFIX.length());
            if (endIndex != -1) {
                String placeholder = buf.substring(startIndex + PLACEHOLDER_PREFIX.length(), endIndex);
                int nextIndex = endIndex + PLACEHOLDER_SUFFIX.length();
                try {
                    Object propVal = parameter.get(placeholder);
                    if (propVal != null) {
                        String propValString = null;
                        if(propVal instanceof String){
                            propValString = propVal.toString();
                            propValString = "'" + propValString + "'";
                        }else if(propVal instanceof Integer){
                            propValString = String.valueOf(propVal);
                        }else if(propVal instanceof Long){
                            propValString = String.valueOf(propVal);
                        }else if(propVal instanceof Float){
                            propValString = String.valueOf(propVal);
                        }else if(propVal instanceof Double){
                            propValString = String.valueOf(propVal);
                        }else if(propVal instanceof Boolean){
                            propValString = String.valueOf(propVal);
                        }else if(propVal instanceof UUID){
                            propValString = String.valueOf(propVal);
                        }else if(propVal instanceof List){
                            propValString = CassandraUtils.convertListToString((List)propVal);
                        }else if(propVal instanceof Map){
                            propValString = CassandraUtils.convertMapToString((Map)propVal);
                        }else if(propVal instanceof Set){
                            propValString = CassandraUtils.convertSetToString((Set)propVal);
                        }else if(propVal instanceof Date){
                            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                            String str=sdf.format(propVal);
                            propValString = "'" + str + "'";
                        }
                        else{
                            throw new RuntimeException("未知类型，解析失败.");
                        }
                        System.out.println("propVal==="+ propValString);
                        buf.replace(startIndex, endIndex + PLACEHOLDER_SUFFIX.length(), propValString);
                        nextIndex = startIndex + propValString.length();
                    } else {
                        buf.replace(startIndex, endIndex + PLACEHOLDER_SUFFIX.length(), "null");
                        nextIndex = startIndex + 4;
                       // System.out.println("Could not resolve placeholder '" + placeholder + "' in [" + text + "] ");
                    }
                } catch (Exception ex) {
                    System.out.println("Could not resolve placeholder '" + placeholder + "' in [" + text + "]: " + ex);
                }
                startIndex = buf.indexOf(PLACEHOLDER_PREFIX, nextIndex);
            } else {
                startIndex = -1;
            }
        }
        return buf.toString();
    }


    public static void main(String[] args) {
        /*String aa= "我们都是好孩子,#{num}说是嘛？ 我觉得#{people}是傻蛋!";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("num",22);
        map.put("people","小明");
        System.out.println(PlaceholderUtils.resolvePlaceholders(aa, map));*/

        Object a = 12.0f;
        List<String> list  = new ArrayList<>();
        list.add("a");
        list.add("b");

        System.out.println(list.toString());

    }
}
