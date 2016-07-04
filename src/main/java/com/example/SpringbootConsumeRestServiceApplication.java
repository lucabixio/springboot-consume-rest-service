package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SpringbootConsumeRestServiceApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SpringbootConsumeRestServiceApplication.class);

    public static void main(String args[]) {
        SpringApplication.run(SpringbootConsumeRestServiceApplication.class);
    }

    @Override
    public void run(String... args) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        Quote quote = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
        log.info(quote.toString());
        System.out.println();
        System.out.println(quote.toString());
        System.out.println();
    }
}

