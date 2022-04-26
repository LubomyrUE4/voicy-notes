package com.example.voicy_notes.controller;

import com.example.voicy_notes.config.Jwt.AuthEntryPointJwt;
import com.example.voicy_notes.config.Jwt.JwtUtils;
import com.example.voicy_notes.payload.request.LoginRequest;
import com.example.voicy_notes.payload.request.RegisterRequest;
import com.example.voicy_notes.repository.UserRepository;
import com.example.voicy_notes.service.NoteService;
import com.example.voicy_notes.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.api.gax.core.CredentialsProvider;
import com.google.cloud.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private AuthEntryPointJwt authEntryPointJwt;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private NoteService noteService;
    @MockBean
    private JwtUtils jwtUtils;
    @MockBean
    private Storage storage;
    @MockBean
    private CredentialsProvider credentialsProvider;

    @Test
    public void shouldNotAuthenticateNotExistingUser() throws Exception {
        LoginRequest loginRequest = new LoginRequest(
                "test",
                "test"
        );

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(loginRequest);

        when(userService.existsByUsername(loginRequest.getUsername())).thenReturn(false);
        mockMvc.perform(post("/authenticate").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotRegisterExistingUser() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest(
                "test",
                "test",
                "test"
        );

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(registerRequest);

        when(userService.existsByUsername(registerRequest.getUsername())).thenReturn(true);
        mockMvc.perform(post("/register").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());
    }
}