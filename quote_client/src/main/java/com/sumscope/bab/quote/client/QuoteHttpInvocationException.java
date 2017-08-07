package com.sumscope.bab.quote.client;

import com.sumscope.optimus.commons.exceptions.ExceptionDto;

/**
 * Created by fan.bai on 2017/2/23.
 */
public class QuoteHttpInvocationException extends RuntimeException {
    private final ExceptionDto exceptionDto;

    public QuoteHttpInvocationException(String msg, ExceptionDto exceptionDto){
        super(msg);
        this.exceptionDto = exceptionDto;
    }

    public ExceptionDto getExceptionDto() {
        return exceptionDto;
    }
}
