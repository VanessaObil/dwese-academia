package org.iesalixar.daw2.vanessaobil.dwese_academia.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.vanessaobil.dwese_academia.entities.Profesor;
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
 * Controlador que maneja las operaciones CRUD para la entidad `Profesor`.
 */
@Controller
@RequestMapping("/profesores")
public class ProfesorController {

    @Autowired
    private MessageSource messageSource;

    private static final Logger logger = LoggerFactory.getLogger(ProfesorController.class);

    // DAO para gestionar las operaciones de los profesores en la base de datos
    @Autowired
    private ProfesorRepository profesorRepository;


    /**
     * Lista todos los profesores y los pasa como atributo al modelo.
     *
     * @param model Objeto del modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para renderizar la lista de profesores.
     */
    @GetMapping
    public String listProfesores(Model model) {
        logger.info("Solicitando la lista de todos los profesores...");
        List<Profesor> listProfesores = profesorRepository.findAll();
        logger.info("Se han cargado {} profesores.", listProfesores.size());
        model.addAttribute("listProfesores", listProfesores);
        return "profesor";
    }

    /**
     * Muestra el formulario para crear un nuevo profesor.
     *
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/new")
    public String showNewForm(Model model) {
        logger.info("Mostrando formulario para nuevo profesor.");
        model.addAttribute("profesor", new Profesor());
        return "profesor-form";
    }

    /**
     * Formulario para editar a un profesor
     *
     * @param id identificador del profesor
     * @param model Modelo para pasar datos a la vista.
     * @return El nombre de la plantilla Thymeleaf para el formulario.
     */
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        logger.info("Mostrando formulario de edición para el profesor con ID {}", id);
        Optional<Profesor> profesor = profesorRepository.findById(id);
        if (profesor == null) {
            logger.warn("No se encontró el profesor con ID {}", id);
        }
        model.addAttribute("profesor", profesor.get());
        return "profesor-form";
    }


    /**
     * Inserta un nuevo profesor en la base de datos.
     *
     * @param profesor            Objeto que contiene los datos del formulario.
     * @param result              Resultados de la validación del formulario.
     * @param redirectAttributes  Atributos para mensajes flash de redirección.
     * @param locale              Idioma para los mensajes de validación.
     * @return Redirección a la lista de profesores.
     */
    @PostMapping("/insert")
    public String insertProfesor(@Valid @ModelAttribute("profesor") Profesor profesor, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Insertando nuevo profesor con nombre {}", profesor.getNombre());
        if (result.hasErrors()) {
            return "profesor-form";
        }
        profesorRepository.save(profesor);
        logger.info("Profesor {} insertado con éxito.", profesor.getNombre());
        return "redirect:/profesores";
    }

    /**
     * Actualiza un profesor existente en la base de datos.
     *
     * @param profesor            Objeto que contiene los datos del formulario.
     * @param result              Resultados de la validación del formulario.
     * @param redirectAttributes  Atributos para mensajes flash de redirección.
     * @param locale              Idioma para los mensajes de validación.
     * @return Redirección a la lista de profesores.
     */
    @PostMapping("/update")
    public String updateProfesor(@Valid @ModelAttribute("profesor") Profesor profesor, BindingResult result, RedirectAttributes redirectAttributes, Locale locale) {
        logger.info("Actualizando profesor con ID {}", profesor.getId());
        if (result.hasErrors()) {
            return "profesor-form";
        }
        profesorRepository.save(profesor);
        logger.info("Profesor con ID {} actualizado con éxito.", profesor.getId());
        return "redirect:/profesores";
    }

    /**
     * Elimina un profesor de la base de datos.
     *
     * @param id                 ID del profesor a eliminar.
     * @param redirectAttributes Atributos para mensajes flash de redirección.
     * @return Redirección a la lista de profesores.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/profesor/delete")
    public String deleteProfesor(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        logger.info("Eliminando profesor con ID {}", id);
        try {
            Optional<Profesor> profesorOptional = profesorRepository.findById(id);
            if (profesorOptional.isPresent()) {
                profesorRepository.deleteById(id);
                logger.info("Profesor con ID {} eliminado con éxito.", id);
            } else {
                logger.warn("No se encontró el profesor con ID {}", id);
                redirectAttributes.addFlashAttribute("errorMessage", "Profesor no encontrado.");
            }
        } catch (Exception e) {
            logger.error("Error al eliminar el profesor con ID {}: {}", id, e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el profesor.");
        }
        return "redirect:/profesores";
    }


}
