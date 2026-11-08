package com.gamedepot.admin.controller;

import com.gamedepot.admin.model.TipoMovimientoInventario;
import com.gamedepot.admin.service.InventarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("variantes", inventarioService.listarVariantes());
        model.addAttribute("movimientos", inventarioService.listarHistorial());
        model.addAttribute("motivos", TipoMovimientoInventario.values());
        model.addAttribute("activePage", "inventario");
        return "inventario/index";
    }

    @PostMapping("/ajustar")
    public String registrarAjuste(@RequestParam("varianteId") Long varianteId,
                                  @RequestParam("motivo") TipoMovimientoInventario motivo,
                                  @RequestParam(value = "cantidad", required = false, defaultValue = "1") Integer cantidad,
                                  @RequestParam(value = "descripcion", required = false) String descripcion,
                                  @RequestParam(value = "nuevasKeysText", required = false) String nuevasKeysText,
                                  RedirectAttributes redirectAttributes) {
        try {
            List<String> keys = parseUrls(nuevasKeysText);
            inventarioService.registrarAjuste(varianteId, motivo, cantidad, descripcion, keys);
            redirectAttributes.addFlashAttribute("success", "Ajuste de existencias registrado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/inventario";
    }

    private List<String> parseUrls(String text) {
        if (text == null || text.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(text.split("[\\r\\n,]+"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}
