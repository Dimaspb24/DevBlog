package com.project.devblog.controller;

import com.project.devblog.model.PersonalInfo;
import com.project.devblog.model.UserEntity;
import com.project.devblog.model.enums.Role;
import com.project.devblog.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GoogleAuthControllerTest {

    @MockBean
    UserService userService;
    @Autowired
    MockMvc mockMvc;

    @Test
    void registrationTest() throws Exception {
        final PersonalInfo personalInfo = new PersonalInfo("firstname", "lastname",
                "nickname", "photo", "user info", "891245646544");
        final UserEntity user = new UserEntity(UUID.randomUUID().toString(), "user@mail.ru", "encryptedPassword",
                Role.USER, true, null, personalInfo,
                List.of(), List.of(), List.of(), List.of(), Set.of(), Set.of());
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", user.getId());
        attributes.put("email", user.getLogin());
        attributes.put("given_name", user.getPersonalInfo().getFirstname());
        attributes.put("family_name", user.getPersonalInfo().getLastname());
        attributes.put("name", user.getPersonalInfo().getNickname());
        attributes.put("picture", user.getPersonalInfo().getPhoto());
        attributes.put("phone_number", user.getPersonalInfo().getPhone());
        attributes.put("role", Role.USER);

        GrantedAuthority authority = new OAuth2UserAuthority(attributes);

        when(userService.create(user.getId(), user.getLogin(), Role.USER, true, personalInfo.getFirstname(),
                personalInfo.getLastname(), personalInfo.getNickname(), personalInfo.getPhoto(), personalInfo.getPhone()))
                .thenReturn(user);
        DefaultOidcUser auth2User = new DefaultOidcUser(List.of(authority), OidcIdToken.withTokenValue("some token here").claims(stringObjectMap ->
                stringObjectMap.putAll(attributes)).build());
        Authentication authentication = new OAuth2AuthenticationToken(auth2User, List.of(authority), "sub");
        mockMvc
                .perform(get("/")
                        .with(authentication(authentication)))
                .andDo(print())
                .andExpect(status().isPermanentRedirect())
                .andExpect(redirectedUrl(String.format("http://localhost:8080/v1/users/%s/articles", user.getId())))
                .andReturn();
    }
}
