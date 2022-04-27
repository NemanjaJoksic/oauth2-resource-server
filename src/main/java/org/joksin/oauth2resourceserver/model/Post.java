package org.joksin.oauth2resourceserver.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class Post {

    @Setter
    private Integer id;
    private String sender;
    private String message;

}
