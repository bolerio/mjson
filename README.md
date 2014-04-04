JSON (JavaScript Object Notation) is a lightweight data-interchange format. You knew that already. If not, continue reading on http://www.json.org.

It's supposed to be about simplicity and clarity. Something minimal, intuitive, direct. Yet, I couldn't find a Java library to work with it in this way. The GSON project is pretty solid and comprehensive, but while working with REST services and coding some JavaScript with JSON in between, I got frustrated of having to be so verbose on the server-side while on the client-side manipulating those JSON structures is so easy. Yes, JSON is naturally embedded in JavaScript, so syntactically it could never be as easy in a Java context, but it just didn't make sense all that strong typing of every JSON element when the structures are dynamic and untyped to being with. It seemed like suffering the verbosity of strong typing without getting any of the benefits. Especially since we don't map JSON to Java or anything of the sort. Our use of JSON is pure and simple: structured data that both client and server can work with. 

After a lot of hesitation and looking over all Java/JSON I could find (well, mostly I examined all the libraries listed on json.org), I wrote yet another Java JSON library. Because it's rather independent from the rest of the project, I separated it. And because it has a chance of meeting other programmers' tastes, I decided to publish it. 

The library is called mjson for "minimal JSON". The source code is a single Java file (also included in the jar). Some of it was ripped off from other projects and credit and licensing notices are included in the appropriate places. The license is Apache 2.0.

The goal of this library is to offer a simple API to work with JSON structures, directly, minimizing the burdens of Java's static typing and minimizing the programmer's typing (pun intended). 

To do that, we emulate dynamic typing by unifying all the different JSON entities into a single type called Json. Different kinds of Json entities (primitives, arrays, objects, null) are implemented as sub-classes (privately nested) of Json, but they all share the exact same set of declared operations and to the outside world, there's only one type. Most mutating operations return this which allows for a method chaining. Constructing the correct concrete entities is done by factory methods, one of them called make which is a "do it all" constructor that takes any Java object and converts it into a Json. Warning: only primitives, arrays, collections and maps are supported here. As I said, we are dealing with pure JSON, we are not handling Java bean mappings and the likes. Such functionality could be added, of course, but....given enough demand.

As a result of this strategy, coding involves no type casts, much fewer intermediary variables, much simpler navigation through a JSON structure, no new operator every time you want to add an element to a structure, no dealing with a multitude of concrete types. Overall, it makes life easier in the current era of JSON-based REST services, when implemented in Java that is.

In a sense, we are flipping the argument from the blog Dynamic Languages Are Static Languages and making use of the universal type idea in a static language. Java already has a universal type called Object, but it doesn't have many useful operations. Because the number of possible JSON concrete types is small and well-defined, taking the union of all their interfaces works well here. Whenever an operation doesn't make sense, it will throw an UnsupportedOperationException. But this is fine. We are dynamic, we can guarantee we are calling the right operation for the right concrete type. Otherwise, the tests would fail!

Here's a quick example:

<pre><code>
import mjson.Json;

Json x = Json.object().set("name", "mjson")
                      .set("version", "1.0")
                      .set("cost", 0.0)
                      .set("alias", Json.array("json", "minimal json"));
x.at("name").asString(); // return mjson as a Java String
x.at("alias").at(1); // returns "minimal json" as a Json instance
x.at("alias").up().at("cost").asDouble(); // returns 0.0

String s = x.toString(); // get string representation

x.equals(Json.read(s)); // parse back and compare => true
</code></pre>

For more, read the documentation at the link above. No point in repeating it here.

This is version 1.0 and suggestions for further enhancements are welcome. Besides some simple nice-to-haves, such as pretty printing or the ability to stream to an OutputStream, Java bean mappings might turn out to be a necessity for some use cases. Also, jQuery style selectors and a richer set of manipulation operations. Closures in JDK 7 would certainly open interesting API possibilities. For now, we are keeping it simple. The main use case is if you don't have a Java object model for the structured data you want to work with, you don't want such a model, or you don't want it to be mapped exactly and faithfully as a JSON structure.
