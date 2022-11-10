package com.jobmatching.domain;

import lombok.Data;

@Data
public class JobSearchAddress {
    private String unit;
    private int maxJobDistance;
    private String longitude;
    private String latitude;
}
