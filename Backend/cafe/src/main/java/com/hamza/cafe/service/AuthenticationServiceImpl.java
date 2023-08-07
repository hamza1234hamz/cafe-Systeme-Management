package com.hamza.cafe.service;

import com.hamza.cafe.Exception.CafeUtils;
import com.hamza.cafe.Exception.MessageError.CafeErros;
import com.hamza.cafe.Repository.UserRepository;
import com.hamza.cafe.Wrapper.UserWrapper;
import com.hamza.cafe.dao.request.Response.JwtAuthenticationResponse;
import com.hamza.cafe.dao.request.SigninRequest;
import com.hamza.cafe.entities.Role;
import com.hamza.cafe.entities.User;
import com.hamza.cafe.security.JwtService;
import com.hamza.cafe.security.config.JwtAuthenticationFilter;
import com.hamza.cafe.service.Email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;
    private final JwtAuthenticationFilter filter;
    private final EmailService emailService;

    @Override
    public ResponseEntity<String> signup(Map<String,String> request) {
        if (validateSingup(request)){
            User user = userRepository.findByEmail(request.get("email")).orElse(null);
            if (Objects.isNull(user)){
                userRepository.save(getUserFromMap(request));
                return CafeUtils.getResponseEntity("Successfully Registred", HttpStatus.OK);
            }
            else {
                return CafeUtils.getResponseEntity("Email alredy exist", HttpStatus.BAD_REQUEST);
            }
        }
        else{
            return CafeUtils.getResponseEntity(CafeErros.INVALID_DATA,HttpStatus.BAD_REQUEST);
        }
    }
    private boolean validateSingup(Map<String,String> request){
        if (request.containsKey("name") && request.containsKey("contactNumber")
        && request.containsKey("email") && request.containsKey("password")){
            return true;
        }
        else {
            return false;
        }
    }
    private User getUserFromMap(Map<String,String> request){
        User user = new User();
        user.setName(request.get("name"));
        user.setContactNumber(request.get("contactNumber"));
        user.setEmail(request.get("email"));
        user.setPassword(request.get("password"));
        user.setStatus("false");
        user.setRole(Role.admin);
        return user;
    }

    @Override
    public ResponseEntity<String> signin(SigninRequest request) {
        try {
            Authentication auth= authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            if (auth.isAuthenticated()){
                var user = userRepository.findByEmail(request.getEmail())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
                if (user.getStatus().equalsIgnoreCase("true")){
                    var jwt = jwtService.generateToken(user.getUsername(),user.getRole());
                    return new ResponseEntity<>("{\"token\":\""+
                            jwt+"\"}",HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<>("{\"message\":\""+"wait for admin approval."+"\"}",HttpStatus.BAD_REQUEST);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeErros.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUsers() {
        try{
            if (filter.isAdmin()){
                return new ResponseEntity<>(userRepository.getAllUser(),HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String,String> request) {
        log.info("insz");
        try{
            if (filter.isAdmin()){
               Optional<User> optional=userRepository.findById(Integer.parseInt(request.get("id")));
               if (!optional.isEmpty()){
                    userRepository.updateStatus(request.get("status"),Integer.parseInt(request.get("id")));
                    SendMailToAllAdmin(request.get("status"),optional.get().getEmail(),userRepository.getAllAdmin());
                   CafeUtils.getResponseEntity("User Status updated successfully.",HttpStatus.OK);
               }
               else {
                   CafeUtils.getResponseEntity("User id does not exist.",HttpStatus.OK);
               }
            }
            else {
                return CafeUtils.getResponseEntity(CafeErros.UNAUTHORIWED_ACCES,HttpStatus.UNAUTHORIZED);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity("User Status updated successfully.",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> checkToken() {
        return CafeUtils.getResponseEntity("true",HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            User userObj=userRepository.findByEmail(filter.getCurrentUset()).orElse(null);
            if (!userObj.equals(null)){
                if (userObj.getPassword().equals(requestMap.get("oldPassword"))){
                    userObj.setPassword(requestMap.get("newPasswor"));
                    userRepository.save(userObj);
                    return CafeUtils.getResponseEntity("Password Updated Succefully",HttpStatus.OK);
                }
                else{
                    return CafeUtils.getResponseEntity("Incorrect Old Password",HttpStatus.BAD_REQUEST);
                }
            }
            return CafeUtils.getResponseEntity(CafeErros.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeErros.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            User user=userRepository.findByEmail(requestMap.get("email")).orElse(null);
            if (!Objects.isNull(user) && user.getEmail()!=null){
                emailService.forgotMail(user.getEmail(),"Credentials by Management System",user.getPassword());
                return CafeUtils.getResponseEntity("check your mail for Credentials",HttpStatus.OK);
            }
            else {
                return CafeUtils.getResponseEntity("account does not exist",HttpStatus.OK);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeErros.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void SendMailToAllAdmin(String status, String user, List<String> allAdmin) {
      log.info("inside send mail");
        allAdmin.remove(filter.getCurrentUset());
        if (status!=null && status.equalsIgnoreCase("true")){
            log.info("inside first if");
            emailService.sendSimplMessage(filter.getCurrentUset(),"Account Approved","User :-"+ user +"\n is approved by \nAdmin:-"+filter.getCurrentUset(),allAdmin);
        }else {
            log.info("inside else");
            emailService.sendSimplMessage(filter.getCurrentUset(),"Account Disabled","User :-"+ user +"\n is Disabled by \nAdmin:-"+filter.getCurrentUset(),allAdmin);
        }
    }


}

