package testmjson;

import mjson.Json;
import static mjson.Json.*;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by biordanov on 10/31/2014.
 */
public class TestWithOptions
{

    @Test
    public void testObjectMerge()
    {
        Json o1 = object("id", 2, "name", "John",
                 "address", object("streetName", "Main",
                                   "streetNumber", 20,
                                   "city","Detroit"));
        Json o2 = o1.dup().set("age", 20).at("address").delAt("city").up();
        o1.with(o2, "merge");
        Assert.assertTrue(o1.is("age", 20));
        Assert.assertTrue(o1.at("address").is("city", "Detroit"));
    }

    @Test
    public void testSortedArrayMerge()
    {
        Json a1 = array(1,2,20,30,50);
        Json a2 = array(0, 2, 20, 30, 35, 40, 51);
        a1.with(a2, "sort");
        Assert.assertEquals(array(0, 1, 2, 20, 30, 35, 40, 50, 51), a1);
    }

    @Test
    public void testUnsortedArrayMerge()
    {
        Json a1 = array(4, 35, 1, 65, 2, 456);
        Json a2 = array(65, 5, 3534, 4);
        a1.with(a2, object("sort", false));
        Assert.assertEquals(TU.set(a1.asJsonList()),
                TU.set(array(4, 35, 1, 65, 2, 456, 65, 5, 3534, 4).asJsonList()));
    }

    @Test
    public void testCompareEqualsInObject()
    {
        Json x1 = object("id", 4, "name", "Tom");
        Json x2 = object("id", 4, "name", "Hanna");
        Json a1 = array(object("person", x1));
        Json a2 = array(object("person", x2));
        a1.with(a2, new Json[0]);
        Assert.assertEquals(2, a1.asJsonList().size());
        a1 = array(object("person", x1));
        a1.with(a2, object("compareBy", "id"));
        Assert.assertEquals(1, a1.asJsonList().size());
        Assert.assertEquals(make("Tom"), a1.at(0).at("person").at("name"));
    }

    @Test
    public void testCompareEqualsInArray()
    {

    }

    @Test
    public void testCompareOrderArray()
    {

    }
}