package org.joksin.oauth2resourceserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.joksin.oauth2resourceserver.model.Post;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class UserController {

    @PreAuthorize("hasAuthority('profile')")
    @GetMapping ("/api/users/me")
    public ResponseEntity<Authentication> getCurrentUser() {
        Authentication principal = getPrincipal();
        return new ResponseEntity(principal, HttpStatus.OK);
    }

    private Authentication getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String principalName = authentication.getName();
        log.info("principal: {}", principal);
        log.info("principal_name: {}", principalName);
        return authentication;
    }

}
