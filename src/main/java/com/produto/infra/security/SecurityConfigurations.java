package com.produto.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;


@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(req -> {
            RequestMatcher matcher = new AntPathRequestMatcher("/produtos", HttpMethod.POST.name());
            RequestMatcher matcher2 = new AntPathRequestMatcher("/produtos/{id}", HttpMethod.PATCH.name());
            RequestMatcher matcher3 = new AntPathRequestMatcher("/produtos/{id}", HttpMethod.DELETE.name());
            RequestMatcher matcher4 = new AntPathRequestMatcher("/produtos", HttpMethod.GET.name());
            RequestMatcher matcher5 = new AntPathRequestMatcher("/categorias", HttpMethod.GET.name());
            RequestMatcher matcher6 = new AntPathRequestMatcher("/categorias", HttpMethod.POST.name());
            RequestMatcher matcher7 = new AntPathRequestMatcher("/categorias/{id}", HttpMethod.DELETE.name());

            req.requestMatchers(matcher).hasRole("LOJA");
            req.requestMatchers(matcher2).hasRole("LOJA");
            req.requestMatchers(matcher3).hasRole("LOJA");
            req.requestMatchers(matcher4).permitAll(); 
            req.requestMatchers(matcher5).permitAll(); 
            req.requestMatchers(matcher6).hasRole("LOJA");
            req.requestMatchers(matcher7).hasRole("LOJA");

            req.anyRequest().authenticated();
         })
        .addFilterBefore(securityFilter, SecurityContextPersistenceFilter.class) 
        .build();
    }
}