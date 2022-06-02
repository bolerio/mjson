package testmjson;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

import mjson.Json;

public class TestWriter 
{
  @Test
  public void testWrite() throws IOException 
  {
    String jsonString = TestSchemas.readTextResource("/suite/additionalProperties.json");
    Json json = Json.read(jsonString);
    StringWriter writer = new StringWriter();
    json.write(writer);
    assertEquals(json.toString(), writer.toString());
  }
  
}
