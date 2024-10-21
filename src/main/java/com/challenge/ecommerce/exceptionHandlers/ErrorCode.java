package com.challenge.ecommerce.exceptionHandlers;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNAUTHENTICATED(401, "Unauthorized access. Please log in.",HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND(404, "user not found !", HttpStatus.NOT_FOUND),
    ARTICLE_NOT_FOUND(404, "article not found !", HttpStatus.NOT_FOUND),
    PASSWORD_INCORRECT(401, "Password incorrect, please enter another password", HttpStatus.UNAUTHORIZED),
    TOKEN_CREATION_FAILED(400, "Token creation failed: bad request", HttpStatus.BAD_REQUEST),
    USER_EXISTED(400, "user already existed", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_FAILED(400, "refresh token failed: bad request", HttpStatus.BAD_REQUEST),
    URL_NOT_EXIST(404,"The requested URL does not exist." , HttpStatus.NOT_FOUND),
    FOLLOWER_EXISTED(400, "You are already following this user.",HttpStatus.BAD_REQUEST),
    FOLLOWER_NOT_EXIST(400, "You have never followed this user.",HttpStatus.BAD_REQUEST),
    SELF_FOLLOW_NOT_ALLOWED(400, "You cannot follow yourself.", HttpStatus.BAD_REQUEST),
    FAVORITE_ARTICLE_EXISTED(400, "You are already favorite this Article.",HttpStatus.BAD_REQUEST),
    FAVORITE_ARTICLE_NOT_EXIST(400, "You have never favorite this Article.",HttpStatus.BAD_REQUEST),
    COMMENT_NOT_OWNED_BY_USER(400, "You are not the owner of this comment.", HttpStatus.BAD_REQUEST),
    ARTICLE_NOT_OWNED_BY_USER(400, "You are not the owner of this article.", HttpStatus.BAD_REQUEST),
    PAGE_SIZE_POSITIVE(400, "The page size must be greater than 0", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_INVALID(401, "Refresh token is invalid or expired.", HttpStatus.UNAUTHORIZED);

    ;

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
