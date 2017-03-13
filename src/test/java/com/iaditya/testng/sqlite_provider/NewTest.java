package com.iaditya.testng.sqlite_provider;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Sample test class to demonstrate use of sqlite as the data source of the test data.
 * 
 * @author adityai
 *
 */
public class NewTest {

	/**
	 * Sample test case that pulls test data from sqlite database
	 * 
	 * @param a
	 * @param b
	 */
  @Test(dataProvider="sqliteDataProvider")
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
  @Test(dataProvider="sqliteDataProvider")
  public void testCase002(String a, String b) {
	  Assert.assertEquals(a, "testData_002_1");
	  Assert.assertEquals(b, "testData_002_2");
  }

  @DataProvider(name="sqliteDataProvider")
  private Object[][] getData(Method method) {
	    Connection c = null;
	    Statement stmt = null;
	    String testCaseName = null;
	    String data1 = null;
	    String data2 = null;
	    Object[][] data = null;
	    
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:src/test/resources/test.db");
	      c.setAutoCommit(false);
	      System.out.println("Opened database successfully");

	      stmt = c.createStatement();
	      ResultSet rs = stmt.executeQuery( "SELECT * FROM main.testcase where TestCaseName = '" + method.getName() + "'" + ";" );
	      rs.next(); 
	         testCaseName = rs.getString("TestCaseName");
	         data1 = rs.getString("data1");
	         data2 = rs.getString("data2");
	         data = new Object[][] {{data1, data2}};
	         System.out.println(data1 + " " + data2);
	         
	      rs.close();
	      stmt.close();
	      c.close();
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Operation done successfully");
	 	  
	  return data;
  }
}
