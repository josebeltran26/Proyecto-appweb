package com.gamedepot.admin.service;

import com.gamedepot.admin.model.EstadoOrden;
import com.gamedepot.admin.model.Orden;
import com.gamedepot.admin.model.VarianteVideojuego;
import com.gamedepot.admin.repository.OrdenRepository;
import com.gamedepot.admin.repository.UsuarioRepository;
import com.gamedepot.admin.repository.VarianteVideojuegoRepository;
import com.gamedepot.admin.repository.VideojuegoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final VideojuegoRepository videojuegoRepository;
    private final VarianteVideojuegoRepository varianteRepository;
    private final OrdenRepository ordenRepository;
    private final UsuarioRepository usuarioRepository;

    public DashboardService(VideojuegoRepository videojuegoRepository,
                            VarianteVideojuegoRepository varianteRepository,
                            OrdenRepository ordenRepository,
                            UsuarioRepository usuarioRepository) {
        this.videojuegoRepository = videojuegoRepository;
        this.varianteRepository = varianteRepository;
        this.ordenRepository = ordenRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Map<String, Object> obtenerEstadisticas() {
        Map<String, Object> stats = new HashMap<>();

        long ordenesPendientes = ordenRepository.countByEstado(EstadoOrden.PENDIENTE);
        long stockBajoCount = varianteRepository.countLowStock();
        long totalVideojuegos = videojuegoRepository.count();
        long totalVariantes = varianteRepository.count();
        long totalUsuarios = usuarioRepository.count();
        BigDecimal totalVentas = ordenRepository.sumTotalProcessed();
        List<Orden> ultimasOrdenes = ordenRepository.findTop5ByOrderByFechaDesc();
        List<VarianteVideojuego> listaStockBajo = varianteRepository.findLowStock();

        stats.put("ordenesPendientes", ordenesPendientes);
        stats.put("stockBajoCount", stockBajoCount);
        stats.put("totalVideojuegos", totalVideojuegos);
        stats.put("totalVariantes", totalVariantes);
        stats.put("totalUsuarios", totalUsuarios);
        stats.put("totalVentas", totalVentas != null ? totalVentas : BigDecimal.ZERO);
        stats.put("ultimasOrdenes", ultimasOrdenes);
        stats.put("listaStockBajo", listaStockBajo);

        return stats;
    }
}
