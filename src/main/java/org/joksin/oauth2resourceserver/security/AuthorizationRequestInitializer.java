package org.joksin.oauth2resourceserver.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@ConditionalOnProperty (name = "security.type", havingValue = "oauth2")
public class AuthorizationRequestInitializer {

    private static final String PERMIT_ALL = "permit-all";
    private static final String AUTHENTICATED = "authenticated";
    private static final String DENY_ALL = "deny-all";

    private List<AuthorizationRequestInfo> authorizationRequestInfoList;

    public AuthorizationRequestInitializer(ResourceLoader resourceLoader, ObjectMapper mapper) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:security-config.json");
        authorizationRequestInfoList = mapper.readValue(resource.getInputStream(), new TypeReference<List<AuthorizationRequestInfo>>() {});
    }

    public void authorizeRequests(HttpSecurity http) throws Exception {
        log.info("Loading security plan");
        for (AuthorizationRequestInfo authorizationRequestInfo : authorizationRequestInfoList) {
            log.info("Setting: {}", authorizationRequestInfo);

            ExpressionUrlAuthorizationConfigurer.AuthorizedUrl authorizedUrl;

            if (authorizationRequestInfo.getMethod() != null) {
                authorizedUrl = http.authorizeRequests().antMatchers(authorizationRequestInfo.getMethod(), authorizationRequestInfo.getPattern());
            } else {
                authorizedUrl = http.authorizeRequests().antMatchers(authorizationRequestInfo.getPattern());
            }

            switch (authorizationRequestInfo.getAccess()) {
                case PERMIT_ALL:
                    authorizedUrl.permitAll();
                    break;
                case AUTHENTICATED:
                    authorizedUrl.authenticated();
                    break;
                case DENY_ALL:
                    authorizedUrl.denyAll();
                    break;
                default:
                    authorizedUrl.hasAnyAuthority(authorizationRequestInfo.getAccess());
            }
        }
    }

    @Getter
    private static class AuthorizationRequestInfo {
        private String pattern;
        private String method;
        private String access;

        public HttpMethod getMethod() {
            if (method == null || method.equals("")) {
                return null;
            } else {
                return HttpMethod.resolve(method);
            }
        }

        @Override
        public String toString() {
            return String.format("method=%s, pattern=%s, access=%s", method, pattern, access);
        }

    }

}
