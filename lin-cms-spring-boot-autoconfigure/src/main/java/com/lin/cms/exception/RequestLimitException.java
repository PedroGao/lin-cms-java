package com.lin.cms.exception;

import com.lin.cms.beans.Code;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public class RequestLimitException extends HttpException {

    @Getter
    protected int code = Code.REQUEST_LIMIT.getCode();

    @Getter
    protected int httpCode = HttpStatus.TOO_MANY_REQUESTS.value();

    public RequestLimitException(String message) {
        super(message);
    }

    public RequestLimitException() {
        super(Code.REQUEST_LIMIT.getDescription());
    }

    public RequestLimitException(int code) {
        super(Code.REQUEST_LIMIT.getDescription());
        this.code = code;
    }

    public RequestLimitException(String message, int code) {
        super(message);
        this.code = code;
    }
}
