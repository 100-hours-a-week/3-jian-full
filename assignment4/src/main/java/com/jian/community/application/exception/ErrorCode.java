package com.jian.community.application.exception;

public enum ErrorCode {

    /*
      400 Bad Request
     */
    USER_INPUT_REQUIRED,
    INVALID_USER_INPUT,
    INVALID_CREDENTIALS,
    INVALID_IMAGE_FORMAT,

    /*
      401 Unauthorized
     */
    AUTHENTICATION_REQUIRED,

    /*
      403 Forbidden
     */
    ACCESS_DENIED,

    /*
      404 Not Found
     */
    USER_NOT_EXISTS,
    POST_NOT_EXISTS,
    COMMENT_NOT_EXISTS,
    POST_VIEW_NOT_EXISTS,

    /*
      409 Conflict
     */
    USER_ALREADY_EXISTS,
    POST_LIKE_ALREADY_EXISTS,

    /*
      413 Content Too Large
     */
    IMAGE_TOO_LARGE,

    /*
      429 Too Many Requests
     */
    TOO_MANY_REQUESTS,

    /*
      500 Internal Server Error
     */
    INTERNAL_SERVER_ERROR
}
