package testmjson;

import junit.framework.Assert;

import org.testng.annotations.Test;

import mjson.Json;

/**
 * Run a test from the https://github.com/json-schema/JSON-Schema-Test-Suite
 * test spec.
 * 
 * @author Borislav Iordanov
 *
 */
public class SuiteTestJson
{
	private String group;
	private String description;
	private Json.Schema schema;
	private Json data;
	private boolean valid;
	
	public SuiteTestJson(String group, 
						 String description, 
						 Json.Schema schema, 
						 Json data, 
						 boolean valid)
	{
		this.group = group;
		this.description = description;
		this.schema = schema;
		this.data = data;
		this.valid = valid;
	}
	
	@Test
	public void doTest()
	{
		Assert.assertEquals("Running test " + description + " from " + group,
						valid, schema.validate(data).is("ok", true));
	}	
}