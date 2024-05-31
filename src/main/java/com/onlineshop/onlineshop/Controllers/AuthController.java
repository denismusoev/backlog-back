package com.onlineshop.onlineshop.Controllers;

import com.onlineshop.onlineshop.ApiResponse1;
import com.onlineshop.onlineshop.ApiService;
import com.onlineshop.onlineshop.ResponseMessageDto;
import com.onlineshop.onlineshop.Exceptions.CustomExceptions.AuthenticationFailureException;
import com.onlineshop.onlineshop.JwtUtil;
import com.onlineshop.onlineshop.Models.DTO.SilentAuthDTO;
import com.onlineshop.onlineshop.Models.DTO.User.SignUpDTO;
import com.onlineshop.onlineshop.Models.TwoFactorCodeDTO;
import com.onlineshop.onlineshop.Models.vk.ApiResponse;
import com.onlineshop.onlineshop.Models.vk.VkUserPartialDto;
import com.onlineshop.onlineshop.Services.AuthService;
import com.onlineshop.onlineshop.Services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private ApiService apiService;
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);


    @PostMapping("/verifyTwoFactorCode")
    public ResponseEntity<ApiResponse> verifyTwoFactorCode(@RequestBody TwoFactorCodeDTO twoFactorCodeDTO) {
        return ResponseEntity.ok(authService.validateAndGenerateJwt(twoFactorCodeDTO));
    }

//    @PostMapping("/exchangeSilentAuthToken")
//    public Mono<ResponseEntity<ApiResponse<?>>> exchangeSilentAuthToken(@RequestBody SilentAuthDTO silentAuthDTO) {
//        return authService.exchangeAndRetrieveProfile(silentAuthDTO.getSilentToken(), silentAuthDTO.getUuid())
//                .map(ResponseEntity::ok)
//                .onErrorResume(AuthenticationFailureException.class, e -> {
//                    ApiResponse<?> apiResponseWithData = ApiResponse.withMessage(e.getMessage());
//                    return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponseWithData));
//                })
//                .onErrorResume(e ->
//                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("df", "Внутренняя ошибка сервера")))
//                )
//                .defaultIfEmpty(ResponseEntity.badRequest().body(new ApiResponse("sds", "Неверный silent token или UUID")));
//    }


    @PostMapping("/exchangeSilentAuthToken")
    public CompletableFuture<ResponseEntity<ApiResponse>> exchangeSilentAuthToken(@RequestBody SilentAuthDTO silentAuthDTO) {
        return authService.exchangeAndRetrieveProfile(silentAuthDTO.getSilentToken(), silentAuthDTO.getUuid())
                .thenApply(body -> ResponseEntity.ok().body(body))
                .exceptionally(e -> {
                    Throwable cause = e.getCause();
                    if (cause instanceof AuthenticationFailureException) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false,e.getMessage()){});
                    }
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false,e.getMessage()){});
                });
    }



    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody SignUpDTO signUpDTO) {
        userService.registerUser(signUpDTO);
        return ResponseEntity.ok(new ApiResponse(true,"Регистрация прошла успешно!"){});
    }

//    @PostMapping("/registerVk")
//    public ResponseEntity<?> registerUserVk(@RequestBody SignUpDTO signUpDTO) {
//        AuthResponse response = authService.registerUserVk(signUpDTO);
//        return ResponseEntity.status(response.getJwt() == null ? HttpStatus.UNAUTHORIZED : HttpStatus.OK).body(response);
//    }

//    @PostMapping("/authenticate")
//    public Mono<ResponseEntity<ApiResponse<String>>> authenticate(@RequestBody AuthRequest request) {
//        return authService.authenticateUser(request)
//                .map(ResponseEntity::ok)
//                .onErrorResume(e -> {
//                    if (e instanceof AuthenticationFailureException) {
//                        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.withMessage(e.getMessage())));
//                    } else {
//                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.withMessage("Внутренняя ошибка сервера")));
//                    }
//                });
//    }

    @PostMapping("/authenticate")
    public CompletableFuture<ResponseEntity<ApiResponse>> authenticate(@RequestBody AuthRequest request) {
        return authService.authenticateUser(request)
                .thenApply(ResponseEntity::ok)
                .exceptionally(e -> {
                    if (e.getCause() instanceof AuthenticationFailureException) {
                        logger.info("Username from request: {}",
                                request.getUsername());
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse(false, e.getMessage()){});
                    } else {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, e.getMessage()){});
                    }
                });
    }

}

