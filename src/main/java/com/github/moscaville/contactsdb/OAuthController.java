package com.github.moscaville.contactsdb;

import com.github.moscaville.contactsdb.ValoSideBarUI;
import com.github.moscaville.contactsdb.auth.OAuthProvider;
import com.github.moscaville.contactsdb.auth.OAuthProvider.OAuthProviderType;
import static com.github.moscaville.contactsdb.auth.OAuthProvider.OAuthProviderType.FACEBOOK;
import static com.github.moscaville.contactsdb.auth.OAuthProvider.OAuthProviderType.GITHUB;
import static com.github.moscaville.contactsdb.auth.OAuthProvider.OAuthProviderType.GOOGLE;
import static com.github.moscaville.contactsdb.auth.OAuthProvider.OAuthProviderType.LINKEDIN;
import com.github.moscaville.contactsdb.auth.OAuthUtil;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.anthavio.httl.HttlResponse;
import net.anthavio.httl.HttlResponseExtractor;
import net.anthavio.httl.HttlSender;
import net.anthavio.httl.auth.OAuthTokenResponse;
import net.anthavio.httl.util.HttlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
@Controller
@RequestMapping("/oauth")
public class OAuthController {

    @Autowired
    OAuthUtil oAuthUtil;
    @Autowired
    private HttpSession httpSession;

    public OAuthController() {

    }

    @PostConstruct
    private void init() {
    }

    @RequestMapping(value = "/authorize/{provider}", method = RequestMethod.GET)
    public String authorize(@PathVariable("provider") String provider) {
        OAuthProvider op = oAuthUtil.getByName(provider);
        String url = op.getOAuth().getAuthorizationUrl(op.getScopes(), String.valueOf(System.currentTimeMillis()));
        return "redirect:" + url;
    }

    @RequestMapping(value = "/callback/{provider}", method = RequestMethod.GET)
    public String oauthErrorCallback(@PathVariable("provider") String provider,
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "error_description", required = false) String error_description,
            @RequestParam(value = "code") String code,
            HttpServletRequest request) {
        httpSession.removeAttribute(ValoSideBarUI.ERROR_ATTRIBUTE);
        httpSession.removeAttribute(ValoSideBarUI.EMAIL_ATTRIBUTE);

        if (error != null) {
            httpSession.setAttribute(ValoSideBarUI.ERROR_ATTRIBUTE, error);
        } else {

            OAuthProviderType p = OAuthProvider.getByName(provider);
            OAuthTokenResponse tokenResponse;

            String email;
            switch (p) {
                case FACEBOOK:
                    email = facebook(code);
                    break;
                case GOOGLE:
                    tokenResponse = oAuthUtil.getByName(provider).getOAuth().access(code).get();
                    email = google(tokenResponse.getAccess_token());
                    break;
                case GITHUB:
                    tokenResponse = oAuthUtil.getByName(provider).getOAuth().access(code).get();
                    email = github(tokenResponse.getAccess_token());
                    break;
                case LINKEDIN:
                    tokenResponse = oAuthUtil.getByName(provider).getOAuth().access(code).get();
                    email = linkedin(tokenResponse.getAccess_token());
                    break;
                default:
                    throw new IllegalStateException("Unknown " + p);
            }
            httpSession.setAttribute(ValoSideBarUI.EMAIL_ATTRIBUTE, email);
        }
        return "redirect:/#!contacts";
    }

    private String facebook(String code) {
        //https://developers.facebook.com/docs/graph-api/reference/v2.1/user

        Map<String, String> mapOfToken = oAuthUtil.getByName(OAuthProviderType.FACEBOOK.name()).getOAuth().access(code)
                .get(new XFormEncodedExtractor("text/plain"));
        String access_token = mapOfToken.get("access_token");

        HttlSender sender = HttlSender.url("https://graph.facebook.com").build();
        HttlResponseExtractor.ExtractedResponse<Map> response = sender.GET("/me").header("Authorization", "Bearer " + access_token)
                .extract(Map.class);
        Map map = response.getBody();
        return (String) map.get("email");
    }

    private String linkedin(String access_token) {
        //https://developer.linkedin.com/documents/profile-api
        HttlSender sender = HttlSender.url("https://api.linkedin.com").build();
        HttlResponseExtractor.ExtractedResponse<Map> response = sender.GET("/v1/people/~").header("Authorization", "Bearer " + access_token)
                .header("x-li-format", "json").extract(Map.class);
        Map map = response.getBody();
        //System.out.println(map);
        return (String) map.get("firstName") + " " + map.get("lastName");

    }

    private String google(String access_token) {
        HttlSender sender = HttlSender.url("https://www.googleapis.com").build();
        HttlResponseExtractor.ExtractedResponse<Map> response = sender.GET("/plus/v1/people/me")
                .header("Authorization", "Bearer " + access_token).extract(Map.class);
        Map map = response.getBody();
        List<LinkedHashMap> emails = (List<LinkedHashMap>) map.get("emails");
        String email = (String) emails.get(0).get("value");
        return email;
        //return (String) map.get("displayName");
    }

    private String github(String access_token) {
        HttlSender sender = HttlSender.url("https://api.github.com").build();
        HttlResponseExtractor.ExtractedResponse<Map> response = sender.GET("/user").header("Authorization", "token " + access_token)
                .extract(Map.class);
        Map map = response.getBody();
        return (String) map.get("login");
    }

    static class XFormEncodedExtractor implements HttlResponseExtractor<Map<String, String>> {

        private final String mediaType;

        public XFormEncodedExtractor(String mediaType) {
            this.mediaType = mediaType;
        }

        @Override
        public Map<String, String> extract(HttlResponse response) throws IOException {
            int code = response.getHttpStatusCode();
            if (code < 200 || code > 299) {
                throw new IllegalArgumentException("Unexpected status code " + response);
            }
            if (!mediaType.equals(response.getMediaType())) {
                throw new IllegalArgumentException("Unexpected media type " + response);
            }
            Map<String, String> map = new HashMap<>();
            String line = HttlUtil.readAsString(response);
            String[] pairs = line.split("\\&");
            for (String pair : pairs) {
                String[] fields = pair.split("=");
                String name = URLDecoder.decode(fields[0], response.getEncoding());
                String value = URLDecoder.decode(fields[1], response.getEncoding());
                map.put(name, value);
            }
            return map;
        }

    }

    private URI getURI(String value) {
        try {
            URI uri = new URI(value);
            return uri;
        } catch (URISyntaxException ex) {
            Logger.getLogger(OAuthController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
