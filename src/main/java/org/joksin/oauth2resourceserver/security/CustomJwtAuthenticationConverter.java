package org.joksin.oauth2resourceserver.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final String principalNameKey;

    public CustomJwtAuthenticationConverter(String principalNameKey) {
        this.principalNameKey = principalNameKey;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = convertJwtToGrantedAuthorityCollection(jwt);
        return new JwtAuthenticationToken(jwt, authorities, jwt.getClaimAsString(principalNameKey));
    }

    private Collection<GrantedAuthority> convertJwtToGrantedAuthorityCollection(Jwt jwt) {
        List<String> scopes = jwt.getClaimAsStringList("scope");
        log.info("scopes: {}", scopes);
        return scopes.stream().map(scope -> new SimpleGrantedAuthority(scope)).collect(Collectors.toList());
    }

}
