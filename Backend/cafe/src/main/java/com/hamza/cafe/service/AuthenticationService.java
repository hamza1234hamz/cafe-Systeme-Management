package com.hamza.cafe.service;

import com.hamza.cafe.Wrapper.UserWrapper;
import com.hamza.cafe.dao.request.Response.JwtAuthenticationResponse;
import com.hamza.cafe.dao.request.SigninRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface AuthenticationService {
    ResponseEntity<String> signup(Map<String,String> request);

    ResponseEntity<String> signin(SigninRequest request);

    ResponseEntity<List<UserWrapper>> getAllUsers();
    ResponseEntity<String> update(Map<String,String> request);
    ResponseEntity<String> checkToken();
    ResponseEntity<String> changePassword(Map<String,String> requestMap);

    ResponseEntity<String> forgotPassword(Map<String,String> requestMap);












}
