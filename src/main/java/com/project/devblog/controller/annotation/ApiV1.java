package com.project.devblog.controller.annotation;

import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping("/v1")
public @interface ApiV1 {
}

