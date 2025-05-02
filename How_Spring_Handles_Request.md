# 🌐 How Spring Handles HTTP Requests (Complete Flow)

This documentation explains how a Spring-based web application handles HTTP requests from the moment a client sends a request to the time a response is returned. 
It covers the full journey, including the role of the **Servlet container**, **DispatcherServlet**, **thread handling**, and how **request mappings** are resolved.

---

## 📌 1. Client Sends an HTTP Request

A client (browser, mobile app, Postman, etc.) sends an HTTP request like:

GET /api/users/1 HTTP/1.1
Host: example.com


This request reaches the **Servlet container** that hosts the Spring application (e.g., Tomcat, Jetty).

---

## 🧱 2. Servlet Container and Thread Allocation

### ✔️ Servlet Container (e.g., Tomcat)

- Listens for incoming HTTP requests on a specific port (default 8080).
- Maintains a **thread pool** (ExecutorService) to handle requests concurrently.

### ✔️ Default Tomcat Thread Pool

| Parameter         | Default Value |
|------------------|---------------|
| `maxThreads`     | `200`         |
| `minSpareThreads`| `10`          |
| `acceptCount`    | `100`         |

If all threads are busy and the request queue is full (`acceptCount`), **new requests are rejected**, leading to thread exhaustion.

***Thread Exhaustion***
java.util.concurrent.RejectedExecutionException: Queue capacity is full
	at org.apache.tomcat.util.threads.ThreadPoolExecutor.execute(ThreadPoolExecutor.java:1386) ~[tomcat-embed-core-10.1.40.jar:10.1.40]
	at org.apache.tomcat.util.net.AbstractEndpoint.processSocket(AbstractEndpoint.java:1264) ~[tomcat-embed-core-10.1.40.jar:10.1.40]
	at org.apache.tomcat.util.net.NioEndpoint$Poller.processKey(NioEndpoint.java:826) ~[tomcat-embed-core-10.1.40.jar:10.1.40]
	at org.apache.tomcat.util.net.NioEndpoint$Poller.run(NioEndpoint.java:793) ~[tomcat-embed-core-10.1.40.jar:10.1.40]
	at java.base/java.lang.Thread.run(Thread.java:833) ~[na:na]

---

## 🔄 3. DispatcherServlet — The Front Controller

Spring’s `DispatcherServlet` is registered in `web.xml` (Spring MVC) or auto-configured (Spring Boot).

---

## 🧭 4. Request Mapping and Routing

Spring uses annotations to route requests:

```java
@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.findById(id);
    }
}


Flow:
    DispatcherServlet → HandlerMapping locates UserController#getUser
    
    HandlerAdapter invokes the method
    
    Return value is written to the HTTP response via HttpMessageConverter.

---

## 🔃 5. Request Data Binding

Spring automatically maps:

    @PathVariable, @RequestParam from URL
    
    @RequestBody from JSON payload

Converts request input into method arguments using HandlerMethodArgumentResolver.

---

##  📤 6. Response Generation

The controller method returns:

    A view name (for MVC) or
    
    A Java object (for REST APIs)

Spring uses HttpMessageConverter (like MappingJackson2HttpMessageConverter) to serialize response objects to JSON.

The response is written back to the client by the DispatcherServlet.

---

## ⚙️ 7. Thread Reuse & Lifecycle

Once the response is sent:

    The thread returns to the Tomcat thread pool.
    
    Ready to handle another request.

If requests come faster than threads can finish, and both maxThreads and acceptCount are exceeded, thread exhaustion happens.





