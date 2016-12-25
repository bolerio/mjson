package testmjson;

import mjson.Json;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.Map;

import static mjson.Json.*;

public class TestIterator
{
	@Test
	public void testBoolean()
	{
		Json b1 = Json.make(true);
		Iterator iter = b1.iterator();
		Assert.assertNotNull(iter);
		Assert.assertEquals(true, iter.hasNext());
		boolean val = (Boolean)iter.next();
		Assert.assertEquals(true, val);
		Assert.assertEquals(false, iter.hasNext());
	}

	@Test
	public void testNil()
	{
		Json nil = Json.nil();
		Iterator iter = nil.iterator();
		Assert.assertNotNull(iter);
		Assert.assertEquals(true, iter.hasNext());
		Object val = iter.next();
		Assert.assertNull(val);
		Assert.assertEquals(false, iter.hasNext());
	}

	@Test
	public void testNumber()
	{
		Json n1 = Json.make(567);
		Iterator iter = n1.iterator();
		Assert.assertNotNull(iter);
		Assert.assertEquals(true, iter.hasNext());
		Number val = (Number)iter.next();
		Assert.assertEquals(567, val);
		Assert.assertEquals(false, iter.hasNext());
	}

	@Test
	public void testString()
	{
		Json s1 = Json.make("Hello");
		Iterator iter = s1.iterator();
		Assert.assertNotNull(iter);
		Assert.assertEquals(true, iter.hasNext());
		String val = (String)iter.next();
		Assert.assertEquals("Hello", val);
		Assert.assertEquals(false, iter.hasNext());
	}

	@Test
	public void testArray()
	{
		Json numbers = array(4,3,7);
		Iterator iter = numbers.iterator();
		Assert.assertNotNull(iter);
		Assert.assertEquals(true, iter.hasNext());
		Json val = (Json)iter.next();
		Assert.assertEquals(4, val.getValue());
		Assert.assertEquals(true, iter.hasNext());
		val = (Json)iter.next();
		Assert.assertEquals(3, val.getValue());
		Assert.assertEquals(true, iter.hasNext());
		val = (Json)iter.next();
		Assert.assertEquals(7, val.getValue());
		Assert.assertEquals(false, iter.hasNext());
	}

	@Test
	@SuppressWarnings("unchecked")
	public void testObject()
	{
		Json o1 = object("p", 1, "p2", "p2value");
		Iterator iter = o1.iterator();
		Assert.assertNotNull(iter);
		Assert.assertEquals(true, iter.hasNext());
		Map.Entry<String, Json> val = (Map.Entry<String, Json>)iter.next();
		Assert.assertEquals("p", val.getKey());
		Assert.assertEquals(1, val.getValue().getValue());
		Assert.assertEquals(true, iter.hasNext());
		val = (Map.Entry<String, Json>)iter.next();
		Assert.assertEquals("p2", val.getKey());
		Assert.assertEquals("p2value", val.getValue().getValue());
		Assert.assertEquals(false, iter.hasNext());
	}
}
