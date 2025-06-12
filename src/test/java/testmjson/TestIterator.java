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
		Iterator<Json> iter = b1.iterator();
		Assert.assertNotNull(iter);
		Assert.assertEquals(true, iter.hasNext());
		Json val = iter.next();
		Assert.assertEquals(true, val.asBoolean());
		Assert.assertEquals(false, iter.hasNext());
	}

	@Test
	public void testNil()
	{
		Json nil = Json.nil();
		Iterator<Json> iter = nil.iterator();
		Assert.assertNotNull(iter);
		Assert.assertEquals(true, iter.hasNext());
		Json val = iter.next();




		Assert.assertTrue(val.isNull());
		Assert.assertEquals(false, iter.hasNext());
	}

	@Test
	public void testNumber()
	{
		Json n1 = Json.make(567);
		Iterator<Json> iter = n1.iterator();
		Assert.assertNotNull(iter);
		Assert.assertEquals(true, iter.hasNext());
		Json val = iter.next();
		Assert.assertEquals(567, val.asInteger());
		Assert.assertEquals(false, iter.hasNext());
	}

	@Test
	public void testString()
	{
		Json s1 = Json.make("Hello");
		Iterator<Json> iter = s1.iterator();
		Assert.assertNotNull(iter);
		Assert.assertEquals(true, iter.hasNext());
		Json val = iter.next();
		Assert.assertEquals("Hello", val.asString());
		Assert.assertEquals(false, iter.hasNext());
	}

	@Test
	public void testArray()
	{
		Json numbers = array(4,3,7);
		Iterator<Json> iter = numbers.iterator();
		Assert.assertNotNull(iter);
		Assert.assertEquals(true, iter.hasNext());
		Json val = iter.next();
		Assert.assertEquals(4, val.getValue());
		Assert.assertEquals(true, iter.hasNext());
		val = iter.next();
		Assert.assertEquals(3, val.getValue());
		Assert.assertEquals(true, iter.hasNext());
		val = iter.next();
		Assert.assertEquals(7, val.getValue());
		Assert.assertEquals(false, iter.hasNext());
	}

	/**
	 * Test object iteration.
	 * OLD API: Object iterator returned Map.Entry<String, Json> pairs (key-value tuples)
	 * NEW API: Object iterator returns only Json values (no keys)
	 * This test updated to expect new API behavior while preserving logical assertions.
	 */
	@Test
	public void testObject()
	{
		Json o1 = object("p", 1, "p2", "p2value");
		Iterator<Json> iter = o1.iterator();
		Assert.assertNotNull(iter);
		Assert.assertEquals(true, iter.hasNext());
		Json val1 = iter.next();
		Assert.assertEquals(true, iter.hasNext());
		Json val2 = iter.next();
		Assert.assertEquals(false, iter.hasNext());
		// Object iterator now returns only values, order not guaranteed
		Assert.assertTrue((val1.asInteger() == 1 && val2.asString().equals("p2value")) ||
		                  (val1.asString().equals("p2value") && val2.asInteger() == 1));
	}
}
