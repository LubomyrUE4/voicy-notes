package com.example.voicy_notes.controller;

import com.example.voicy_notes.config.Jwt.JwtUtils;
import com.example.voicy_notes.entity.User;
import com.example.voicy_notes.payload.request.LoginRequest;
import com.example.voicy_notes.payload.request.RegisterRequest;
import com.example.voicy_notes.service.NoteService;
import com.example.voicy_notes.service.UserService;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final NoteService noteService;
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder encoder;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, NoteService noteService, UserService userService, JwtUtils jwtUtils, PasswordEncoder encoder) {
        this.authenticationManager = authenticationManager;
        this.noteService = noteService;
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.encoder = encoder;
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        if (!userService.existsByUsername(loginRequest.getUsername()) || !userService.existsByUsername(loginRequest.getUsername())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);
        JSONObject jsonObject = new JSONObject();

        if(authentication.isAuthenticated()) {
            try {
                jsonObject.put("token", jwt);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userService.existsByUsername(registerRequest.getUsername()) || userService.existsByEmail(registerRequest.getEmail())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User user = new User(registerRequest.getUsername(), registerRequest.getEmail(),
                encoder.encode(registerRequest.getPassword()));

        userService.saveUser(user);
        noteService.saveDefaultNote(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}