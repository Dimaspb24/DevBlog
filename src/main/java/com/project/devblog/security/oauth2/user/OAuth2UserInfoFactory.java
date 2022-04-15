package com.project.devblog.security.oauth2.user;

import com.project.devblog.exception.OAuth2AuthenticationProcessingException;
import com.project.devblog.model.enums.AuthProvider;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(AuthProvider.GOOGLE.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}
