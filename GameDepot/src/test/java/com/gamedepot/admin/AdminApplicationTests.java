package com.gamedepot.admin;

import com.gamedepot.admin.model.*;
import com.gamedepot.admin.repository.CategoriaRepository;
import com.gamedepot.admin.repository.OrdenRepository;
import com.gamedepot.admin.repository.UsuarioRepository;
import com.gamedepot.admin.repository.VarianteVideojuegoRepository;
import com.gamedepot.admin.repository.VideojuegoRepository;
import com.gamedepot.admin.service.CategoriaService;
import com.gamedepot.admin.service.InventarioService;
import com.gamedepot.admin.service.OrdenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class AdminApplicationTests {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private VideojuegoRepository videojuegoRepository;

    @Autowired
    private VarianteVideojuegoRepository varianteRepository;

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private InventarioService inventarioService;

    @Autowired
    private OrdenService ordenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("1. Entrar a /admin sin sesión redirige a /admin/login")
    void accesoSinSesionRedirigeALogin() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/login"));
    }

    @Test
    @DisplayName("2. Login con admin mock exitoso redirige a /admin")
    void loginConAdminExitoso() throws Exception {
        mockMvc.perform(formLogin("/admin/login")
                        .user("admin@gamedepot.com")
                        .password("admin123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"))
                .andExpect(authenticated().withRoles("ADMIN"));
    }

    @Test
    @DisplayName("3. Login con cliente no admin es rechazado")
    void loginConClienteRechazado() throws Exception {
        mockMvc.perform(formLogin("/admin/login")
                        .user("jesus.luzanilla@example.com")
                        .password("cliente123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/login?error=true"))
                .andExpect(unauthenticated());
    }

    @Test
    @DisplayName("4. Logout POST redirige a /admin/login?logout=true")
    void logoutPostRedirige() throws Exception {
        mockMvc.perform(post("/admin/logout").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/login?logout=true"))
                .andExpect(unauthenticated());
    }

    @Test
    @DisplayName("5. Contraseña en BD está hasheada con BCrypt y usuario tiene teléfono")
    void contrasenasHasheadasYTelefonoEnBD() {
        Usuario admin = usuarioRepository.findByEmail("admin@gamedepot.com").orElseThrow();
        assertThat(admin.getPassword()).startsWith("$2a$");
        assertThat(passwordEncoder.matches("admin123", admin.getPassword())).isTrue();
        assertThat(admin.getTelefono()).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("6. Regla CategoriaService: No borrar categoría con juegos asociados")
    void noBorrarCategoriaConJuegosAsociados() {
        Categoria catAccion = categoriaRepository.findByNombreIgnoreCase("Acción").orElseThrow();
        long asociados = videojuegoRepository.countByCategoriasContaining(catAccion);
        assertThat(asociados).isGreaterThan(0);

        assertThatThrownBy(() -> categoriaService.eliminar(catAccion.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No se puede eliminar la categoría");
    }

    @Test
    @DisplayName("7. Regla InventarioService: Validar que el stock de variante nunca quede negativo")
    void stockNuncaNegativo() {
        VarianteVideojuego variante = varianteRepository.findAll().get(0);
        int stockActual = variante.getStock();

        int exceso = stockActual + 10;
        assertThatThrownBy(() -> inventarioService.registrarAjuste(
                variante.getId(),
                TipoMovimientoInventario.MERMA,
                exceso,
                "Intento de merma excesiva",
                Collections.emptyList()
        )).isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("El stock no puede quedar negativo");
    }

    @Test
    @DisplayName("8. Regla InventarioService: Ajuste válido incrementa stock y crea movimiento")
    void ajusteValidoIncrementaStock() {
        VarianteVideojuego variante = varianteRepository.findAll().get(0);
        int stockInicial = variante.getStock();

        MovimientoInventario mov = inventarioService.registrarAjuste(
                variante.getId(),
                TipoMovimientoInventario.ABASTECIMIENTO,
                5,
                "Reabastecimiento de prueba unitaria",
                Collections.emptyList()
        );

        VarianteVideojuego varianteActualizada = varianteRepository.findById(variante.getId()).orElseThrow();
        assertThat(varianteActualizada.getStock()).isEqualTo(stockInicial + 5);
        assertThat(mov.getCantidadAjustada()).isEqualTo(5);
        assertThat(mov.getMotivo()).isEqualTo(TipoMovimientoInventario.ABASTECIMIENTO);
    }

    @Test
    @DisplayName("9. Regla OrdenService: No se puede cancelar una orden ya enviada")
    void noCancelarOrdenYaEnviada() {
        Orden ordenEnviada = ordenRepository.findAll().stream()
                .filter(o -> o.getEstado() == EstadoOrden.ENVIADO)
                .findFirst()
                .orElseThrow();

        assertThatThrownBy(() -> ordenService.cambiarEstado(ordenEnviada.getId(), EstadoOrden.CANCELADO))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No se puede cancelar una orden que ya ha sido enviada");
    }

    @Test
    @DisplayName("10. Regla OrdenService: No se puede enviar una orden que no está pagada")
    void noEnviarOrdenSinPagoAprobado() {
        Orden ordenPendiente = ordenRepository.findAll().stream()
                .filter(o -> o.getEstado() == EstadoOrden.PENDIENTE)
                .findFirst()
                .orElseThrow();

        // Si el pago no está aprobado o es nulo
        ordenPendiente.getPago().setEstadoPago(EstadoPago.PENDIENTE);
        ordenRepository.save(ordenPendiente);

        assertThatThrownBy(() -> ordenService.cambiarEstado(ordenPendiente.getId(), EstadoOrden.ENVIADO))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("No se puede enviar una orden que no está pagada");
    }

    @Test
    @DisplayName("11. Dinero en BigDecimal y cálculo del total sumando renglones")
    void dineroEnBigDecimalYCalculoTotal() {
        Long ordenId = ordenRepository.findAll().get(0).getId();
        Orden orden = ordenService.buscarPorId(ordenId);
        assertThat(orden.getTotal()).isInstanceOf(BigDecimal.class);
        assertThat(orden.getTotal().compareTo(BigDecimal.ZERO)).isGreaterThan(0);

        BigDecimal sumaRenglones = orden.getItems().stream()
                .map(ItemOrden::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        assertThat(orden.getTotal().compareTo(sumaRenglones)).isEqualTo(0);
    }
}
