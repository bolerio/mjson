package testmjson;

import static mjson.Json.array;
import static mjson.Json.object;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import mjson.Json;

/**
 * Json implementations other than mjson's own classes, mixed with mjson's classes: equality,
 * hashing, text output and with(). Applications (for example a storage layer) may subclass Json;
 * mjson must treat such elements like its own.
 */
public class TestForeignImplementations
{
	// Minimal foreign implementations, one per kind of JSON element. They follow the equality and
	// hashing rules of mjson's own classes: objects hash as their map, arrays as their list,
	// strings as the string, numbers as the double value, booleans as 1/0 and null as 0.

	static class ForeignObject extends Json
	{
		final Map<String, Json> map = new LinkedHashMap<String, Json>();
		boolean mergedInto = false;

		ForeignObject put(String name, Json value) { map.put(name, value); return this; }

		public boolean isObject() { return true; }
		public Json at(String property) { return map.get(property); }
		public boolean has(String property) { return map.containsKey(property); }
		public Map<String, Json> asJsonMap() { return map; }
		public Map<String, Object> asMap()
		{
			Map<String, Object> m = new HashMap<String, Object>();
			for (Map.Entry<String, Json> e : map.entrySet())
				m.put(e.getKey(), e.getValue().getValue());
			return m;
		}
		public Object getValue() { return asMap(); }
		public Json set(String property, Json value) { map.put(property, value); return this; }
		public Json dup()
		{
			ForeignObject copy = new ForeignObject();
			for (Map.Entry<String, Json> e : map.entrySet())
				copy.put(e.getKey(), e.getValue().dup());
			return copy;
		}
		public String toString() { return toString(Integer.MAX_VALUE); }
		public String toString(int maxCharacters)
		{
			StringBuilder sb = new StringBuilder("{");
			for (Map.Entry<String, Json> e : map.entrySet())
			{
				if (sb.length() > 1)
					sb.append(",");
				sb.append(Json.make(e.getKey()).toString()).append(":").append(e.getValue().toString(maxCharacters));
			}
			return sb.append("}").toString();
		}
		public int hashCode() { return map.hashCode(); }
		public boolean equals(Object x)
		{
			return x instanceof Json && ((Json)x).isObject() && map.equals(((Json)x).asJsonMap());
		}
	}

	/** A foreign object that supports being merged into by with(..., "merge"). */
	static class MergeableForeignObject extends ForeignObject
	{
		protected Json withOptions(Json other, Json allOptions, String path)
		{
			mergedInto = true;
			for (Map.Entry<String, Json> e : other.asJsonMap().entrySet())
				if (!map.containsKey(e.getKey()))
					map.put(e.getKey(), e.getValue());
			return this;
		}
	}

	static class ForeignArray extends Json
	{
		final List<Json> list = new ArrayList<Json>();

		ForeignArray(Json... elements) { Collections.addAll(list, elements); }

		public boolean isArray() { return true; }
		public Json at(int index) { return list.get(index); }
		public List<Json> asJsonList() { return list; }
		public List<Object> asList()
		{
			List<Object> L = new ArrayList<Object>();
			for (Json x : list)
				L.add(x.getValue());
			return L;
		}
		public Object getValue() { return asList(); }
		public Json add(Json el) { list.add(el); return this; }
		public String toString() { return toString(Integer.MAX_VALUE); }
		public String toString(int maxCharacters)
		{
			StringBuilder sb = new StringBuilder("[");
			for (Json x : list)
			{
				if (sb.length() > 1)
					sb.append(",");
				sb.append(x.toString(maxCharacters));
			}
			return sb.append("]").toString();
		}
		public int hashCode() { return list.hashCode(); }
		public boolean equals(Object x)
		{
			return x instanceof Json && ((Json)x).isArray() && list.equals(((Json)x).asJsonList());
		}
	}

	static class ForeignString extends Json
	{
		final String val;
		ForeignString(String val) { this.val = val; }
		public boolean isString() { return true; }
		public String asString() { return val; }
		public Object getValue() { return val; }
		public String toString() { return Json.make(val).toString(); }
		public String toString(int maxCharacters) { return toString(); }
		public int hashCode() { return val.hashCode(); }
		public boolean equals(Object x)
		{
			return x instanceof Json && ((Json)x).isString() && val.equals(((Json)x).asString());
		}
	}

	static class ForeignNumber extends Json
	{
		final double val;
		ForeignNumber(double val) { this.val = val; }
		public boolean isNumber() { return true; }
		public double asDouble() { return val; }
		public int asInteger() { return (int)val; }
		public Object getValue() { return val; }
		public String toString() { return Double.toString(val); }
		public String toString(int maxCharacters) { return toString(); }
		public int hashCode() { return Double.hashCode(val); }
		public boolean equals(Object x)
		{
			return x instanceof Json && ((Json)x).isNumber() && val == ((Json)x).asDouble();
		}
	}

