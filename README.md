# Introduction

The goal of this programming challenge is to make the acceptance tests pass. This is important, so I'll say it again.

_The goal of this programming challenge is to make the acceptance tests pass._ 

Although, tiny-sql resembles a small, in-memory relational database management system, it is a mistake to write code that would apply to an 
RDBMS that is not covered by a specific acceptance test. The primary criterion by which the challenge will be evaluated is the number of passing acceptance tests.

That said, it may help to think about the big picture and break the solution down into parts. There are really three high-level pieces:

* Parsing the SQL into something more usable for the database engine
* Creating an abstract model that represents the database engine
* Tying the executable SQL statements into the database engine for the select/insert/update logic

This problem may seem complex, but I encourage you not to overthink it. There are no joins and there are no subqueries. Select statements can have a where clause, but,
 if you examine the acceptance tests, you'll see that the only relation that's tested is equality (where label = 'foo'). There are only three types, integer, string, and NULL. Fundamentally, this problem is really about putting rows of data into a data structure and taking them out. Java already provides all the appropriate data structures -- you won't need to write any by hand, you'll just need to identify the appropriate ones and provide the interface.

This programming challenge is achievable by using classes in `java.util`, `java.lang`, and perhaps `java.text`. It doesn't require knowledge of any java 
features past 1.4. It requires you to be able to use generics in collections, but you won't have to write any parameterized types or methods.  
It doesn't require specialized knowledge of any frameworks, JEE, XML, or anything enterprisey. It's not designed to measure how well someone can design 
and implement large systems, but to provide some insight into how well someone has mastered the fundamentals of good Java/OO programming. 
I've included Spring and set up a trivial `application-context.xml` in case you want to use Spring for dependency injection, but this isn't required, either. 

The API for the tiny-sql boundary classes has been provided. They are:

* `TinySqlDb` - the interface to the DB itself
* `TinySqlTypes` - an enumeration of the types of data tiny-sql supports
* `ResultSetRow` - an ordered collection of values of TinySqlTypes
* `ResultSet` - an ordered aggregation of ResultSetRows
* `DbMetadata` - metadata for the specific tiny-sql database instance
* `TinySqlException` - the mechanism that TinySqlDb uses to communicate exceptional conditions

It's unlikely that all the tests will be passing and the code will be "production-ready" after one day, but that's also part of the
 challenge -- to prioritize what will be completed and what will be left undone. An acceptable solution is to start with the TestCreateTable suite,
  work your way through TestInsertAndSelect and move on to TestUpdate if you have time.

The code is evaluated on three axes: correctness, clarity, and conciseness. Most importantly, the code should be correct. The best way of 
guaranteeing correctness is to have a full suite of unit tests. [JUnit], [AssertJ], and [Mockito] are all included in the maven `pom.xml`. 
It's not required that mockito and AssertJ be used, but unit tests are a requirement and the tests should be clear and obviously correct.

After correctness, the code should be clear. Classes, variables, and methods should all be named appropriately. Logic should be simple and 
standard java idioms should be used as much as possible. Following good OO principles like the [Single Responsibility Principle] and the 
[Interface Segregation Principle] will help keep the code clear.

Finally, the code should be as concise as possible without sacrificing correctness or clarity. A good way of keeping the code concise is to reuse 
code in the JDK and other standard libraries as much as possible. Google [guava] and [commons-lang] are included on the maven classpath for your use.

This is meant to be an individual challenge and should be done without assistance. Good luck!

-- Alex Garrett

[JUnit]: http://junit.org
[AssertJ]: http://joel-costigliola.github.io/assertj/
[Mockito]: http://mockito.org/
[Single Responsibility Principle]: https://en.wikipedia.org/wiki/Single_responsibility_principle
[Interface Segregation Principle]: https://en.wikipedia.org/wiki/Interface_segregation_principle
[guava]: https://github.com/google/guava
[commons-lang]: https://commons.apache.org/proper/commons-lang/
