package com.gamedepot.admin.service;

import com.gamedepot.admin.model.RolUsuario;
import com.gamedepot.admin.model.Usuario;
import com.gamedepot.admin.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarClientes() {
        return usuarioRepository.findByRol(RolUsuario.CLIENTE);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con id: " + id));
    }

    public Usuario registrarAdminSiNoExiste(String nombre, String email, String passwordPlana, String telefono) {
        return usuarioRepository.findByEmail(email).orElseGet(() -> {
            Usuario admin = new Usuario(
                    null,
                    nombre,
                    email,
                    passwordEncoder.encode(passwordPlana),
                    telefono != null ? telefono : "6621000000",
                    RolUsuario.ADMIN,
                    LocalDateTime.now()
            );
            return usuarioRepository.save(admin);
        });
    }

    public Usuario registrarClienteSiNoExiste(String nombre, String email, String passwordPlana, String telefono) {
        return usuarioRepository.findByEmail(email).orElseGet(() -> {
            Usuario cliente = new Usuario(
                    null,
                    nombre,
                    email,
                    passwordEncoder.encode(passwordPlana),
                    telefono != null ? telefono : "6622000000",
                    RolUsuario.CLIENTE,
                    LocalDateTime.now()
            );
            return usuarioRepository.save(cliente);
        });
    }
}
