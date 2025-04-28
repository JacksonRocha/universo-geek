package br.com.algaworks.documentacao.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private final InMemoryUserDetailsManager userDetailsManager;
    private final Map<String, String> userPermissions = new HashMap<>();

    public UserService(InMemoryUserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
    }

    public void createUser(String username, String password, String role) {
        UserDetails user = User.builder()
                .username(username)
                .password(password)  // Aqui já deve estar criptografado
                .roles(role)
                .build();
        userDetailsManager.createUser(user);
        userPermissions.put(username, role);
    }

    public String getUserPermission(String username) {
        return userPermissions.get(username);
    }
}
