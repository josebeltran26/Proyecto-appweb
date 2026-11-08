package com.gamedepot.admin.controller;

import com.gamedepot.admin.model.*;
import com.gamedepot.admin.service.CategoriaService;
import com.gamedepot.admin.service.VideojuegoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/videojuegos")
public class VideojuegoController {

    private final VideojuegoService videojuegoService;
    private final CategoriaService categoriaService;

    public VideojuegoController(VideojuegoService videojuegoService, CategoriaService categoriaService) {
        this.videojuegoService = videojuegoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("videojuegos", videojuegoService.listarTodos());
        model.addAttribute("plataformas", Plataforma.values());
        model.addAttribute("formatos", FormatoVideojuego.values());
        model.addAttribute("activePage", "videojuegos");
        return "videojuegos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevoForm(Model model) {
        if (!model.containsAttribute("videojuego")) {
            Videojuego v = new Videojuego();
            v.setActivo(true);
            v.setClasificacionEdad(ClasificacionEdad.E);
            model.addAttribute("videojuego", v);
        }
        model.addAttribute("todasCategorias", categoriaService.listarTodas());
        model.addAttribute("clasificaciones", ClasificacionEdad.values());
        model.addAttribute("plataformas", Plataforma.values());
        model.addAttribute("formatos", FormatoVideojuego.values());
        model.addAttribute("imageUrlsText", "");
        model.addAttribute("activePage", "videojuegos");
        return "videojuegos/form";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("videojuego") Videojuego videojuego,
                        BindingResult bindingResult,
                        @RequestParam(value = "categoriaIds", required = false) List<Long> categoriaIds,
                        @RequestParam(value = "imageUrlsText", required = false) String imageUrlsText,
                        // Datos para la primera variante inicial obligatoria
                        @RequestParam("variantePlataforma") Plataforma plataforma,
                        @RequestParam("varianteFormato") FormatoVideojuego formato,
                        @RequestParam("variantePrecio") BigDecimal precio,
                        @RequestParam("varianteStock") Integer stock,
                        @RequestParam("varianteStockMinimo") Integer stockMinimo,
                        @RequestParam(value = "varianteSku", required = false) String sku,
                        @RequestParam(value = "varianteKeys", required = false) String keysText,
                        RedirectAttributes redirectAttributes,
                        Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("todasCategorias", categoriaService.listarTodas());
            model.addAttribute("clasificaciones", ClasificacionEdad.values());
            model.addAttribute("plataformas", Plataforma.values());
            model.addAttribute("formatos", FormatoVideojuego.values());
            model.addAttribute("imageUrlsText", imageUrlsText);
            model.addAttribute("activePage", "videojuegos");
            return "videojuegos/form";
        }

        try {
            List<String> urls = parseUrls(imageUrlsText);
            Videojuego creado = videojuegoService.crear(videojuego, categoriaIds, urls);

            List<String> keysList = parseUrls(keysText);
            videojuegoService.agregarVariante(creado.getId(), plataforma, formato, precio, stock, stockMinimo, sku, keysList);

            redirectAttributes.addFlashAttribute("success", "Videojuego '" + videojuego.getNombre() + "' registrado con su edición inicial.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/videojuegos";
    }

    @GetMapping("/{id}/editar")
    public String editarForm(@PathVariable Long id, Model model) {
        Videojuego v = videojuegoService.buscarPorId(id);
        model.addAttribute("videojuego", v);
        model.addAttribute("todasCategorias", categoriaService.listarTodas());
        model.addAttribute("clasificaciones", ClasificacionEdad.values());
        model.addAttribute("plataformas", Plataforma.values());
        model.addAttribute("formatos", FormatoVideojuego.values());

        String urls = v.getImagenes().stream()
                .map(ImagenVideojuego::getUrl)
                .collect(Collectors.joining("\n"));
        model.addAttribute("imageUrlsText", urls);
        model.addAttribute("activePage", "videojuegos");
        return "videojuegos/form";
    }

    @PostMapping("/{id}/editar")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("videojuego") Videojuego videojuego,
                             BindingResult bindingResult,
                             @RequestParam(value = "categoriaIds", required = false) List<Long> categoriaIds,
                             @RequestParam(value = "imageUrlsText", required = false) String imageUrlsText,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (bindingResult.hasErrors()) {
            videojuego.setId(id);
            model.addAttribute("todasCategorias", categoriaService.listarTodas());
            model.addAttribute("clasificaciones", ClasificacionEdad.values());
            model.addAttribute("plataformas", Plataforma.values());
            model.addAttribute("formatos", FormatoVideojuego.values());
            model.addAttribute("imageUrlsText", imageUrlsText);
            model.addAttribute("activePage", "videojuegos");
            return "videojuegos/form";
        }

        try {
            List<String> urls = parseUrls(imageUrlsText);
            videojuegoService.actualizar(id, videojuego, categoriaIds, urls);
            redirectAttributes.addFlashAttribute("success", "Videojuego '" + videojuego.getNombre() + "' actualizado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/videojuegos";
    }

    @PostMapping("/{id}/variantes")
    public String agregarVariante(@PathVariable Long id,
                                  @RequestParam("plataforma") Plataforma plataforma,
                                  @RequestParam("formato") FormatoVideojuego formato,
                                  @RequestParam("precio") BigDecimal precio,
                                  @RequestParam("stock") Integer stock,
                                  @RequestParam(value = "stockMinimo", defaultValue = "5") Integer stockMinimo,
                                  @RequestParam(value = "sku", required = false) String sku,
                                  @RequestParam(value = "keysDigitales", required = false) String keysDigitales,
                                  RedirectAttributes redirectAttributes) {
        try {
            List<String> keysList = parseUrls(keysDigitales);
            videojuegoService.agregarVariante(id, plataforma, formato, precio, stock, stockMinimo, sku, keysList);
            redirectAttributes.addFlashAttribute("success", "Nueva edición / variante agregada con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/videojuegos";
    }

    @PostMapping("/variantes/{varianteId}/precio")
    public String cambiarPrecioVariante(@PathVariable Long varianteId,
                                        @RequestParam("precio") BigDecimal precio,
                                        RedirectAttributes redirectAttributes) {
        try {
            videojuegoService.actualizarPrecioVariante(varianteId, precio);
            redirectAttributes.addFlashAttribute("success", "Precio de la variante actualizado correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/videojuegos";
    }

    @PostMapping("/{id}/toggle-activo")
    public String toggleActivo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            videojuegoService.alternarActivo(id);
            redirectAttributes.addFlashAttribute("success", "Estado de publicación modificado.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/videojuegos";
    }

    @PostMapping("/variantes/{varianteId}/toggle-activo")
    public String toggleActivoVariante(@PathVariable Long varianteId, RedirectAttributes redirectAttributes) {
        try {
            videojuegoService.alternarActivoVariante(varianteId);
            redirectAttributes.addFlashAttribute("success", "Estado de la variante actualizado.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/videojuegos";
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
