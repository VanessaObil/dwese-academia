package org.iesalixar.daw2.vanessaobil.dwese_academia.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "profesores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profesor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @NotEmpty(message = "El apellido no puede estar vacío")
    @Size(max = 100, message = "El apellido no puede exceder 100 caracteres")
    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    @NotEmpty(message = "El correo no puede estar vacío")
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    // Relación uno a muchos con Cursos
    @OneToMany(mappedBy = "profesor", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Curso> cursos;

    // Constructor personalizado
    public Profesor(String nombre, String apellido, String email) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
    }
}

