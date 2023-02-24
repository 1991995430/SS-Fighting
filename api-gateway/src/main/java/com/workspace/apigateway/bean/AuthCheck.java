package com.workspace.apigateway.bean;

import lombok.Data;

@Data
public class AuthCheck {
    private String token;
    private String url;
}
