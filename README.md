一款基于NOSQL数据库Cassandra，为实现CQL与代码分离的小框架

（1）首先定义XML文本内容：

<?xml version="1.0" encoding="gbk"?>

<sqls>

    <sql id="selectAll">

        select * from test_keyspace.person

    </sql>

    <sql id="addPerson">

      insert into test_keyspace.person(id,name,age,tags,hobby,sex,numbers,map) values(#{id},#{name},#{age},#{tags},#{hobby},#{sex},#{numbers},#{map})

    </sql>

    <sql id="updatePerson">
        update test_keyspace.person set name= #{name} , age = #{age} , tags = tags + #{tags} , hobby =  hobby + #{hobby} , sex = #{sex},
        numbers = numbers + #{numbers} , map = map + #{map}
        where id= #{id}
    </sql>


</sqls>

（2）获取Session
 Session session = CassandraClientManager.newInstance().getSession();
 
 (3)执行CQL语句
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
        
   （4）关闭session 连接
    if(session != null)
        session.close();
    