	static class ForeignBoolean extends Json
	{
		final boolean val;
		ForeignBoolean(boolean val) { this.val = val; }
		public boolean isBoolean() { return true; }
		public boolean asBoolean() { return val; }
		public Object getValue() { return val; }
		public String toString() { return val ? "true" : "false"; }
		public String toString(int maxCharacters) { return toString(); }
		public int hashCode() { return val ? 1 : 0; }
		public boolean equals(Object x)
		{
			return x instanceof Json && ((Json)x).isBoolean() && val == ((Json)x).asBoolean();
		}
	}

	static class ForeignNull extends Json
	{
		public boolean isNull() { return true; }
		public Object getValue() { return null; }
		public String toString() { return "null"; }
		public String toString(int maxCharacters) { return toString(); }
		public int hashCode() { return 0; }
		public boolean equals(Object x) { return x instanceof Json && ((Json)x).isNull(); }
	}

	static void assertEqualBothWays(Json a, Json b)
	{
		Assert.assertTrue(a + " equals " + b, a.equals(b));
		Assert.assertTrue(b + " equals " + a, b.equals(a));
		Assert.assertEquals("hashCode of " + a + " and " + b, a.hashCode(), b.hashCode());
	}

	static void assertNotEqualBothWays(Json a, Json b)
	{
		Assert.assertFalse(a + " equals " + b, a.equals(b));
		Assert.assertFalse(b + " equals " + a, b.equals(a));
	}

	// ------------------------------------------------------------------
	// Equality and hashing
	// ------------------------------------------------------------------

	@Test
	public void primitivesEqualForeignPrimitives()
	{
		assertEqualBothWays(Json.make("abc"), new ForeignString("abc"));
		assertEqualBothWays(Json.make(2.5), new ForeignNumber(2.5));
		assertEqualBothWays(Json.make(42), new ForeignNumber(42));
		assertEqualBothWays(Json.make(true), new ForeignBoolean(true));
		assertEqualBothWays(Json.make(false), new ForeignBoolean(false));
		assertEqualBothWays(Json.nil(), new ForeignNull());
	}

	@Test
	public void primitivesOfDifferentKindOrValueDiffer()
	{
		assertNotEqualBothWays(Json.make("abc"), new ForeignString("abd"));
		assertNotEqualBothWays(Json.make("1"), new ForeignNumber(1));
		assertNotEqualBothWays(Json.make(1), new ForeignNumber(2));
		assertNotEqualBothWays(Json.make(true), new ForeignBoolean(false));
		assertNotEqualBothWays(Json.make(false), new ForeignNull());
	}

	@Test
	public void objectEqualsForeignObject()
	{
		Json plain = Json.read("{\"a\":\"x\",\"b\":2}");
		Json foreign = new ForeignObject().put("a", Json.make("x")).put("b", Json.make(2));
		assertEqualBothWays(plain, foreign);
		assertNotEqualBothWays(plain, new ForeignObject().put("a", Json.make("x")));
		assertNotEqualBothWays(plain, new ForeignArray(Json.make("x"), Json.make(2)));
	}

	@Test
	public void arrayEqualsForeignArray()
	{
		Json plain = Json.read("[\"x\",2,true]");
		Json foreign = new ForeignArray(Json.make("x"), Json.make(2), Json.make(true));
		assertEqualBothWays(plain, foreign);
		assertNotEqualBothWays(plain, new ForeignArray(Json.make(2), Json.make("x"), Json.make(true)));
		assertNotEqualBothWays(plain, new ForeignObject());
	}

	@Test
	public void nestedForeignElementsCompareByContent()
	{
		Json expected = Json.read("{\"name\":\"n\",\"address\":{\"street\":\"s\",\"zip\":[1,2]},\"tags\":[\"t\",{\"k\":\"v\"}]}");
		Json mixed = object("name", "n",
		                    "address", new ForeignObject().put("street", new ForeignString("s"))
		                                                  .put("zip", new ForeignArray(Json.make(1), new ForeignNumber(2))),
		                    "tags", new ForeignArray(Json.make("t"), new ForeignObject().put("k", Json.make("v"))));
		assertEqualBothWays(expected, mixed);
		assertEqualBothWays(mixed.dup(), expected);
		Json differentDeep = object("name", "n",
		                            "address", new ForeignObject().put("street", new ForeignString("s"))
		                                                          .put("zip", new ForeignArray(Json.make(1), new ForeignNumber(3))),
		                            "tags", Json.read("[\"t\",{\"k\":\"v\"}]"));
		assertNotEqualBothWays(expected, differentDeep);
	}

	@Test
	public void foreignElementsAsHashKeys()
	{
		Set<Json> set = new HashSet<Json>();
		set.add(Json.read("{\"a\":[1,\"x\"]}"));
		set.add(Json.make("s"));
		set.add(Json.make(7));
		Assert.assertTrue(set.contains(new ForeignObject().put("a", new ForeignArray(new ForeignNumber(1), new ForeignString("x")))));
		Assert.assertTrue(set.contains(new ForeignString("s")));
		Assert.assertTrue(set.contains(new ForeignNumber(7)));
	}

