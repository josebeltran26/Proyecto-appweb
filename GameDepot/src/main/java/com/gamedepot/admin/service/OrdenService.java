package com.gamedepot.admin.service;

import com.gamedepot.admin.model.*;
import com.gamedepot.admin.repository.EnvioRepository;
import com.gamedepot.admin.repository.KeyLicenciaRepository;
import com.gamedepot.admin.repository.OrdenRepository;
import com.gamedepot.admin.repository.VarianteVideojuegoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OrdenService {

    private final OrdenRepository ordenRepository;
    private final EnvioRepository envioRepository;
    private final KeyLicenciaRepository keyLicenciaRepository;
    private final VarianteVideojuegoRepository varianteRepository;

    public OrdenService(OrdenRepository ordenRepository,
                        EnvioRepository envioRepository,
                        KeyLicenciaRepository keyLicenciaRepository,
                        VarianteVideojuegoRepository varianteRepository) {
        this.ordenRepository = ordenRepository;
        this.envioRepository = envioRepository;
        this.keyLicenciaRepository = keyLicenciaRepository;
        this.varianteRepository = varianteRepository;
    }

    @Transactional(readOnly = true)
    public List<Orden> listarTodas() {
        return ordenRepository.findAllWithUsuario();
    }

    @Transactional(readOnly = true)
    public List<Orden> listarPorEstado(EstadoOrden estado) {
        return ordenRepository.findByEstadoOrderByFechaDesc(estado);
    }

    @Transactional(readOnly = true)
    public Orden buscarPorId(Long id) {
        return ordenRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada con id: " + id));
    }

    public void cambiarEstado(Long id, EstadoOrden nuevoEstado) {
        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado no puede ser nulo");
        }
        Orden orden = buscarPorId(id);
        EstadoOrden estadoActual = orden.getEstado();

        if (estadoActual == nuevoEstado) {
            return;
        }

        // 1. REGLA: No se puede cancelar una orden ya enviada o entregada
        if (nuevoEstado == EstadoOrden.CANCELADO) {
            if (estadoActual == EstadoOrden.ENVIADO || estadoActual == EstadoOrden.ENTREGADO) {
                throw new IllegalStateException("No se puede cancelar una orden que ya ha sido enviada o entregada.");
            }

            // Liberar stock y keys digitales reservadas
            for (ItemOrden item : orden.getItems()) {
                if (item.getVariante() != null) {
                    VarianteVideojuego v = item.getVariante();
                    v.setStock(v.getStock() + item.getCantidad());
                    varianteRepository.save(v);
                }

                if (item.getKeyAsignada() != null) {
                    KeyLicencia key = item.getKeyAsignada();
                    key.setAsignada(false);
                    key.setItemOrden(null);
                    key.setFechaAsignacion(null);
                    keyLicenciaRepository.save(key);
                }
            }

            orden.setEstado(EstadoOrden.CANCELADO);
            ordenRepository.save(orden);
            return;
        }

        // 2. REGLA: No se puede enviar una orden que no está pagada
        if (nuevoEstado == EstadoOrden.ENVIADO) {
            if (orden.getPago() == null || orden.getPago().getEstadoPago() != EstadoPago.APROBADO) {
                String estadoPagoStr = orden.getPago() != null ? orden.getPago().getEstadoPago().name() : "SIN_PAGO";
                throw new IllegalStateException("No se puede enviar una orden que no está pagada. Estado actual del pago: " + estadoPagoStr);
            }

            // Si es físico, actualizar estado de paquetería
            if (orden.getEnvio() != null) {
                orden.getEnvio().setEstadoEnvio(EstadoEnvio.EN_TRANSITO);
                if (orden.getEnvio().getFechaEnvio() == null) {
                    orden.getEnvio().setFechaEnvio(LocalDateTime.now());
                }
            }
        }

        // 3. Al pasar a ENTREGADO
        if (nuevoEstado == EstadoOrden.ENTREGADO) {
            if (orden.getEnvio() != null) {
                orden.getEnvio().setEstadoEnvio(EstadoEnvio.ENTREGADO);
                if (orden.getEnvio().getFechaEntregaReal() == null) {
                    orden.getEnvio().setFechaEntregaReal(LocalDateTime.now());
                }
            }
        }

        // 4. Si pasa a PROCESANDO o ENVIADO y tiene artículos digitales sin key asignada, asignar keys disponibles
        if (nuevoEstado == EstadoOrden.PROCESANDO || nuevoEstado == EstadoOrden.ENVIADO) {
            asignarKeysDigitales(orden);
        }

        orden.setEstado(nuevoEstado);
        ordenRepository.save(orden);
    }

    /**
     * Regla: "No se puede vender una key ya asignada".
     * Busca únicamente keys con asignada = false y las vincula al ItemOrden.
     */
    public void asignarKeysDigitales(Orden orden) {
        for (ItemOrden item : orden.getItems()) {
            if (item.getVariante() != null && item.getVariante().isDigital() && item.getKeyAsignada() == null) {
                for (int i = 0; i < item.getCantidad(); i++) {
                    KeyLicencia keyDisponible = keyLicenciaRepository
                            .findFirstByVarianteIdAndAsignadaFalse(item.getVariante().getId())
                            .orElseThrow(() -> new IllegalStateException(
                                    "No se puede vender: no hay licencias digitales disponibles (keys) para " +
                                    item.getVariante().getNombreCompleto()));

                    keyDisponible.setAsignada(true);
                    keyDisponible.setItemOrden(item);
                    keyDisponible.setFechaAsignacion(LocalDateTime.now());
                    keyLicenciaRepository.save(keyDisponible);
                    item.setKeyAsignada(keyDisponible);
                }
            }
        }
    }

    public void actualizarEnvio(Long ordenId, String paqueteria, String numeroGuia, LocalDateTime fechaEstimada) {
        Orden orden = buscarPorId(ordenId);
        if (!orden.requiereEnvio()) {
            throw new IllegalStateException("Esta orden contiene únicamente artículos digitales y no requiere envío físico.");
        }

        Envio envio = orden.getEnvio();
        if (envio == null) {
            envio = new Envio(null, orden, orden.getDireccionEntrega(), paqueteria, numeroGuia,
                    LocalDateTime.now(), fechaEstimada, null, EstadoEnvio.EN_TRANSITO);
            orden.setEnvio(envio);
        } else {
            envio.setPaqueteria(paqueteria);
            envio.setNumeroGuia(numeroGuia);
            envio.setFechaEntregaEstimada(fechaEstimada);
            envio.setEstadoEnvio(EstadoEnvio.EN_TRANSITO);
        }
        envioRepository.save(envio);
    }
}
