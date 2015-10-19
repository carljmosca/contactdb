/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.moscaville.contactsdb.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;
import net.anthavio.httl.HttlSender;
import net.anthavio.httl.auth.OAuth2;
import net.anthavio.httl.auth.OAuth2Builder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OAuthUtil {

    @Value("${oauth.google.client.id}")
    private String googleClientId;
    @Value("${oauth.google.client.secret}")
    private String googleClientSecret;
    @Value("${oauth.facebook.client.id}")
    private String facebookClientId;
    @Value("${oauth.facebook.client.secret}")
    private String facebookClientSecret;

    @Value("${oauth.redirect.uri}")
    private String redirectUri;

    List<OAuthProvider> oAuthProviders;

    public OAuthUtil() {
    //    init();
    }

    @PostConstruct
    private void init() {
        System.out.println(facebookClientId);
        oAuthProviders = new ArrayList<>();
        oAuthProviders.add(new OAuthProvider(OAuthProvider.OAuthProviderType.GOOGLE, buildGoogle(), "email"));
        oAuthProviders.add(new OAuthProvider(OAuthProvider.OAuthProviderType.FACEBOOK, buildFacebook(), "email"));
        //oAuthProviders.add(new OAuthProvider(OAuthProvider.OAuthProviderType.LINKEDIN, buildLinkedIn(properties, redirectUri), "r_basicprofile"));
        //oAuthProviders.add(new OAuthProvider(OAuthProvider.OAuthProviderType.GITHUB, buildGithub(properties, redirectUri), ""));
    }

    public OAuthProvider getByName(String provider) {
        for (OAuthProvider oAuthProvider : oAuthProviders) {
            if (oAuthProvider.getType().name().equalsIgnoreCase(provider)) {
                return oAuthProvider;
            }
        }
        throw new IllegalArgumentException("No provider: " + provider);
    }

    private OAuth2 buildGithub(Properties properties, String redirectUri) {
        HttlSender sender = HttlSender.url("https://github.com").httpClient4().sender()
                .addHeader("Accept", "application/json").build();

        String clientId = properties.getProperty("github.client_id");
        String clientSecret = properties.getProperty("github.client_secret");
        redirectUri = redirectUri.replace("{provider}", "github");

        OAuth2 oauth = new OAuth2Builder().setClientId(clientId).setClientSecret(clientSecret)
                .setTokenEndpoint(sender, "/login/oauth/access_token").setAuthorizationUrl("/login/oauth/authorize")
                .setRedirectUri(redirectUri).build();
        return oauth;
    }

    private OAuth2 buildGoogle() {
        HttlSender sender = HttlSender.url("https://accounts.google.com").httpClient4().sender().build();

        OAuth2 oauth = new OAuth2Builder().setClientId(googleClientId).setClientSecret(googleClientSecret)
                .setTokenEndpoint(sender, "/o/oauth2/token").setAuthorizationUrl("/o/oauth2/auth").setRedirectUri(redirectUri.replace("{provider}", "google"))
                .build();
        return oauth;
    }

    private OAuth2 buildLinkedIn(Properties properties, String redirectUri) {
        HttlSender sender = HttlSender.url("https://www.linkedin.com").httpClient4().sender().build();

        String clientId = properties.getProperty("linkedin.client_id");
        String clientSecret = properties.getProperty("linkedin.client_secret");
        redirectUri = redirectUri.replace("{provider}", "linkedin");

        OAuth2 oauth = new OAuth2Builder().setClientId(clientId).setClientSecret(clientSecret)
                .setTokenEndpoint(sender, "/uas/oauth2/accessToken").setAuthorizationUrl("/uas/oauth2/authorization")
                .setRedirectUri(redirectUri).build();
        return oauth;
    }

    /**
     * https://developers.facebook.com/docs/facebook-login/manually-build-a-login-flow/v2.1
     */
    private OAuth2 buildFacebook() {
        HttlSender sender = HttlSender.url("https://graph.facebook.com").httpClient4().sender().build();

        OAuth2 oauth = new OAuth2Builder().setClientId(facebookClientId).setClientSecret(facebookClientSecret)
                .setAuthParam("display", "popup").setTokenEndpoint(sender, "/oauth/access_token")
                .setAuthorizationUrl("https://www.facebook.com/dialog/oauth").setRedirectUri(redirectUri.replace("{provider}", "facebook")).build();
        return oauth;
    }

}
