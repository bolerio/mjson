package testmjson;

import mjson.Json;
import static mjson.Json.*;
import org.junit.Test;
import org.junit.Assert;

public class TestBasics
{
	@Test
	public void testBoolean()
	{
		Json b1 = Json.make(true);
		Json b1dup = b1.dup();
		Assert.assertFalse(b1 == b1dup);
		Assert.assertEquals(b1, b1dup);
		Json b2 = Json.factory().bool(Boolean.TRUE);
		Assert.assertEquals(b1, b2);
		Assert.assertEquals(Boolean.FALSE, Json.make(false).getValue());
	}
	
	@Test
	public void testNil()
	{
		Json nil = Json.nil();
		Assert.assertEquals(nil, Json.make(null));
		Assert.assertNull(nil.getValue());
		Json nil2 = nil.dup();
		Assert.assertFalse(nil == nil2);
		Assert.assertEquals(nil, nil2);
	}
	
	@Test
	public void testNumber()
	{
		Json n1 = Json.make(567);
		Json n2 = Json.make(567.0);
		Json n1dup = n1.dup();
		Assert.assertFalse(n1 == n1dup);
		Assert.assertEquals(n1, n1dup);
		Assert.assertEquals(567, n1.asInteger());
		Assert.assertEquals(567, n2.asInteger());
		Assert.assertEquals(567l, n2.asLong());
		Assert.assertEquals(567.0, n2.asFloat(), 0.0);
		Assert.assertEquals(567.0, n1.asDouble(), 0.0);
		Assert.assertEquals(n1, n2);
		Assert.assertEquals(Double.MAX_VALUE, Json.factory().number(Double.MAX_VALUE).getValue());
		Assert.assertEquals(Integer.MIN_VALUE, Json.factory().number(Integer.MIN_VALUE).getValue());
	}
	
	@Test
	public void testString()
	{
		Json s1 = Json.make("");
		Assert.assertEquals("", s1.getValue());
		Json s1dup = s1.dup();
		Assert.assertFalse(s1dup == s1);
		Assert.assertEquals(s1, s1dup);
		Json s2 = Json.make("string1");
		Json s2again = Json.factory().string("string1");
		Assert.assertEquals(s2, s2again);
		Json s3 = Json.make("Case Sensitive");
		Json s3_lower = Json.make("Case Sensitive".toLowerCase());
		Assert.assertNotEquals(s3, s3_lower);
	}
	
	@Test
	public void testArray()
	{
		Json empty = array();
		Assert.assertTrue(empty.asJsonList().isEmpty());
		Json numbers  = array(4,3,4);
		Assert.assertEquals(numbers.at(0), numbers.at(2));
		Assert.assertSame(numbers, numbers.add(1));
		Assert.assertEquals(4, numbers.asJsonList().size());
		Assert.assertEquals(1, numbers.at(3).asInteger());
		Json mixed = array(nil(), "s", 2);
		Assert.assertNull(mixed.at(0).getValue());
		Json mixeddup = mixed.dup();
		Assert.assertEquals(mixed, mixeddup);
		Assert.assertEquals("s", mixeddup.at(1).getValue());
	}
	
	@Test
	public void testObject()
	{
		Json empty = object();
		Assert.assertTrue(empty.asJsonMap().isEmpty());
		Json o1 = object("p", 1).set("p2", "p2value");
		Assert.assertEquals(1, o1.at("p").getValue());
		Assert.assertTrue(o1.has("p2"));
		Assert.assertFalse(o1.has("p2asdfasd"));
		Json dup = o1.dup();
		Assert.assertEquals(o1,  dup);
		dup.set("A", array("23423", 24,2423, o1));
		Assert.assertNotEquals(o1, dup);
	}
}
