package com.devmission.microservices.currencyexchange.circuitbreaker;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@Slf4j
public class FaultToleranceController {

    @GetMapping("sample-api-retry")
    // The "Retry pattern" enables an application to retry an operation in the expectation that the operation will eventually succeed
    // makes the method retry-able - if it fails once, it will be called again 3 times before returning a response
    // resilience4j.retry.instances.sample-api.maxRetryAttempts configures the number of times the retry is performed
    @Retry(name = "sample-api-retry", fallbackMethod = "getHardcodedResponse")
    public String sampleApiRetry() {
        log.info("Sample API call received!");
        // make it fail to trigger the retry mechanism
        return callFailingEndpoint();
    }

    private String callFailingEndpoint() {
        final ResponseEntity<String> response = new RestTemplate().getForEntity("http://localhost:8080/url-causing-expected-failure", String.class);
        return response.getBody();
    }

    @GetMapping("sample-api-circuit-breaker")
    // The Circuit Breaker pattern prevents an application from performing an operation that's likely to fail.
    // doesn't call the method anymore if a threshold percentage of the requests are failing, it directly returns the result of the fallback method
    // if the microservice is failing for multiple requests it will avoid calling it altogether, as opposed to the retry method
    // from time to time, it sends a small percentage of the requests   to the microservice to check if it's back up
    @CircuitBreaker(name = "simple-api-circuit-breaker", fallbackMethod = "getHardcodedResponse")
    public String sampleApiCircuitBreaker() {
        log.info("Sample API call received!");
        // make it fail to trigger the circuit breaker mechanism
        return callFailingEndpoint();
    }

    @GetMapping("sample-api-rate-limiter")
    // limits the number of calls in a time window
    // any extra calls above the limit will fail
    @RateLimiter(name = "sample-api-rate-limiter")
    public String sampleApiRateLimiter() {
        log.info("Sample API call received!");
        return "Sample API Rate Limiter";
    }

    @GetMapping("sample-api-bulkhead")
    // The bulkhead implementation in Hystrix/Resilience4j limits the number of concurrent calls to a component to avoid overloading it and therefore slowing it down/making it fail.
    // This way, the number of resources (typically threads) that is waiting for a reply from the component is limited.
    // at most X request-handling threads can hang when calling this method/microservice, while the others are free to handle requests to other services
    // X = the limit for the concurrent calls
    @Bulkhead(name = "sample-api-bulkhead")
    public String sampleApiBulkhead() {
        log.info("Sample API call received!");
        return "Sample API Bulkhead";
    }

    // must have a Throwable parameter
    public String getHardcodedResponse(Exception ex) {
        return "Fallback response";
    }
}
