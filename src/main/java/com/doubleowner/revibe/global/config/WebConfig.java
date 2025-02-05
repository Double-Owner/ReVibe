package com.doubleowner.revibe.global.config;

import com.doubleowner.revibe.domain.user.entity.Role;
import com.doubleowner.revibe.global.config.filter.JwtAuthFilter;
import jakarta.servlet.DispatcherType;
import jodd.net.HttpMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    private static final String[] WHITE_LIST = { "/api/users/**", "/oauth2/**", "/login/oauth2/code/**", "/oauth/authorize"};
    private static final String[] AUTHENTICATED_URLS = {"/api/reviews/**","/api/executions", "/api/items/{itemId}/wishlists", "/api/wishlists", "/chats/**", "/chat/sendMessage/{roomId}", "/chatrooms/{roomId}/messages"};
    private static final String[] USER_ONLY_URLS = {"/api/accounts/**", "/api/buy-bids/**", "/api/sell-bids/**", "/api/carts/**",
            "/api/items/{itemId}/wishlists","/api/wishlists", "/api/v1/payments", "/v1/payments/confirm", "/confirm/payment", "payments", "/api/issued-coupons/**"};
    private static final String[] ADMIN_ONLY_URLS = {"/api/brands", "/api/coupons/**"};

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(WHITE_LIST).permitAll()
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.ERROR).permitAll()
                        // path 별로 접근이 가능한 권한 설정
                        // 로그인한 사용자만 접근 가능 (USER 또는 ADMIN)
                        .requestMatchers(AUTHENTICATED_URLS).authenticated()
                        // USER 궎한이 필요한 API
                        .requestMatchers(USER_ONLY_URLS).hasRole(Role.ROLE_USER.getName())
                        // ADMIN 권한이 필요한 API
                        .requestMatchers(ADMIN_ONLY_URLS).hasRole(Role.ROLE_ADMIN.getName())
                        .anyRequest().authenticated()
                )
                .exceptionHandling(handler -> handler
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
