[![Build Status](https://app.travis-ci.com/Phil-Ba/rql-2-queryDsl.svg?branch=master&kill_cache=1)](https://app.travis-ci.com/github/Phil-Ba/rql-2-queryDsl)
[![Coverage Status](https://coveralls.io/repos/github/Phil-Ba/rql-2-queryDsl/badge.svg?branch=master&kill_cache=1)](https://coveralls.io/github/Phil-Ba/rql-2-queryDsl?branch=master)

# About

This library aims to provide a queryDsl(JPA) based implementation for the RQL language. It is based on the `net.jazdw:rql-parser`. This
allows to provide dynamic querying of entity properties without having to implement any queries. This is currently a POC.

The `RqlExcutor` is the main entry class for usage. It is thread safe and an instance can be reused, as long as the underlying
`EntityManager` is also thread safe.

# Example usage

``` Kotlin 
    val executor = RqlExecutor(entityManager)
    val result = executor.queryByRql(
                "and(name=John,lt(age,20))&gt(age,10)",
                Person::class.java
            )
```

# Links

[RQL Language](https://github.com/persvr/rql)