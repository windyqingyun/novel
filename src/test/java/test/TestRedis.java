package test;

import java.util.ArrayList;
import java.util.List;

import com.jeeplus.common.response.ServerResponse;
import com.jeeplus.common.utils.JedisUtils;
import com.jeeplus.modules.bus.entity.Book;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class TestRedis {
 
    public static JedisPool JedisPoolFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(100);
        poolConfig.setMaxTotal(600);
        JedisPool jp0 = new JedisPool(poolConfig, "47.104.13.223", 6389,
                10 * 1000, "root!password", 0);
        return jp0;
    }
    
    
    public static void main(String[] agrs) {
    	JedisPool jedisPool = JedisPoolFactory();
    	Jedis jedis = jedisPool.getResource();
    	
//    	String page = "{\"status\":0,\"data\":[{\"id\":\"b1230ba03d9844899b1f04311745dab7\",\"isNewRecord\":false}]}";
//    	jedis.set("0:fine:3".getBytes(), page.getBytes());
//    	
//    	byte[] bs = jedis.get("0:fine:3".getBytes());
//    	System.out.println("1: " + bs);
//    	System.out.println("2: " + new String(bs));
////    	System.out.println("3: " + ObjectUtils.unserialize(bs));
//    	System.out.println("4: " + ObjectUtils.unserialize(bs).toString());
    	
    	Book book = new Book();
    	book.setId("1");
    	book.setName("haha");
//    	book.setPublishDate(new Date());
    	Book book2 = new Book();
    	book2.setId("2");
    	book2.setName("呵呵");
//    	book2.setPublishDate(new Date());
    	List<Book> list = new ArrayList<Book>();
    	list.add(book);
    	list.add(book2);
    	ServerResponse<List<Book>> response = ServerResponse.createBySuccess(list);
    	
//    	byte[] bytes = ObjectUtils.serialize(response);
//    	byte[] serializer = JsonUtil.serializer(response);
//    	String obj2String = JsonUtil.obj2String(response);
//    	jedis.set("0:fine:3", obj2String);
    	String setObject = JedisUtils.setObject("0:fine:3", response, 10000);
    	System.out.println(setObject);
    	
//    	String str = jedis.get("0:fine:3");
//    	ServerResponse<List<Book>> obj = (ServerResponse<List<Book>>) ObjectUtils.unserialize(string.getBytes());
//    	ServerResponse deserializer = JsonUtil.deserializer(bs, ServerResponse.class);
//    	ServerResponse<List<Book>> string2Obj = JsonUtil.string2Obj(str, new TypeReference<ServerResponse<List<Book>>>() {});
    	
    	Object object = JedisUtils.getObject("0:fine:3");
    	System.out.println(object);

    	
    	if (jedis != null) {
    		if (jedisPool != null) {
    			jedisPool.returnResource(jedis);
    			jedisPool.destroy();
    		}
		}
    }
	
}
