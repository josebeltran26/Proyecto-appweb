package com.gamedepot.admin.config;

import com.gamedepot.admin.model.*;
import com.gamedepot.admin.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final DireccionRepository direccionRepository;
    private final CategoriaRepository categoriaRepository;
    private final VideojuegoRepository videojuegoRepository;
    private final VarianteVideojuegoRepository varianteRepository;
    private final KeyLicenciaRepository keyLicenciaRepository;
    private final MovimientoInventarioRepository movimientoRepository;
    private final OrdenRepository ordenRepository;
    private final EnvioRepository envioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository,
                           DireccionRepository direccionRepository,
                           CategoriaRepository categoriaRepository,
                           VideojuegoRepository videojuegoRepository,
                           VarianteVideojuegoRepository varianteRepository,
                           KeyLicenciaRepository keyLicenciaRepository,
                           MovimientoInventarioRepository movimientoRepository,
                           OrdenRepository ordenRepository,
                           EnvioRepository envioRepository,
                           PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.direccionRepository = direccionRepository;
        this.categoriaRepository = categoriaRepository;
        this.videojuegoRepository = videojuegoRepository;
        this.varianteRepository = varianteRepository;
        this.keyLicenciaRepository = keyLicenciaRepository;
        this.movimientoRepository = movimientoRepository;
        this.ordenRepository = ordenRepository;
        this.envioRepository = envioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // 1. Admin mock con BCrypt hash y teléfono
        Usuario admin = usuarioRepository.findByEmail("admin@gamedepot.com").orElseGet(() -> {
            Usuario nuevoAdmin = new Usuario(
                    null,
                    "Administrador GameDepot",
                    "admin@gamedepot.com",
                    passwordEncoder.encode("admin123"),
                    "6621234567",
                    RolUsuario.ADMIN,
                    LocalDateTime.now().minusMonths(6)
            );
            return usuarioRepository.save(nuevoAdmin);
        });

        // 2. Cliente mock con teléfono
        Usuario cliente = usuarioRepository.findByEmail("jesus.luzanilla@example.com").orElseGet(() -> {
            Usuario nuevoCliente = new Usuario(
                    null,
                    "Jesús Luzanilla",
                    "jesus.luzanilla@example.com",
                    passwordEncoder.encode("cliente123"),
                    "6629876543",
                    RolUsuario.CLIENTE,
                    LocalDateTime.now().minusMonths(2)
            );
            return usuarioRepository.save(nuevoCliente);
        });

        // 3. Dirección para el cliente
        Direccion direccionCliente = direccionRepository.findByUsuarioId(cliente.getId()).stream().findFirst().orElseGet(() -> {
            Direccion dir = new Direccion(
                    null,
                    cliente,
                    "Av. Tecnológico",
                    "450",
                    "B-12",
                    "Sahuaro",
                    "Hermosillo",
                    "Sonora",
                    "83170",
                    "Entre Mendoza y Solidaridad, portón azul",
                    "6629876543"
            );
            return direccionRepository.save(dir);
        });

        // 4. Categorías
        if (categoriaRepository.count() == 0) {
            Categoria catAccion = categoriaRepository.save(new Categoria(null, "Acción", "Combate rápido y reflejos"));
            Categoria catRPG = categoriaRepository.save(new Categoria(null, "RPG", "Rol y progresión de personaje"));
            Categoria catAventura = categoriaRepository.save(new Categoria(null, "Aventura", "Exploración y narrativa"));
            Categoria catEstrategia = categoriaRepository.save(new Categoria(null, "Estrategia", "Táctica y gestión"));

            // 5. Videojuego 1: Elden Ring
            Videojuego eldenRing = new Videojuego(
                    null,
                    "Elden Ring: Shadow of the Erdtree",
                    "Levántate, tiznado, y déjate guiar por la gracia para esgrimir el poder del Anillo de Elden.",
                    "FromSoftware",
                    ClasificacionEdad.M,
                    true,
                    List.of(catAccion, catRPG),
                    List.of(new ImagenVideojuego("https://images.unsplash.com/photo-1542751371-adc38448a05e?w=800&auto=format&fit=crop&q=80"))
            );
            eldenRing = videojuegoRepository.save(eldenRing);

            // Variantes Elden Ring (Plataforma + Formato con propio precio y stock)
            VarianteVideojuego erPcDigital = varianteRepository.save(new VarianteVideojuego(
                    null, eldenRing, Plataforma.PC, FormatoVideojuego.DIGITAL,
                    new BigDecimal("1199.00"), 3, 5, "ELDEN-PC-DIG", true));
            keyLicenciaRepository.save(new KeyLicencia(erPcDigital, "ER-PC-DIG-AAAA-1111"));
            keyLicenciaRepository.save(new KeyLicencia(erPcDigital, "ER-PC-DIG-BBBB-2222"));
            keyLicenciaRepository.save(new KeyLicencia(erPcDigital, "ER-PC-DIG-CCCC-3333"));
            movimientoRepository.save(new MovimientoInventario(null, erPcDigital, 3, TipoMovimientoInventario.STOCK_INICIAL, "Stock inicial digital", LocalDateTime.now().minusDays(20)));

            VarianteVideojuego erPs5Fisico = varianteRepository.save(new VarianteVideojuego(
                    null, eldenRing, Plataforma.PLAYSTATION_5, FormatoVideojuego.FISICO,
                    new BigDecimal("1299.00"), 15, 5, "ELDEN-PS5-FIS", true));
            movimientoRepository.save(new MovimientoInventario(null, erPs5Fisico, 15, TipoMovimientoInventario.STOCK_INICIAL, "Stock inicial físico en almacén", LocalDateTime.now().minusDays(20)));

            // 6. Videojuego 2: Cyberpunk 2077
            Videojuego cyberpunk = new Videojuego(
                    null,
                    "Cyberpunk 2077: Phantom Liberty",
                    "Aventura de rol y acción en un mundo abierto ambientada en Night City.",
                    "CD Projekt RED",
                    ClasificacionEdad.M,
                    true,
                    List.of(catAccion, catRPG),
                    List.of(new ImagenVideojuego("https://images.unsplash.com/photo-1550745165-9bc0b252726f?w=800&auto=format&fit=crop&q=80"))
            );
            cyberpunk = videojuegoRepository.save(cyberpunk);

            VarianteVideojuego cpPcDigital = varianteRepository.save(new VarianteVideojuego(
                    null, cyberpunk, Plataforma.PC, FormatoVideojuego.DIGITAL,
                    new BigDecimal("799.00"), 2, 5, "CP2077-PC-DIG", true)); // STOCK BAJO
            keyLicenciaRepository.save(new KeyLicencia(cpPcDigital, "CP-PC-DIG-XXXX-8888"));
            keyLicenciaRepository.save(new KeyLicencia(cpPcDigital, "CP-PC-DIG-YYYY-9999"));
            movimientoRepository.save(new MovimientoInventario(null, cpPcDigital, 5, TipoMovimientoInventario.STOCK_INICIAL, "Stock inicial", LocalDateTime.now().minusDays(15)));
            movimientoRepository.save(new MovimientoInventario(null, cpPcDigital, -3, TipoMovimientoInventario.VENTA, "Ventas procesadas", LocalDateTime.now().minusDays(5)));

            VarianteVideojuego cpXboxFisico = varianteRepository.save(new VarianteVideojuego(
                    null, cyberpunk, Plataforma.XBOX_SERIES, FormatoVideojuego.FISICO,
                    new BigDecimal("899.00"), 8, 4, "CP2077-XBS-FIS", true));
            movimientoRepository.save(new MovimientoInventario(null, cpXboxFisico, 8, TipoMovimientoInventario.STOCK_INICIAL, "Stock físico", LocalDateTime.now().minusDays(15)));

            // 7. Videojuego 3: Hollow Knight
            Videojuego hollowKnight = new Videojuego(
                    null,
                    "Hollow Knight: Silksong",
                    "Explora un reino embrujado y descubre los secretos del reino de Pharloom.",
                    "Team Cherry",
                    ClasificacionEdad.E10,
                    true,
                    List.of(catAccion, catAventura),
                    List.of(new ImagenVideojuego("https://images.unsplash.com/photo-1511512578047-dfb367046420?w=800&auto=format&fit=crop&q=80"))
            );
            hollowKnight = videojuegoRepository.save(hollowKnight);

            VarianteVideojuego hkSwitchFisico = varianteRepository.save(new VarianteVideojuego(
                    null, hollowKnight, Plataforma.NINTENDO_SWITCH, FormatoVideojuego.FISICO,
                    new BigDecimal("449.00"), 2, 6, "HK-NSW-FIS", true)); // STOCK BAJO
            movimientoRepository.save(new MovimientoInventario(null, hkSwitchFisico, 2, TipoMovimientoInventario.STOCK_INICIAL, "Stock preventa cartucho", LocalDateTime.now().minusDays(10)));

            // 8. Videojuego 4: Red Dead Redemption 2
            Videojuego rdr2 = new Videojuego(
                    null,
                    "Red Dead Redemption 2",
                    "América, 1899. El ocaso del salvaje oeste ha comenzado.",
                    "Rockstar Games",
                    ClasificacionEdad.M,
                    true,
                    List.of(catAventura, catAccion),
                    List.of(new ImagenVideojuego("https://images.unsplash.com/photo-1579373903781-fd5c0c30c4cd?w=800&auto=format&fit=crop&q=80"))
            );
            rdr2 = videojuegoRepository.save(rdr2);

            VarianteVideojuego rdrPs4Fisico = varianteRepository.save(new VarianteVideojuego(
                    null, rdr2, Plataforma.PLAYSTATION_4, FormatoVideojuego.FISICO,
                    new BigDecimal("999.00"), 12, 4, "RDR2-PS4-FIS", true));
            movimientoRepository.save(new MovimientoInventario(null, rdrPs4Fisico, 12, TipoMovimientoInventario.STOCK_INICIAL, "Stock físico", LocalDateTime.now().minusDays(12)));

            // ==========================================
            // ÓRDENES MOCK CON REGLAS DE NEGOCIO
            // ==========================================

            // Orden 1: PENDIENTE (Digital, sin envío)
            Orden ord1 = new Orden(null, cliente, "ORD-2026-001", LocalDateTime.now().minusHours(3),
                    EstadoOrden.PENDIENTE, null, null);
            ItemOrden item1 = new ItemOrden(null, ord1, erPcDigital, 1, new BigDecimal("1199.00"), BigDecimal.ZERO);
            ord1.addItem(item1);
            Pago pago1 = new Pago(null, ord1, new BigDecimal("1199.00"), MetodoPago.TARJETA, EstadoPago.APROBADO, LocalDateTime.now().minusHours(3));
            ord1.setPago(pago1);
            ordenRepository.save(ord1);

            // Orden 2: PROCESANDO (Digital con key asignada)
            Orden ord2 = new Orden(null, cliente, "ORD-2026-002", LocalDateTime.now().minusDays(1),
                    EstadoOrden.PROCESANDO, null, null);
            ItemOrden item2 = new ItemOrden(null, ord2, cpPcDigital, 1, new BigDecimal("799.00"), BigDecimal.ZERO);
            ord2.addItem(item2);
            Pago pago2 = new Pago(null, ord2, new BigDecimal("799.00"), MetodoPago.PAYPAL, EstadoPago.APROBADO, LocalDateTime.now().minusDays(1));
            ord2.setPago(pago2);
            KeyLicencia keyAsignada = keyLicenciaRepository.save(new KeyLicencia(null, cpPcDigital, "CP-PC-DIG-PROCESADA-001", true, LocalDateTime.now().minusDays(1)));
            item2.setKeyAsignada(keyAsignada);
            keyAsignada.setItemOrden(item2);
            ordenRepository.save(ord2);

            // Orden 3: ENVIADO (Físico, requiere y tiene Envio con número de guía)
            Orden ord3 = new Orden(null, cliente, "ORD-2026-003", LocalDateTime.now().minusDays(2),
                    EstadoOrden.ENVIADO, direccionCliente, null);
            ItemOrden item3 = new ItemOrden(null, ord3, erPs5Fisico, 1, new BigDecimal("1299.00"), BigDecimal.ZERO);
            ord3.addItem(item3);
            Pago pago3 = new Pago(null, ord3, new BigDecimal("1299.00"), MetodoPago.TRANSFERENCIA, EstadoPago.APROBADO, LocalDateTime.now().minusDays(2));
            ord3.setPago(pago3);
            Envio envio3 = new Envio(null, ord3, direccionCliente, "DHL Express", "DHL-88492019",
                    LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(2), null, EstadoEnvio.EN_TRANSITO);
            ord3.setEnvio(envio3);
            ordenRepository.save(ord3);

            // Orden 4: ENTREGADO (Físico con entrega completada)
            Orden ord4 = new Orden(null, cliente, "ORD-2026-004", LocalDateTime.now().minusDays(4),
                    EstadoOrden.ENTREGADO, direccionCliente, null);
            ItemOrden item4 = new ItemOrden(null, ord4, hkSwitchFisico, 1, new BigDecimal("449.00"), BigDecimal.ZERO);
            ord4.addItem(item4);
            Pago pago4 = new Pago(null, ord4, new BigDecimal("449.00"), MetodoPago.TARJETA, EstadoPago.APROBADO, LocalDateTime.now().minusDays(4));
            ord4.setPago(pago4);
            Envio envio4 = new Envio(null, ord4, direccionCliente, "FedEx", "FDX-77382910",
                    LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1), EstadoEnvio.ENTREGADO);
            ord4.setEnvio(envio4);
            ordenRepository.save(ord4);

            // Orden 5: CANCELADO
            Orden ord5 = new Orden(null, cliente, "ORD-2026-005", LocalDateTime.now().minusDays(5),
                    EstadoOrden.CANCELADO, null, null);
            ItemOrden item5 = new ItemOrden(null, ord5, cpPcDigital, 1, new BigDecimal("799.00"), BigDecimal.ZERO);
            ord5.addItem(item5);
            Pago pago5 = new Pago(null, ord5, new BigDecimal("799.00"), MetodoPago.TARJETA, EstadoPago.RECHAZADO, LocalDateTime.now().minusDays(5));
            ord5.setPago(pago5);
            ordenRepository.save(ord5);
        }
    }
}
