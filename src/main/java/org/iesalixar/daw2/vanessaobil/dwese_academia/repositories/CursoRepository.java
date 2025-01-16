package org.iesalixar.daw2.vanessaobil.dwese_academia.repositories;

import org.iesalixar.daw2.vanessaobil.dwese_academia.entities.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface CursoRepository extends JpaRepository<Curso, Long> {



}
