package com.project.devblog.integration.config;

import com.project.devblog.service.ArticleService;
import com.project.devblog.service.AuthenticationService;
import com.project.devblog.service.SubscriptionService;
import com.project.devblog.service.UserService;
import com.project.devblog.service.VerificationService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
public class ContextConfigIT {

    @MockBean
    AuthenticationService authenticationService;
    @MockBean
    VerificationService verificationService;
    @MockBean
    UserService userService;
    @MockBean
    SubscriptionService subscriptionService;
    @MockBean
    ArticleService articleService;
}
