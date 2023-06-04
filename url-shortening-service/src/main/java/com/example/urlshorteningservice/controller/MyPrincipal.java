package com.example.urlshorteningservice.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyPrincipal {
    private int userId;
    private String sessionId;
}