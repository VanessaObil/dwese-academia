package org.iesalixar.daw2.vanessaobil.dwese_academia.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cursos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "El nombre del curso no puede estar vacío")
    @Size(max = 150, message = "El nombre del curso no puede exceder 150 caracteres")
    @Column(name = "nombre_curso", nullable = false, length = 150)
    private String nombreCurso;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    @Column(name = "descripcion", length = 500)
    private String descripcion;

    // Relación muchos a uno con Profesor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor_id", nullable = false)
    private Profesor profesor;

    // Constructor personalizado
    public Curso(String nombreCurso, String descripcion, Profesor profesor) {
        this.nombreCurso = nombreCurso;
        this.descripcion = descripcion;
        this.profesor = profesor;
    }
}
