mJson is an extremely lightweight Java JSON library with a very concise API. The source code is a single Java file. The license is Apache 2.0. It was originally developed in the context of the [OpenCiRM](https://github.com/sharegov/opencirm) project. There is a graph database based persistent layer for mJson implemented at the [HyperGraphDB Project](http://hypergraphdb.org/learn?page=Json&project=hypergraphdb).

### Features

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

### Wish List

(get in touch if you want to help!)

1. Traversal API, with pattern-matching
2. JSON Schema supports for validation and template generation

**[Goto mJson Official Website](http://bolerio.github.io/mjson/)**

