# Fibonacci numbers service

Simple service built using Kotlin and Spring framework.

## Installation

Install JDK and Maven; this is largely automated by using an IDE, such as IDEA.
You will also need to install [Redis](https://redis.io/), although this can
replaced with instantiating it via Docker:

```shell
docker run --name redis -d -p 6379:6379 redis:alpine
```

## Usage

Run the `Application` class (e.g., via IDEA). On success, the site should
be available on [`localhost`](http://localhost:8080/), which should display
auto-generated Swagger API docs, where you can check that the service works properly.

## License

Licensed under [the Apache 2.0 license](LICENSE).
