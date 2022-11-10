package com.jobmatching.exception;

import org.apache.commons.lang3.builder.ToStringBuilder;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BadRequestException extends Exception {

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("Bad request due to invalid parameters").toString();
    }
}
