package com.tecnova.technical_test.infrastructure.rest.controller;

import com.tecnova.technical_test.application.service.AuthService;
import com.tecnova.technical_test.domain.model.dto.LoginUserDto;
import com.tecnova.technical_test.domain.model.dto.NewUserDto;
import com.tecnova.technical_test.domain.model.dto.TaskDto;
import com.tecnova.technical_test.domain.model.dto.response.AuthRegisterResponse;
import com.tecnova.technical_test.domain.model.dto.response.BadLoginResponse;
import com.tecnova.technical_test.domain.model.dto.response.ErrorResponse;
import com.tecnova.technical_test.domain.model.dto.response.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.security.auth.message.AuthException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/app/auth")
@Tag(name = "Authentication Controller",
        description = "Controller to management all operation related with Authentication")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Get Token By User", responses = {
            @ApiResponse(responseCode = "200", description = "Token Created Correctly",
                    content = @Content(schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = BadLoginResponse.class)))},
            description = "Login is performed to generate the corresponding token")
    public ResponseEntity<?> login(@Valid @RequestBody LoginUserDto loginUserDto,
                                   BindingResult bindingResult) throws AuthException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new BadLoginResponse("Check your credentials"));
        }
        String jwt = authService.authenticate(loginUserDto.getUserName(), loginUserDto.getPassword());
        return ResponseEntity.ok(new TokenResponse(jwt, loginUserDto.userName));
    }

    @PostMapping("/register")
    @Operation(summary = "Register User in DB", responses = {
            @ApiResponse(responseCode = "200", description = "User Register Correctly",
                    content = @Content(schema = @Schema(implementation = AuthRegisterResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content(schema = @Schema(implementation = AuthRegisterResponse.class)))},
            description = "API to register a user in the Table where users who can interact with the APIs are stored")
    public ResponseEntity<?> register(@Valid @RequestBody NewUserDto newUserDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    new AuthRegisterResponse("Check the fields", newUserDto.getUserName(), newUserDto.getRole()));
        }
        authService.registerUser(newUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new AuthRegisterResponse("Registered", newUserDto.userName, newUserDto.getRole()));
    }

    @GetMapping("/check-auth")
    @Operation(summary = "Verify Token", description = "Check if the token sent in the Authorization header is correct")
    public ResponseEntity<String> checkAuth() {
        return ResponseEntity.ok().body("Authenticated");
    }
}
