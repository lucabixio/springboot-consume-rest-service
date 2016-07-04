# Consuming a RESTful Web Service

This guide walks you through the process of creating an application that consumes a RESTful web service.
You’ll build an application that uses Spring’s RestTemplate to retrieve a random Spring Boot quotation at http://gturnquist-quoters.cfapps.io/api/random

## Fetch a REST resource
With project setup complete, you can create a simple application that consumes a RESTful service.

A RESTful service has been stood up at http://gturnquist-quoters.cfapps.io/api/random. It randomly fetches quotes about Spring Boot and returns them as a JSON document.

If you request that URL through your web browser or curl, you’ll receive a JSON document that looks something like this:
```json
{
   type: "success",
   value: {
      id: 10,
      quote: "Really loving Spring Boot, makes stand alone Spring apps easy."
   }
}
```
Easy enough, but not terribly useful when fetched through a browser or through curl.

A more useful way to consume a REST web service is programmatically. To help you with that task, Spring provides a convenient template class called RestTemplate. RestTemplate makes interacting with most RESTful services a one-line incantation. And it can even bind that data to custom domain types.

First, create a domain class to contain the data that you need. If all you need to know are Pivotal’s name, phone number, website URL, and what the pivotalsoftware page is about, then the following domain class should do fine:
```java
package hello;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Quote {

    private String type;
    private Value value;

    public Quote() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "type='" + type + '\'' +
                ", value=" + value +
                '}';
    }
}
```

As you can see, this is a simple Java class with a handful of properties and matching getter methods. It’s annotated with @JsonIgnoreProperties from the Jackson JSON processing library to indicate that any properties not bound in this type should be ignored.

An additional class is needed to embed the inner quotation itself.

```java
package hello;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Value {

    private Long id;
    private String quote;

    public Value() {
    }

    public Long getId() {
        return this.id;
    }

    public String getQuote() {
        return this.quote;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    @Override
    public String toString() {
        return "Value{" +
                "id=" + id +
                ", quote='" + quote + '\'' +
                '}';
    }
}
```
This uses the same annotations but simply maps onto other data fields.

## Make the application executable

Although it is possible to package this service as a traditional WAR file for deployment to an external application server, the simpler approach demonstrated below creates a standalone application. You package everything in a single, executable JAR file, driven by a good old Java main() method. Along the way, you use Spring’s support for embedding the Tomcat servlet container as the HTTP runtime, instead of deploying to an external instance.

Now you can write the Application class that uses RestTemplate to fetch the data from our Spring Boot quotation service.

```java
package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String args[]) {
        SpringApplication.run(Application.class);
    }

    @Override
    public void run(String... args) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        Quote quote = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
        log.info(quote.toString());
    }
}
```

Because the Jackson JSON processing library is in the classpath, RestTemplate will use it (via a message converter) to convert the incoming JSON data into a Quote object. From there, the contents of the Quote object will be logged to the console.

Here you’ve only used RestTemplate to make an HTTP GET request. But RestTemplate also supports other HTTP verbs such as POST, PUT, and DELETE.