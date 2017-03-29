package cassandra;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import java.io.File;
import java.util.Iterator;

/**
 * @author: kevin.
 * @date: 2017/3/24 0024.
 * @description:
 */
public class Dom4jTest {


    @Test
    public void test() throws Exception {

        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new File("src/test/java/cassandra/demo.xml"));

        //获取根元素
        Element root = document.getRootElement();
        System.out.println("Root:"+root.getName());

        //获取所有子元素
       // List<Element> childList = root.elements();
       // System.out.println("total child count:"+childList.size());

        //获取特定名称的子元素
        //List<Element> childList2 = root.elements("sql");

        //Element firstElement = root.element("sql");
        for(Iterator iterator = root.elementIterator();iterator.hasNext();){
            Element e = (Element)iterator.next();
            System.out.println("id==="+e.attribute("id").getValue()+"    value==="+e.attributeValue("id"));

            System.out.println("text========="+e.getText());
        }



    }
}
