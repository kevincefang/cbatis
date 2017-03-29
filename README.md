一款基于NOSQL数据库Cassandra，为实现CQL与代码分离的mini框架.如有问题还请各位大神赐教，O(∩_∩)O

（1）首先定义test.XML文本内容：

    <?xml version="1.0" encoding="gbk"?>

    <sqls>

        <sql id="selectAll">

            select * from test_keyspace.test

        </sql>

        <sql id="addTest">

          insert into test_keyspace.test (id,date,float,list_int,map_int,name,set_int,timestamp)
            values (#{id},#{date},#{float},#{list_int},#{map_int},#{name},#{set_int},#{timestamp})

        </sql>


    </sqls>

（2）获取Session

    Session session = CassandraClientManager.newInstance().getSession();
 
 （3）执行CQL语句
        请求参数放在Map中，key的名称与#{name}相对应.

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
        
   （4）最后记得关闭session连接(⊙o⊙)哦
 
        if(session != null)
            session.close();

