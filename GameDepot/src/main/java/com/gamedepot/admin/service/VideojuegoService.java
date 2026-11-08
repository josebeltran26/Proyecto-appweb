package com.gamedepot.admin.service;

import com.gamedepot.admin.model.*;
import com.gamedepot.admin.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class VideojuegoService {

    private final VideojuegoRepository videojuegoRepository;
    private final VarianteVideojuegoRepository varianteRepository;
    private final CategoriaRepository categoriaRepository;
    private final MovimientoInventarioRepository movimientoRepository;
    private final KeyLicenciaRepository keyLicenciaRepository;

    public VideojuegoService(VideojuegoRepository videojuegoRepository,
            VarianteVideojuegoRepository varianteRepository,
            CategoriaRepository categoriaRepository,
            MovimientoInventarioRepository movimientoRepository,
            KeyLicenciaRepository keyLicenciaRepository) {
        this.videojuegoRepository = videojuegoRepository;
        this.varianteRepository = varianteRepository;
        this.categoriaRepository = categoriaRepository;
        this.movimientoRepository = movimientoRepository;
        this.keyLicenciaRepository = keyLicenciaRepository;
    }

    @Transactional(readOnly = true)
    public List<Videojuego> listarTodos() {
        return videojuegoRepository.findAllWithVariantes();
    }

    @Transactional(readOnly = true)
    public Videojuego buscarPorId(Long id) {
        return videojuegoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Videojuego no encontrado con id: " + id));
    }

    public Videojuego crear(Videojuego videojuego, List<Long> categoriaIds, List<String> imageUrls) {
        if (categoriaIds != null && !categoriaIds.isEmpty()) {
            List<Categoria> cats = categoriaRepository.findAllById(categoriaIds);
            videojuego.setCategorias(cats);
        }

        if (imageUrls != null) {
            for (String url : imageUrls) {
                if (url != null && !url.trim().isEmpty()) {
                    videojuego.addImagen(new ImagenVideojuego(url.trim()));
                }
            }
        }

        if (videojuego.getActivo() == null) {
            videojuego.setActivo(true);
        }
        if (videojuego.getClasificacionEdad() == null) {
            videojuego.setClasificacionEdad(ClasificacionEdad.E);
        }

        return videojuegoRepository.save(videojuego);
    }

    public Videojuego actualizar(Long id, Videojuego form, List<Long> categoriaIds, List<String> imageUrls) {
        Videojuego existente = buscarPorId(id);

        existente.setNombre(form.getNombre());
        existente.setDescripcion(form.getDescripcion());
        existente.setDesarrollador(form.getDesarrollador());
        existente.setClasificacionEdad(form.getClasificacionEdad());
        if (form.getActivo() != null) {
            existente.setActivo(form.getActivo());
        }

        // Categorías
        if (categoriaIds != null) {
            List<Categoria> cats = categoriaRepository.findAllById(categoriaIds);
            existente.setCategorias(cats);
        } else {
            existente.getCategorias().clear();
        }

        // Imágenes
        existente.getImagenes().clear();
        if (imageUrls != null) {
            for (String url : imageUrls) {
                if (url != null && !url.trim().isEmpty()) {
                    existente.addImagen(new ImagenVideojuego(url.trim()));
                }
            }
        }

        return videojuegoRepository.save(existente);
    }

    public void alternarActivo(Long id) {
        Videojuego juego = buscarPorId(id);
        juego.setActivo(!Boolean.TRUE.equals(juego.getActivo()));
        videojuegoRepository.save(juego);
    }

    // ==========================================
    // GESTIÓN DE VARIANTES (Juego + Plataforma + Formato)
    // ==========================================

    @Transactional(readOnly = true)
    public List<VarianteVideojuego> listarVariantes(Long videojuegoId) {
        return varianteRepository.findByVideojuegoId(videojuegoId);
    }

    @Transactional(readOnly = true)
    public List<VarianteVideojuego> listarTodasVariantes() {
        return varianteRepository.findAllWithVideojuego();
    }

    @Transactional(readOnly = true)
    public VarianteVideojuego buscarVariantePorId(Long id) {
        return varianteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Variante no encontrada con id: " + id));
    }

    public VarianteVideojuego agregarVariante(Long videojuegoId, Plataforma plataforma, FormatoVideojuego formato,
            BigDecimal precio, Integer stock, Integer stockMinimo, String sku,
            List<String> keysDigitales) {
        Videojuego juego = buscarPorId(videojuegoId);

        if (precio == null || precio.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio debe ser mayor o igual a 0");
        }

        int stockCalculado = stock != null ? stock : 0;
        if (formato == FormatoVideojuego.DIGITAL && keysDigitales != null && !keysDigitales.isEmpty()) {
            stockCalculado = keysDigitales.size();
        }

        if (sku == null || sku.isBlank()) {
            sku = (juego.getNombre().replaceAll("[^a-zA-Z0-9]", "").toUpperCase() + "-" +
                    plataforma.name().substring(0, Math.min(3, plataforma.name().length())) + "-" +
                    formato.name().substring(0, 3) + "-" + System.currentTimeMillis() % 10000);
        }

        VarianteVideojuego variante = new VarianteVideojuego(
                null,
                juego,
                plataforma,
                formato,
                precio,
                stockCalculado,
                stockMinimo != null ? stockMinimo : 5,
                sku,
                true);

        VarianteVideojuego guardada = varianteRepository.save(variante);

        // Si es digital, guardar las keys individuales
        if (formato == FormatoVideojuego.DIGITAL && keysDigitales != null) {
            for (String key : keysDigitales) {
                if (key != null && !key.isBlank()) {
                    KeyLicencia keyLicencia = new KeyLicencia(guardada, key.trim());
                    keyLicenciaRepository.save(keyLicencia);
                }
            }
        }

        // Registrar movimiento de stock inicial si stock > 0
        if (stockCalculado > 0) {
            MovimientoInventario movimiento = new MovimientoInventario(
                    null,
                    guardada,
                    stockCalculado,
                    TipoMovimientoInventario.STOCK_INICIAL,
                    "Stock inicial para " + guardada.getNombreCompleto(),
                    LocalDateTime.now());
            movimientoRepository.save(movimiento);
        }

        return guardada;
    }

    public void actualizarPrecioVariante(Long varianteId, BigDecimal nuevoPrecio) {
        if (nuevoPrecio == null || nuevoPrecio.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio debe ser mayor o igual a 0");
        }
        VarianteVideojuego variante = buscarVariantePorId(varianteId);
        variante.setPrecio(nuevoPrecio);
        varianteRepository.save(variante);
    }

    public void alternarActivoVariante(Long varianteId) {
        VarianteVideojuego variante = buscarVariantePorId(varianteId);
        variante.setActivo(!Boolean.TRUE.equals(variante.getActivo()));
        varianteRepository.save(variante);
    }
}
