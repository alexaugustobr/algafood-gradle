package br.com.felipelima.api.expressfood.api.controller

import br.com.felipelima.api.expressfood.domain.model.Restaurante
import br.com.felipelima.api.expressfood.domain.service.RestauranteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = "/restaurantes", produces = [ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE ])
class RestauranteController {

    @Autowired
    RestauranteService restauranteService

    @GetMapping
    ResponseEntity<Restaurante> findAll(){
        List<Restaurante> restaurantes = restauranteService.findAll()
        return ResponseEntity.ok(restaurantes) as ResponseEntity<Restaurante>
    }

    @GetMapping("/{id}")
    ResponseEntity<Restaurante> get(@PathVariable Long id){
        Restaurante restaurante = restauranteService.get(id)
        return ResponseEntity.ok(restaurante)
    }

    @PostMapping
    ResponseEntity<Restaurante> create(@RequestBody Restaurante restaurante){
        Restaurante restauranteAdded = restauranteService.create(restaurante)
        return ResponseEntity.status(HttpStatus.CREATED).body(restauranteAdded)
    }

    @PutMapping("/{id}")
    ResponseEntity<Restaurante> update(@RequestBody Restaurante restaurante, @PathVariable Long id){
        Restaurante restauranteUpdated = restauranteService.update(restaurante, id)
        return ResponseEntity.ok(restauranteUpdated)
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Restaurante> remove(@PathVariable Long id){
        try {
            Restaurante restauranteDeleted = restauranteService.remove(id)
            return ResponseEntity.noContent().build()
        } catch(DataIntegrityViolationException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).build()
        }
    }

}
