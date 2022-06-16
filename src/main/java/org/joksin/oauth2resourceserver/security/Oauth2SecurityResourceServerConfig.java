package org.joksin.oauth2resourceserver.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Slf4j
@EnableWebSecurity
@EnableGlobalMethodSecurity (prePostEnabled = true)
@ConditionalOnProperty (name = "security.type", havingValue = "oauth2")
public class Oauth2SecurityResourceServerConfig extends WebSecurityConfigurerAdapter {

    private final AuthorizationRequestInitializer authorizationRequestInitializer;

    public Oauth2SecurityResourceServerConfig(AuthorizationRequestInitializer authorizationRequestInitializer) {
        this.authorizationRequestInitializer = authorizationRequestInitializer;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

//        authorizationRequestInitializer.authorizeRequests(httpSecurity);
        
        httpSecurity
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(this::configureJwtConfigurer))
                .csrf()
                .disable();
    }

    private void configureJwtConfigurer(OAuth2ResourceServerConfigurer.JwtConfigurer jwtConfigurer) {
        jwtConfigurer
                .decoder(NimbusJwtDecoder.withJwkSetUri("http://localhost:8081/realms/master/protocol/openid-connect/certs").build())
                .jwtAuthenticationConverter(new CustomJwtAuthenticationConverter("sub"));
    }
}
