package com.iaditya.testng.sqlite_provider;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.embedded.RedisServer;

/**
 * Sample test class to demonstrate use of sqlite as the data source of the test data.
 * 
 * @author adityai
 *
 */
public class NewTest {

    private RedisServer redisServer = null;

	/**
	 * Sample test case that pulls test data from sqlite database
	 * 
	 * @param a
	 * @param b
	 */
  @Test(dataProvider="redisDataProvider")
  public void testCase001(String a, String b) {
	  Assert.assertEquals(a, "testData_001_1");
	  Assert.assertEquals(b, "testData_001_2");
  }

  /**
   * Sample test case that pulls test data from sqlite database
   * 
   * @param a
   * @param b
   */
  @Test(dataProvider="redisDataProvider")
  public void testCase002(String a, String b) {
	  Assert.assertEquals(a, "testData_002_1");
	  Assert.assertEquals(b, "testData_002_2");
  }
  

  /**
   * Before suite, create redis embedded instance and insert data
   */
  @BeforeSuite
  private void initRedis() {
		try {
			redisServer = new RedisServer(6379);
		    redisServer.start();
		    System.out.println(redisServer.isActive());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		 try {
			JedisPool pool = new JedisPool("localhost", 6379);
			Jedis jedis = pool.getResource();
			jedis.ping();
			Map dataHash = new HashMap<String, String>();
			dataHash.put("data1", "testData_001_1");
			dataHash.put("data2", "testData_001_2");

			jedis.hmset("testCase001", dataHash); 
		    
			dataHash = new HashMap<String, String>();
			dataHash.put("data1", "testData_002_1");
			dataHash.put("data2", "testData_002_2");
			jedis.hmset("testCase002", dataHash); 
			
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
  }

  /**
   * After test suite, stop embedded redis
   */
  @AfterSuite
  private void stopRedisServer(){
		 redisServer.stop();
  }

  @DataProvider(name="redisDataProvider")
  private Object[][] getData(Method method) {
	    Object[][] data = null;
		
		 try {
			JedisPool pool = new JedisPool("localhost", 6379);
			Jedis jedis = pool.getResource();
			
		    data = new Object[][] {{jedis.hmget(method.getName(), "data1").get(0), jedis.hmget(method.getName(), "data2").get(0)}};
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	  return data;
  }
  
}
  
