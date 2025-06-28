# mjson - Minimalistic JSON Library

<p align="left">
    <a href="https://mvnrepository.com/artifact/org.sharegov/mjson" alt="Maven Artifact">
        <img src="https://img.shields.io/maven-central/v/org.sharegov/mjson" />
    </a>
</p>  

### News

* October 5, 2024 - 1.4.2 released after a long hiatus. Fixes memory leak problem, a security vulnerability and doc lint warnings during build.
  
### What Is It?
mJson is an extremely lightweight Java JSON library with a very concise API. The source code is a single Java file. The license is Apache 2.0. Because of its tiny size, it's well-suited for any application aiming at a small footprint such as mobile/Android applications.

It was originally developed in the context of the [OpenCiRM](https://github.com/sharegov/opencirm) project. There is a graph database based persistent layer for mJson implemented at the [HyperGraphDB Project](http://hypergraphdb.org). This means you can transparently persist and query JSON documents like in document-oriented databases (MongoDB, CouchDB), but you don't have split documents into separate collection or create special purposes indices since all documents and properties are automatically interlinked.

### Features

* Full support for [JSON Schema Draft 4](http://json-schema.org/) validation
* Single universal type - everything is a `Json`, no type casting
* Single factory method, no new operators, just call `Json.make(anything here)`
* Fast, hand-coded parsing
* Designed as a general purpose data structure for use in Java
* Parent pointers and `up` method to traverse the JSON structure
* Concise methods to read (`Json.at`), modify (`Json.set`, `Json.add`), duplicate (`Json.dup`), merge (`Json.with`) 
* Methods for type-check (e.g. `Json.isString()`) and access to underlying Java value (e.g. `Json.asString()`)
* Method chaining
* Pluggable factory to build your own support for arbitrary Java<->Json mapping
* 1 Java file is the whole library with no external dependencies

### API Tour

Go see a **[Complete Tour of the API](https://github.com/bolerio/mjson/wiki/A-Tour-of-the-API)**

Read my tutorial blog on **[JSON Schema](http://www.kobrix.blogspot.com/2014/09/jayson-skima-validating-javascript.html)**

### Wish List

(get in touch if you want to help!)

1. Traversal API, with pattern-matching
2. Extend JSON Schema support for template generation

**[Goto mJson Official Website](http://bolerio.github.io/mjson/)**

