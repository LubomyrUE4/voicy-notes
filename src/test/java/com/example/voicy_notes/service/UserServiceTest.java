package com.example.voicy_notes.service;

import com.example.voicy_notes.entity.User;
import com.example.voicy_notes.exception.BadRequestException;
import com.example.voicy_notes.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserServiceTest {


    @Mock
    private UserRepository userRepository;
    private UserService userService;
    private User user;

    @BeforeEach
    public void setUp() {
        userService = new UserService(userRepository);
        user = new User(
                "test",
                "test",
                "test"
        );
    }

    @Test
    public void shouldSaveUser() {
        userService.saveUser(user);

        ArgumentCaptor<User> userArgumentCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userArgumentCaptor.capture());
        assertEquals(user, userArgumentCaptor.getValue());
    }

    @Test
    public void shouldThrowExceptionForExistingUser() {
        BDDMockito.given(userRepository.existsByEmail(user.getEmail())).willReturn(true);

        assertThrows(BadRequestException.class, () -> userService.saveUser(user));
    }
}