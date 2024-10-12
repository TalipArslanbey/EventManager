package com.eventmanager.service;

import com.eventmanager.model.User;
import com.eventmanager.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    void testCreateUser() {
        // given
        User user = User.builder().username("testuser").email("test@example.com").password("password123").build();
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        User createdUser = userService.createUser(user);

        // then
        assertThat(createdUser.getUsername()).isEqualTo("testuser");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetUserById() {
        // given
        User user = User.builder().username("testuser").email("test@example.com").password("password123").build();
        when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.of(user));

        // when
        User foundUser = userService.getUserById(1L);

        // then
        assertThat(foundUser.getUsername()).isEqualTo("testuser");
        verify(userRepository, times(1)).findById(1L);
    }
}
