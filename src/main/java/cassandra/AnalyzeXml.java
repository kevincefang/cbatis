package cassandra;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

/**
 * @author: kevin.
 * @date: 2017/3/24 0024.
 * @description: 解析自定义xml
 */
public final class AnalyzeXml {

    private AnalyzeXml(){}

    private static final String XML_PATH = "/cassandra/";

    private static final String SQL_ID = "id";

    private static Element root = null;


    /**
     * 加载XML文件
     * @param xmlFileName
     * @return
     * @throws Exception
     */
    public static AnalyzeXml loadXmlFile(String xmlFileName) throws Exception {

        System.out.println("start to load xmlFile fileName ====>>  "+xmlFileName);
        SAXReader saxReader = new SAXReader();
        String filePath = XML_PATH + xmlFileName;
        InputStream inputStream = Thread.currentThread().getClass().getResourceAsStream(filePath);
        Document document = saxReader.read(inputStream);
        //获取根元素
        root = document.getRootElement();
        System.out.println("load xmlFile fileName ==>>  "+xmlFileName +" had finished.");
        return new AnalyzeXml();
    }


    /**
     * 获取XML的cql内容
     * @param cqlId cqlId 名称
     * @param params 请求的参数
     * @return
     */
    public String getCqlFromXml(String cqlId,Map<String,Object> params){

        System.out.println("invoke AnalyzeXml.getXmlContent requestParam cqlId ====  "+cqlId);
        String text = null;
        for(Iterator iterator = root.elementIterator(); iterator.hasNext();){
            Element e = (Element)iterator.next();
            if(e.attributeValue(SQL_ID).equals(cqlId)){
                text =  e.getText();
                //System.out.println("id==="+id +"    value==="+e.getText());
            }
        }
        String attr = PlaceholderUtils.resolvePlaceholders(text,params);
        String result = StringUtils.trim(attr);

        System.out.println("invoke result return cql ====>>  "+ StringUtils.trim(attr));
        return result;
    }

    /**
     * 无请求参数查询xml的cql语句
     * @param cqlId
     * @return
     */
    public String getXmlContent(String cqlId){
        return getCqlFromXml(cqlId,null);
    }


}
