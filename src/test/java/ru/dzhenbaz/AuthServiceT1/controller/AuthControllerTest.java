package ru.dzhenbaz.AuthServiceT1.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.dzhenbaz.AuthServiceT1.AbstractPostgresTest;
import ru.dzhenbaz.AuthServiceT1.dto.LoginRequest;
import ru.dzhenbaz.AuthServiceT1.dto.RefreshTokenRequest;
import ru.dzhenbaz.AuthServiceT1.dto.RegisterRequest;
import ru.dzhenbaz.AuthServiceT1.repository.UserRepository;

import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerTest extends AbstractPostgresTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private static String accessToken;
    private static String refreshToken;

    @BeforeEach
    void cleanDb() {
        userRepository.deleteAll();
    }

    @Test
    @Order(1)
    void shouldRegisterUser() throws Exception {
        RegisterRequest request = new RegisterRequest(
                "John Doe",
                "johndoe@example.com",
                "123456",
                Set.of("ROLE_GUEST")
        );

        var result = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andReturn();

        var response = objectMapper.readTree(result.getResponse().getContentAsString());
        accessToken = response.get("accessToken").asText();
        refreshToken = response.get("refreshToken").asText();
    }

    @Test
    @Order(2)
    void shouldLoginUser() throws Exception {
        // Сначала регистрируем
        shouldRegisterUser();

        LoginRequest request = new LoginRequest("John Doe", "123456");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists());
    }

    @Test
    @Order(3)
    void shouldNotLoginWithWrongPassword() throws Exception {
        // Зарегистрировать корректного
        shouldRegisterUser();

        LoginRequest request = new LoginRequest("John Doe", "wrongpassword");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid username or password")));
    }

    @Test
    @Order(4)
    void shouldRefreshAccessToken() throws Exception {
        shouldRegisterUser();

        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);

        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists());
    }

    @Test
    @Order(5)
    void shouldLogoutAndInvalidateToken() throws Exception {
        shouldRegisterUser();

        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);

        mockMvc.perform(post("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Successfully logged out")));

        // Попробуем повторно использовать refresh токен
        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Token has been revoked"));
    }

    @Test
    @Order(6)
    void shouldAccessGuestEndpointWithAccessToken() throws Exception {
        shouldRegisterUser();

        mockMvc.perform(get("/api/guest")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, guest!"));
    }

    @Test
    @Order(7)
    void shouldDenyAdminAccessForGuest() throws Exception {
        shouldRegisterUser();

        mockMvc.perform(get("/api/admin")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isForbidden());
    }
}
