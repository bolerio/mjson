package testmjson;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import mjson.Json;

public class TestParser
{
	@Test(expected=Json.MalformedJsonException.class)
	public void malformedTest1() { Json.read("{\"value2\":\"years\"\"value3\":\"wtf\"}"); }
	@Test(expected=Json.MalformedJsonException.class)
	public void malformedTest2() { Json.read("{\"x\":\"5\",\"y\":\"wtf\""); }
	@Test(expected=Json.MalformedJsonException.class)
	public void malformedTest3() { Json.read("{\"value2\"\"years\",\"value3\":\"wtf\"}"); }
	@Test(expected=Json.MalformedJsonException.class)
	public void malformedTest4() { Json.read("{\"a\":true, }"); }
	@Test(expected=Json.MalformedJsonException.class)
	public void malformedTest5() { Json.read("[43 45]"); }
	@Test(expected=Json.MalformedJsonException.class)
	public void malformedTest6() { Json.read("[true, false, ]"); }
	@Test(expected=Json.MalformedJsonException.class)
	public void malformedTest7() { Json.read("[true, 10, \"asdf\""); }
	@Test(expected=Json.MalformedJsonException.class)
	public void malformedTest8() { Json.read("{[}"); }
	@Test(expected=Json.MalformedJsonException.class)
	public void malformedTest9() { Json.read("{\"a\":[}"); }
	@Test(expected=Json.MalformedJsonException.class)
	public void malformedTest10() { Json.read("[{]"); }

	@Test
	public void parsePrimitives()
	{
		Assert.assertEquals(Json.nil(), Json.read("null"));
		Assert.assertEquals(Json.make(23), Json.read("23"));
		Assert.assertEquals(Json.make(0.345), Json.read("0.345"));
		Assert.assertEquals(Json.make(""), Json.read("\"\""));
		Assert.assertEquals(Json.make("hell\""), Json.read("\"hell\\\"\""));
		Assert.assertEquals(Json.make(true), Json.read("true"));
		Assert.assertEquals(Json.make(false), Json.read("false"));
	}
	
	@Test
	public void parseArrays()
	{
		Assert.assertEquals(Json.array(), Json.read("[]"));
		Assert.assertEquals(Json.array(1,2,3), Json.read("[1,2,3]"));
		Assert.assertEquals(Json.array(10.3,"blabla", true), Json.read("[10.3,  \"blabla\", true]"));
	}
	
	@Test
	public void parseObjects()
	{
		Assert.assertEquals(Json.object(), Json.read("{}"));
		Assert.assertEquals(Json.object("one", 1, "maybe", false, "nothing", null), 
							Json.read("\t{\"one\":1, \t    \"maybe\":false , \n\n\"nothing\"    :     null} "));
	}
	
	@Test
	public void parseSomeDeepStructures()
	{
		Json j1 = Json.read(TU.resource("/parseme1.json"));
		j1.is("ok", true);
		j1.at("doc").at("content").at(0).is("type", "discourseContainer");
		Assert.assertEquals(Json.array(Json.object(), Json.object("x", null), null), Json.read("[{},{\"x\":null},null]"));
	}


    @Test
	public void testMaximumObjectNesting()
    {
        this.doTestNesting(1000, "{\"x\":",  "}", "0");
    }

    @Test
	public void testMaximumArrayNesting()
    {
        this.doTestNesting(1000, "[",  "]", "0");
    }

	private void doTestNesting(int nesting, 
                               String open, 
                               String close, 
                               String content)
	{
		StringBuilder bad = new StringBuilder(nesting * (open.length() + close.length()));
		StringBuilder good = new StringBuilder(nesting * (open.length() + close.length()));
		for (int i = 0; i < nesting; ++i) {
			bad.append(open);
			good.append(open);
			if ((i & 31) == 0) {
			    bad.append("\n");
                good.append("\n");
			}
		}
        // one more for bad
        bad.append(open).append(" ");

		bad.append("\n").append(content).append("\n");
        good.append("\n").append(content).append("\n");
		for (int i = 0; i < nesting; ++i) {
		    bad.append(close);
            good.append(close);
		    if ((i & 31) == 0) {
		        bad.append("\n");
                good.append("\n");
		    }
		}        
        // one more for bad
        bad.append(close).append(" ");

        String badjson = bad.toString();
        String goodjson = good.toString();

        Json.read(goodjson); // no exception expected

        try {
            Json.read(badjson);
			Assert.fail("Should have thrown a max depth reached during parsing error.");
        }
        catch (Exception t) {
            Assert.assertTrue(t.toString().contains(
                "While parsing JSON, maximum depth of "));
        }
	}
	
	public static void main(String [] argv)
	{
		JUnitCore junit = new JUnitCore();
		Result result = null;
		result = junit.run(Request.method(TestParser.class, "malformedTest4"));
		if (result.getFailureCount() > 0)
		{
			for (Failure failure : result.getFailures())
			{
				failure.getException().printStackTrace();
			}
		}
		System.exit(0);
		
	}
}
