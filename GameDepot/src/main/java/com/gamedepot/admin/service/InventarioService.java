package com.gamedepot.admin.service;

import com.gamedepot.admin.model.*;
import com.gamedepot.admin.repository.KeyLicenciaRepository;
import com.gamedepot.admin.repository.MovimientoInventarioRepository;
import com.gamedepot.admin.repository.VarianteVideojuegoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class InventarioService {

    private final MovimientoInventarioRepository movimientoRepository;
    private final VarianteVideojuegoRepository varianteRepository;
    private final KeyLicenciaRepository keyLicenciaRepository;

    public InventarioService(MovimientoInventarioRepository movimientoRepository,
                             VarianteVideojuegoRepository varianteRepository,
                             KeyLicenciaRepository keyLicenciaRepository) {
        this.movimientoRepository = movimientoRepository;
        this.varianteRepository = varianteRepository;
        this.keyLicenciaRepository = keyLicenciaRepository;
    }

    @Transactional(readOnly = true)
    public List<MovimientoInventario> listarHistorial() {
        return movimientoRepository.findAllWithVariante();
    }

    @Transactional(readOnly = true)
    public List<MovimientoInventario> listarHistorialPorVariante(Long varianteId) {
        return movimientoRepository.findByVarianteIdOrderByFechaDesc(varianteId);
    }

    @Transactional(readOnly = true)
    public List<VarianteVideojuego> listarVariantes() {
        return varianteRepository.findAllWithVideojuego();
    }

    @Transactional(readOnly = true)
    public List<VarianteVideojuego> obtenerStockBajo() {
        return varianteRepository.findLowStock();
    }

    @Transactional(readOnly = true)
    public long contarStockBajo() {
        return varianteRepository.countLowStock();
    }

    public MovimientoInventario registrarAjuste(Long varianteId,
                                               TipoMovimientoInventario tipo,
                                               Integer cantidad,
                                               String descripcion,
                                               List<String> nuevasKeys) {
        if (varianteId == null) {
            throw new IllegalArgumentException("Debes seleccionar una variante (juego + plataforma + formato)");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("Debes especificar el motivo o tipo de movimiento");
        }

        VarianteVideojuego variante = varianteRepository.findById(varianteId)
                .orElseThrow(() -> new IllegalArgumentException("Variante no encontrada con id: " + varianteId));

        int cantidadFinal = cantidad != null ? cantidad : 0;

        // Si es formato digital y se reciben keys nuevas para abastecer
        if (variante.isDigital() && nuevasKeys != null && !nuevasKeys.isEmpty()) {
            for (String keyStr : nuevasKeys) {
                if (keyStr != null && !keyStr.isBlank()) {
                    KeyLicencia k = new KeyLicencia(variante, keyStr.trim());
                    keyLicenciaRepository.save(k);
                }
            }
            cantidadFinal = nuevasKeys.size();
        }

        if (cantidadFinal == 0) {
            throw new IllegalArgumentException("La cantidad ajustada no puede ser cero ni vacía");
        }

        int delta;
        switch (tipo) {
            case ABASTECIMIENTO:
            case STOCK_INICIAL:
                delta = Math.abs(cantidadFinal);
                break;
            case MERMA:
            case VENTA:
                delta = -Math.abs(cantidadFinal);
                break;
            case CORRECCION:
            default:
                delta = cantidadFinal;
                break;
        }

        int stockActual = variante.getStock() != null ? variante.getStock() : 0;
        int nuevoStock = stockActual + delta;

        if (nuevoStock < 0) {
            throw new IllegalArgumentException("Ajuste denegado: El stock no puede quedar negativo. Stock actual: "
                    + stockActual + ", intento de cambio: " + delta + ", resultado: " + nuevoStock);
        }

        variante.setStock(nuevoStock);
        varianteRepository.save(variante);

        String desc = (descripcion != null && !descripcion.trim().isEmpty())
                ? descripcion.trim()
                : "Ajuste de inventario: " + tipo.name() + " para " + variante.getNombreCompleto();

        MovimientoInventario movimiento = new MovimientoInventario(
                null,
                variante,
                delta,
                tipo,
                desc,
                LocalDateTime.now()
        );

        return movimientoRepository.save(movimiento);
    }
}
