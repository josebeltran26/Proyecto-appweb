package com.gamedepot.admin.controller;

import com.gamedepot.admin.model.Categoria;
import com.gamedepot.admin.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", categoriaService.listarTodas());
        if (!model.containsAttribute("categoria")) {
            model.addAttribute("categoria", new Categoria());
        }
        model.addAttribute("activePage", "categorias");
        return "categorias/lista";
    }

    @GetMapping("/{id}/editar")
    public String editarForm(@PathVariable Long id, Model model) {
        model.addAttribute("categoria", categoriaService.buscarPorId(id));
        model.addAttribute("activePage", "categorias");
        return "categorias/form";
    }

    @PostMapping
    public String guardar(@Valid @ModelAttribute("categoria") Categoria categoria,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (bindingResult.hasErrors()) {
            if (categoria.getId() != null) {
                model.addAttribute("activePage", "categorias");
                return "categorias/form";
            }
            redirectAttributes.addFlashAttribute("error", "Error en los datos de la categoría: Verifique el nombre.");
            return "redirect:/admin/categorias";
        }

        try {
            categoriaService.guardar(categoria);
            redirectAttributes.addFlashAttribute("success", "Categoría '" + categoria.getNombre() + "' guardada exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/categorias";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoriaService.eliminar(id);
            redirectAttributes.addFlashAttribute("success", "Categoría eliminada exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/categorias";
    }
}
