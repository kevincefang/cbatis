package cassandra;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;


public class CassandraTest {

    public static Session session;

    @BeforeClass
    public static void connect(){

        session = CassandraClientManager.newInstance().getSession();

    }



    @Test
    public void createKeyspace(){
        String sql = "create keyspace if not exists test_keyspace with replication ={'class': 'SimpleStrategy', 'replication_factor': '1'}";
        session.execute(sql);
    }



    @Test
    public void createTable(){
        String sql = "create table if not exists test_keyspace.person (id uuid primary key, name text ,age int ,sex text,tags set<text>,data blob);";
        session.execute(sql);
    }

    @Test
    public void insert(){

        String sql = "insert into test_keyspace.person (id,name,age,sex,tags,data) values(uuid(),'aa',21,'male',{'a','b','c'},null);";
        session.execute(sql);
    }

    @Test
    public void selectPerson(){
        Map<String,Object> map = new HashMap<>();

        try {

            String sql = AnalyzeXml.loadXmlFile("person.xml").getXmlContent("selectAll");

            //String sql = "select * from person where name = #{name}";
            //PreparedStatement prepareStatement = session.prepare(sql);

            //BoundStatement bindStatement = new BoundStatement(prepareStatement).bind("aa");
            ResultSet resultSet = session.execute(sql);

            Iterator<Row> iterator =  resultSet.iterator();
            while(iterator.hasNext()){
                Row row = iterator.next();
                System.out.print("id======="+row.getUUID("id")+"\tname======"+row.getString("name")+ "\tage====="+row.getInt("age") + "\tmap====="+row.getMap("map",String.class,String.class));
                System.out.println("\tnumbers======"+row.getList("numbers",Integer.class)+"\ttags=========="+row.getSet("tags",String.class));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void addPerson(){
        Map<String,Object> reqMap = new HashMap<>();
        reqMap.put("id", UUIDs.random());
        reqMap.put("name","kevin22222222");
        reqMap.put("age",20);
        reqMap.put("tags",null);
        List<Object> list  = new ArrayList<>();
        list.add("55");
        reqMap.put("hobby", list);
        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        list2.add(1);
        list2.add(3);
        reqMap.put("numbers",list2);

        Set<String> tags = new HashSet<>();
        tags.add("12");
        tags.add("12");
        tags.add("13");
        tags.add("14");

        Map<String,Object> map = new HashMap<>();
        map.put("pid","123");
        map.put("name","kevin");

        reqMap.put("map",map);


        reqMap.put("tags",tags);
        try {
            String sql = AnalyzeXml.loadXmlFile("person.xml").getCqlFromXml("addPerson",reqMap);

            System.out.println(session.execute(sql).isExhausted());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void updatePerson(){
        Map<String,Object> reqMap = new HashMap<>();
        reqMap.put("id", UUID.fromString("af79a536-b1df-4aec-976a-76ec6e94bb91"));
        reqMap.put("name","kevin3333333");
        reqMap.put("age",18);
        reqMap.put("sex","male");
        List<String> list  = new ArrayList<>();
        list.add("55");
        list.add("77");
        reqMap.put("hobby", list);
        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        list2.add(1);
        list2.add(3);
        reqMap.put("numbers",list2);

        Set<String> tags = new HashSet<>();
        tags.add("12");
        tags.add("12");
        tags.add("13");
        tags.add("16");

        Map<String,String> map = new HashMap<>();
        map.put("id","123");
        map.put("name","kevin");
        map.put("hobby","music");

        reqMap.put("map",map);


        reqMap.put("tags",tags);
        try {
            String sql = AnalyzeXml.loadXmlFile("person.xml").getCqlFromXml("updatePerson",reqMap);

            System.out.println(session.execute(sql).isExhausted());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Test
    public void addTest(){

        Map<String,Object> reqMap = new HashMap<>();
        reqMap.put("id", UUIDs.random());
        reqMap.put("date",new Date());
        reqMap.put("float",12.34f);
        List<Integer> list = new ArrayList<>();
        list.add(2);
        list.add(3);
        list.add(5);
        reqMap.put("list_int",list);

        Map<String,Integer> map_int = new HashMap<>();
        map_int.put("a",1);
        map_int.put("b",2);
        reqMap.put("map_int",map_int);

        reqMap.put("name","name2");

        Set<Integer> set = new HashSet<>();
        set.add(11);
        set.add(22);
        set.add(33);
        reqMap.put("set_int",set);

        reqMap.put("timestamp",System.currentTimeMillis());

        try {
            String sql = AnalyzeXml.loadXmlFile("test.xml").getCqlFromXml("addTest",reqMap);

            System.out.println(session.execute(sql).isExhausted());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @AfterClass
    public static void closeSession(){
        if(session != null)
        session.close();
    }


    @Test
    public void main(){

        Set<String> stringSet = new HashSet<>();
        stringSet.add("aa");
        stringSet.add("bb");

        Map<String,String> map = new HashMap<>();
        map.put("a","aa");
        map.put("b","bb");
        Integer i = 122222;
        String a = "aa";
        Object b = "ccc";
        Map<String,String> map2 = new HashMap<>();
        System.out.println("map == "+ (map2 instanceof Map));
        Long c = 123456789L;
        float d = 12.34f;
        double e = 22.45;
        boolean f = true;
        Date g = new Date();


        System.out.println(String.valueOf(i));
        System.out.println(a.toString());
        System.out.println(b.toString());
        System.out.println(c.toString());
        System.out.println(String.valueOf(d));
        System.out.println(String.valueOf(e));
        System.out.println(String.valueOf(f));
        System.out.println(String.valueOf(g));

        //System.out.println(CassandraUtils.convertMapToString(map));
    }

    @Test
    public void testMultiThread(){
        for(int i=0;i<100;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread()+ "thread==="+CassandraClientManager.newInstance().getSession());
                }
            }).start();
        }
    }




}
