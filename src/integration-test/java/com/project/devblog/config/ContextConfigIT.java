package com.project.devblog.config;

import com.project.devblog.service.*;
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
