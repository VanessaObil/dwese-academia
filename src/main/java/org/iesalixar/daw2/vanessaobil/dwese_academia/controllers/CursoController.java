package org.iesalixar.daw2.vanessaobil.dwese_academia.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.vanessaobil.dwese_academia.entities.Curso;
import org.iesalixar.daw2.vanessaobil.dwese_academia.repositories.CursoRepository;
import org.iesalixar.daw2.vanessaobil.dwese_academia.repositories.ProfesorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Controlador que maneja las operaciones CRUD para la entidad `Curso`.
 */
@Controller
@RequestMapping("/cursos")
public class CursoController {

    @Autowired
    private MessageSource messageSource;

    private static final Logger logger = LoggerFactory.getLogger(CursoController.class);

    // DAO para gestionar las operaciones de los cursos en la base de datos
    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private ProfesorRepository profesorRepository;


    /**
     * Lista todos los cursos y los pasa como atributo al modelo.
     *
     * @param model Objeto del modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para renderizar la lista de cursos.
     */
    @GetMapping
    public String listCursos(Model model) {
        logger.info("Solicitando la lista de todos los cursos...");
        List<Curso> listCursos = cursoRepository.findAll();
        logger.info("Se han cargado {} cursos.", listCursos.size());
        model.addAttribute("listCursos", listCursos);
        return "curso"; 
    }

    /**
     * Muestra el formulario para crear un nuevo curso.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nuevo curso.");
        model.addAttribute("curso", new Curso()); // Crear un nuevo objeto Curso
        model.addAttribute("listProfesores", profesorRepository.findAll());

        return "curso-form";
    }

    /**
     * Muestra el formulario para editar un nuevo curso.
     *
     * @param id identificador del curso
     * @param model modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        logger.info("Mostrando formulario de edición para el curso con ID {}", id);
        Optional<Curso> curso = cursoRepository.findById(id);
        if (curso == null) {
            logger.warn("No se encontró el curso con ID {}", id);
        }
        model.addAttribute("curso", curso.get());
        model.addAttribute("listProfesores", profesorRepository.findAll());
        return "curso-form";
    }


    /**
     * Inserta un nuevo curso en la base de datos.
     *
     * @param curso              Objeto que contiene los datos del formulario.
     * @param result             Resultados de la validación del formulario.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @param locale             Idioma para los mensajes de validación.
     * @return Redirección a la lista de cursos.
     */
    @PostMapping("/insert")
    public String insertCurso(@Valid @ModelAttribute("curso") Curso curso, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Insertando nuevo curso con nombre {}", curso.getNombreCurso());
        if (result.hasErrors()) {
            return "curso-form";
        }
        cursoRepository.save(curso);
        logger.info("Curso {} insertado con éxito.", curso.getNombreCurso());
        return "redirect:/cursos";
    }

    /**
     * Actualiza un curso existente en la base de datos.
     *
     * @param curso              Objeto que contiene los datos del formulario.
     * @param result             Resultados de la validación del formulario.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @param locale             Idioma para los mensajes de validación.
     * @return Redirección a la lista de cursos.
     */
    @PostMapping("/update")
    public String updateCurso(@Valid @ModelAttribute("curso") Curso curso, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Actualizando curso con ID {}", curso.getId());
        if (result.hasErrors()) {
            return "curso-form";
        }
        cursoRepository.save(curso);
        logger.info("Curso con ID {} actualizado con éxito.", curso.getId());
        return "redirect:/cursos";
    }

    /**
     * Elimina un curso de la base de datos.
     *
     * @param id                 ID del curso a eliminar.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de cursos.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete")
    public String deleteCurso(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando curso con ID {}", id);
        try {
            Optional<Curso> cursoOptional = cursoRepository.findById(id);
            if (cursoOptional.isPresent()) {
                cursoRepository.deleteById(id);
                logger.info("Curso con ID {} eliminado con éxito.", id);
            } else {
                logger.warn("No se encontró el curso con ID {}", id);
                redirectAttributes.addFlashAttribute("errorMessage", "Curso no encontrado.");
            }
        } catch (Exception e) {
            logger.error("Error al eliminar el curso con ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el curso.");
        }
        return "redirect:/cursos";
    }


}
