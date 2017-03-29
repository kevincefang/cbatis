package cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.HostDistance;
import com.datastax.driver.core.PoolingOptions;
import com.datastax.driver.core.Session;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * @author: kevin.
 * @date: 2017/3/29 0029.
 * @description: cassandra session manager
 */
public class CassandraClientManager {

    private static HostDistance hostDistance = HostDistance.LOCAL; //调用远程（REMOTE）/本地(LOCAL)

    private final static int newMaxRequests = Integer.parseInt(getProperty("newMaxRequests"));
    private final static int newCoreConnections = Integer.parseInt(getProperty("newCoreConnections"));
    private final static int newMaxConnections = Integer.parseInt(getProperty("newMaxConnections"));
    private final static String host = getProperty("host");
    private final static int port = Integer.parseInt(getProperty("port"));

    private final static String username = getProperty("username");
    private final static String password = getProperty("password");


    private static volatile Cluster cluster = null;
    private static PoolingOptions poolingOptions = null;

    private Session session = null;


    private CassandraClientManager(){
        if(cluster == null)
        cluster = Cluster.builder()
                .addContactPoints(host)
                .withPort(port)
                .withCredentials(username,password)
                .withPoolingOptions(poolingOptions).build();
    }


    static{
        loadProps();
        initPool();
    }

    /**
     * 初始化casandra的线程池
     */
    private synchronized static void initPool(){
        poolingOptions = new PoolingOptions();

        // 每个连接的最大请求数
        poolingOptions.setMaxRequestsPerConnection(hostDistance,newMaxRequests);

        // 表示和集群里的机器至少有newCoreConnections个连接 最多有newMaxConnections个连接
        poolingOptions.setCoreConnectionsPerHost(hostDistance,newCoreConnections).setMaxConnectionsPerHost(hostDistance,newMaxConnections);

    }


    /**
     * 提供给外部调用new 一个实例对象
     * @return
     */
    public static CassandraClientManager newInstance(){

        return new CassandraClientManager();
    }



    /**
     * 获取session
     * @return
     */
    public Session getSession(){
        //建立连接
        if(session == null){
            if(cluster == null){
                new CassandraClientManager();
            }
            return session = cluster.connect();
        }else{
            return session;
        }
    }

    private static Properties props;

    public static String getProperty(String key){
        if(null == props) {
            loadProps();
        }
        return props.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        if(null == props) {
            loadProps();
        }
        return props.getProperty(key, defaultValue);
    }

     private synchronized static void loadProps(){
        props = new Properties();
        InputStream in = null;
        try {
            in = Thread.currentThread().getClass().getResourceAsStream("/cassandra/cassandra.properties");
            props.load(in);
        } catch (FileNotFoundException e) {
            System.out.println("cassandra.properties文件未找到");
        } catch (IOException e) {
            System.out.println("出现IOException");
        } finally {
            try {
                if(null != in) {
                    in.close();
                }
            } catch (IOException e) {
                System.out.println("cassandra.properties文件流关闭出现异常");
            }
        }
    }


}
