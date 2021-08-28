package br.com.felipelima.api.expressfood.repository

import br.com.felipelima.api.expressfood.domain.model.Cozinha
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CozinhaRepository extends JpaRepository<Cozinha, Long> {
}
