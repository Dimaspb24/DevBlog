package com.project.devblog;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class DevBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevBlogApplication.class, args);
    }
}
