package cassandra;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author: kevin.
 * @date: 2017/3/24 0024.
 * @description: cassandra工具包
 */
public final class CassandraUtils {

    /**
     * 将List<String>转换为 cassandra要求的list<Text>格式
     * for example: ["aa","bb"]  ==>> ['aa','bb']
     * @param reqList
     * @return
     */
    protected static <T> String convertListToString(List<T> reqList){
        String result = "";
        if(CollectionUtils.isEmpty(reqList)){
            return null;
        }
        int size = reqList.size();
        Object[] arr = reqList.toArray(new Object[size]);
        StringBuilder sb = new StringBuilder("[");
        for (Object anArr : arr) {
            if (anArr != null) {
                if(anArr instanceof String){
                    sb.append("'").append(anArr).append("'").append(",");
                }else {
                    sb.append(anArr).append(",");
                }

            }
        }
        String temp = sb.toString();
        result = temp.substring(0,temp.length()-1);
        result += "]";
        return result;
    }


    /**
     * 将Set<String> 转换为cassandra要求的set<Text>
     * for example:['aa','bb']  ==>> {'aa','bb'}
     * @param reqSet
     * @return
     */
    protected static <T> String convertSetToString(Set<T> reqSet){
        String result = "";
        if(CollectionUtils.isEmpty(reqSet)){
            return null;
        }
        int size = reqSet.size();
        Object[] arr = reqSet.toArray(new Object[size]);
        StringBuilder sb = new StringBuilder("{");
        for (Object anArr : arr) {
            if (anArr != null) {

                if(anArr instanceof String){
                    sb.append("'").append(anArr).append("',");
                }else{
                    sb.append(anArr).append(",");
                }

            }
        }
        String temp = sb.toString();
        result = temp.substring(0,temp.length()-1);
        result += "}";
        return result;
    }


    /**
     * 将Map<String,String> 转换为cassandra 要求的map<Text,Text>
     * for example: {"a":"xxx","b":"xxx"} ==>> {'a':'xxx','b':'xxx'}
     * @param map
     * @return
     */
    protected static <K,V> String convertMapToString(Map<K,V> map){
        String result = "";
        if (map == null){
            return null;
        }
        StringBuilder sb = new StringBuilder("{");
        Set<Map.Entry<K, V>> entrySet = map.entrySet();
        for(Map.Entry m : entrySet){
            if(m.getKey() instanceof String){
                sb.append("'").append(m.getKey()).append("':");
            }else{
                sb.append(m.getValue()).append(":");
            }

            if(m.getValue() instanceof String){
                sb.append("'").append(m.getValue()).append("',");
            }else{
                sb.append(m.getValue()).append(",");
            }

        }
        String temp = sb.toString();
        result = temp.substring(0,temp.length()-1);
        result += "}";
        return result;
    }

}
