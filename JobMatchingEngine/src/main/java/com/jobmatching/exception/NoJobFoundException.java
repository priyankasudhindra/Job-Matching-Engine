package com.jobmatching.exception;

import org.apache.commons.lang3.builder.ToStringBuilder;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NoJobFoundException extends Exception {

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("No jobs found").toString();
    }
}
