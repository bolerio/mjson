package testmjson;

import mjson.Json;
import static mjson.Json.*;

import org.junit.Assert;
import org.junit.Test;

public class TestEnclosing
{
	@Test
	public void testParentObject()
	{
		Json obj  = object();
		Json s = make("hi");
		obj.set("greet", s);
		Assert.assertTrue(obj == s.up());
		
		Json nested = object();
		obj.set("nested", nested);
		Assert.assertTrue(obj == nested.up());
		
		nested.set("parent", obj);
		Assert.assertTrue(nested == obj.up());
		
		Json nested2 = object();
		obj.set("nested2", nested2);
		nested2.set("parent", obj);
		Assert.assertTrue(obj == nested2.up());
		Assert.assertTrue(obj.up().asJsonList().contains(nested2));
	}
	
	@Test
	public void testParentArray()
	{
		Json arr = array();
		Json i = make(10);
		arr.add(i);
		Assert.assertTrue(i.up() == arr);
		System.out.println(i.up());
		Json arr2 = array();
		arr2.add(i);
		
		System.out.println(i.up());
		Assert.assertTrue(i.up().asJsonList().contains(arr));
		Assert.assertTrue(i.up().asJsonList().contains(arr2));
	}

	@Test
	public void testToStringOfCircularObject()
	{
		Json x = Json.object("name", "x", "tuple", Json.array());
		Json y = Json.object("backref", x);
		x.at("tuple").add(y);
		String asstring = x.toString();
		Assert.assertTrue(asstring.contains("tuple"));
	}
	
	/**
	 * When we duplicate a deeply nested JSON structure, the parent chains must
	 * be properly replicated.
	 */
	@Test
	public void testDuplicateSimpleTree()
	{

	}
	
	@Test
	public void testDuplicateGraphWithCycles()
	{
		
	}
}