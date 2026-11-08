package com.gamedepot.admin.controller;

import com.gamedepot.admin.model.EstadoOrden;
import com.gamedepot.admin.model.Orden;
import com.gamedepot.admin.service.OrdenService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin/ordenes")
public class OrdenController {

    private final OrdenService ordenService;

    public OrdenController(OrdenService ordenService) {
        this.ordenService = ordenService;
    }

    @GetMapping
    public String listar(@RequestParam(value = "estado", required = false) EstadoOrden estado, Model model) {
        if (estado != null) {
            model.addAttribute("ordenes", ordenService.listarPorEstado(estado));
            model.addAttribute("estadoSeleccionado", estado);
        } else {
            model.addAttribute("ordenes", ordenService.listarTodas());
            model.addAttribute("estadoSeleccionado", null);
        }
        model.addAttribute("estados", EstadoOrden.values());
        model.addAttribute("activePage", "ordenes");
        return "ordenes/lista";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        Orden orden = ordenService.buscarPorId(id);
        model.addAttribute("orden", orden);
        model.addAttribute("estados", EstadoOrden.values());
        model.addAttribute("activePage", "ordenes");
        return "ordenes/detalle";
    }

    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable Long id,
                                @RequestParam("estado") EstadoOrden estado,
                                RedirectAttributes redirectAttributes) {
        try {
            ordenService.cambiarEstado(id, estado);
            redirectAttributes.addFlashAttribute("success", "El estado de la orden #" + id + " cambió a " + estado + ".");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/ordenes/" + id;
    }

    @PostMapping("/{id}/envio")
    public String actualizarEnvio(@PathVariable Long id,
                                  @RequestParam("paqueteria") String paqueteria,
                                  @RequestParam("numeroGuia") String numeroGuia,
                                  @RequestParam(value = "fechaEstimada", required = false)
                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaEstimada,
                                  RedirectAttributes redirectAttributes) {
        try {
            if (fechaEstimada == null) {
                fechaEstimada = LocalDateTime.now().plusDays(3);
            }
            ordenService.actualizarEnvio(id, paqueteria, numeroGuia, fechaEstimada);
            redirectAttributes.addFlashAttribute("success", "Información de guía y paquetería actualizada.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/ordenes/" + id;
    }
}