	@Test
	public void isComparesForeignValues()
	{
		Json o = object("a", new ForeignString("x"), "b", new ForeignNumber(3));
		Assert.assertTrue(o.is("a", "x"));
		Assert.assertTrue(o.is("b", 3));
		Json a = array(new ForeignString("x"));
		Assert.assertTrue(a.is(0, "x"));
	}

	/** Pre-existing inconsistency: numbers compare as doubles, so they must hash as doubles too. */
	@Test
	public void numbersOfDifferentJavaTypesHashAlike()
	{
		assertEqualBothWays(Json.make(50), Json.make(50.0));
		assertEqualBothWays(Json.make(50L), Json.make(50.0f));
		Map<Json, String> m = new HashMap<Json, String>();
		m.put(Json.make(50), "fifty");
		Assert.assertEquals("fifty", m.get(Json.make(50.0)));
	}

	// ------------------------------------------------------------------
	// Text output
	// ------------------------------------------------------------------

	@Test
	public void toStringWithForeignChildren()
	{
		Json o = object("o", new ForeignObject().put("k", Json.make("v")),
		                "a", new ForeignArray(Json.make(1), new ForeignString("s")),
		                "s", new ForeignString("str"),
		                "b", new ForeignBoolean(true),
		                "n", new ForeignNull());
		Assert.assertEquals(Json.read("{\"o\":{\"k\":\"v\"},\"a\":[1,\"s\"],\"s\":\"str\",\"b\":true,\"n\":null}"),
		                    Json.read(o.toString()));
		Json a = array(new ForeignObject().put("k", Json.make("v")), new ForeignArray(new ForeignNumber(2.5)), new ForeignString("x"));
		Assert.assertEquals(Json.read("[{\"k\":\"v\"},[2.5],\"x\"]"), Json.read(a.toString()));
	}

	@Test
	public void toStringOfPlainInsideForeignInsidePlain()
	{
		Json o = object("outer", new ForeignObject().put("inner", object("x", array(1, 2))));
		Assert.assertEquals(Json.read("{\"outer\":{\"inner\":{\"x\":[1,2]}}}"), Json.read(o.toString()));
	}

	@Test
	public void toStringWithMaxCharactersAndForeignChildren()
	{
		Json o = object("a", new ForeignObject().put("k", Json.make("a fairly long string value")),
		                "b", new ForeignArray(new ForeignString("another fairly long string")));
		String s = o.toString(20);
		Assert.assertTrue("truncated output: " + s, s.length() < o.toString().length());
		Json a = array(new ForeignObject().put("k", Json.make("a fairly long string value")), new ForeignString("x"));
		String t = a.toString(10);
		Assert.assertTrue("truncated output: " + t, t.length() < a.toString().length());
	}

	// ------------------------------------------------------------------
	// with()
	// ------------------------------------------------------------------

	@Test
	public void arrayWithForeignArrayAppendsElements()
	{
		Json a = array(1);
		a.with(new ForeignArray(Json.make(2), new ForeignString("x")));
		Assert.assertEquals(Json.read("[1,2,\"x\"]"), a);
	}

	@Test
	public void objectWithForeignObject()
	{
		Json o = object("a", 1);
		o.with(new ForeignObject().put("b", Json.make(2)));
		Assert.assertEquals(Json.read("{\"a\":1,\"b\":2}"), o);
	}

	@Test
	public void mergeIntoForeignObjectThatSupportsIt()
	{
		MergeableForeignObject nested = new MergeableForeignObject();
		nested.put("x", Json.make(1));
		Json o = object("nested", nested);
		o.with(Json.read("{\"nested\":{\"y\":2}}"), "merge");
		Assert.assertTrue("merge must be delegated to the foreign element", nested.mergedInto);
		Assert.assertSame(nested, o.at("nested"));
		Assert.assertEquals(Json.read("{\"x\":1,\"y\":2}"), nested);
	}

	@Test
	public void mergeIntoForeignObjectThatDoesNotSupportItFails()
	{
		ForeignObject nested = new ForeignObject().put("x", Json.make(1));
		Json o = object("nested", nested);
		try
		{
			o.with(Json.read("{\"nested\":{\"y\":2}}"), "merge");
			Assert.fail("merging into an element that doesn't support it must not silently replace it");
		}
		catch (UnsupportedOperationException expected)
		{
		}
		Assert.assertSame(nested, o.at("nested"));
	}

	// ------------------------------------------------------------------
	// Parent tracking (pre-existing bugs found on the way)
	// ------------------------------------------------------------------

	@Test
	public void deletingMissingPropertyDoesNotThrow()
	{
		Json o = object("a", 1);
		Assert.assertNull(o.atDel("missing"));
		Assert.assertSame(o, o.delAt("missing"));
	}

	@Test
	public void deletingChildWhoseOnlyParentIsAnotherArray()
	{
		Json child = Json.make("c");
		Json otherParent = array(child, Json.make("d"));
		Json o = object();
		o.asJsonMap().put("k", child); // in o's map, but its recorded parent is otherParent
		o.delAt("k");
		Assert.assertFalse(o.has("k"));
		Assert.assertEquals("the other array must be untouched", Json.read("[\"c\",\"d\"]"), otherParent);
	}
}
