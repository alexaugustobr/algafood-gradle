package br.com.felipelima.api.expressfood.controller

import br.com.felipelima.api.expressfood.api.exceptionHandler.ProblemExceptionType
import br.com.felipelima.api.expressfood.domain.model.Cozinha
import br.com.felipelima.api.expressfood.domain.model.Restaurante
import br.com.felipelima.api.expressfood.domain.service.CozinhaService
import br.com.felipelima.api.expressfood.domain.service.RestauranteService
import br.com.felipelima.api.expressfood.test.GeneralTest
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import static org.junit.jupiter.api.Assertions.*

class RestauranteControllerTest extends GeneralTest{

    @Autowired
    MockMvc mvc

    @Autowired
    ObjectMapper objectMapper

    @Autowired
    RestauranteService restauranteService

    @Autowired
    CozinhaService cozinhaService

    //TODO: Testar a criacao de um restaurante com sucesso
    @Test
    void restaurante_InserirComSucesso() {
        Cozinha cozinha = cozinhaService.get(1)
        def restaurante = new Restaurante(
                nome: "Casa do Sabor",
                taxaFrete: 2.99,
                cozinha: cozinha
        )

        def response = mvc.perform(
                post("/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(restaurante)))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isCreated())
        .andReturn().response

        Restaurante restauranteReturn = objectMapper.readerFor(Restaurante.class).readValue(response.contentAsString)
        assertEquals(restauranteReturn.nome, restaurante.nome)
        assertEquals(restauranteReturn.taxaFrete, restaurante.taxaFrete)
        assertEquals(restauranteReturn.cozinha.id, restaurante.cozinha.id)
        assertNotNull(restauranteReturn.id)
    }

    //TODO: Testar a criação e recuperação de um id
    @Test
    void restaurante_CriaERecuperaId() {
        Cozinha cozinha = cozinhaService.get(1)
        def restaurante = restauranteService.create(new Restaurante(nome: "Casa do Sabor", taxaFrete: 2.99, cozinha: cozinha))

        def response = mvc.perform(get("/restaurantes/${restaurante.id}"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andReturn().response

        Restaurante restauranteReturn = objectMapper.readerFor(Restaurante.class).readValue(response.contentAsString)

        assertEquals(restauranteReturn.nome, restaurante.nome)
        assertEquals(restauranteReturn.taxaFrete, restaurante.taxaFrete)
        assertEquals(restauranteReturn.cozinha.id, restaurante.cozinha.id)
        assertNotNull(restauranteReturn.id)
    }

    //TODO: Testar a recuperação de um restaurante inexistente
    @Test
    void restaurante_RecuperaIdInvalido() {
        def response = mvc.perform(get("/restaurantes/${Integer.MAX_VALUE}"))
            .andDo(MockMvcResultHandlers.print())
            .andExpectAll(
                status().isNotFound(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("title").value(ProblemExceptionType.RECURSO_NAO_ENCONTRADO.getTitle())
            )
            .andReturn().response
    }

    //TODO: Testar a exclusão de um restaurante inexistente
    @Test
    void restaurante_ExcluirInexistente() {
        def response = mvc.perform(delete("/restaurantes/${Integer.MAX_VALUE}"))
            .andDo(MockMvcResultHandlers.print())
            .andExpectAll(
                status().isNotFound(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("title").value(ProblemExceptionType.RECURSO_NAO_ENCONTRADO.getTitle())
            )
            .andReturn().response
    }

    //TODO: Testar restaurante com frete negativo
    @Test
    void restaurante_InserirFreteNegativo() {
        Cozinha cozinha = cozinhaService.get(1)
        def restaurante = new Restaurante(
            nome: "Casa do Sabor",
            taxaFrete: -5,
            cozinha: cozinha
        )

        def response = mvc.perform(
            post("/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(restaurante)))
            .andDo(MockMvcResultHandlers.print())
            .andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("title").value(ProblemExceptionType.DADOS_INVALIDOS.getTitle()),
                jsonPath("objects[*].name").value("taxaFrete")
            )
            .andReturn().response
    }

    //TODO: Testar restaurante com frete gratis sem descricao "Frete Grátis"
    @Test
    void restaurante_InserirFreteGratisSemDescricao() {
        Cozinha cozinha = cozinhaService.get(1)
        def restaurante = new Restaurante(
            nome: "Casa do Sabor",
            taxaFrete: 0,
            cozinha: cozinha
        )

        def response = mvc.perform(
            post("/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(restaurante)))
            .andDo(MockMvcResultHandlers.print())
            .andExpectAll(
                    status().isBadRequest(),
                    content().contentType(MediaType.APPLICATION_JSON),
                    jsonPath("title").value(ProblemExceptionType.DADOS_INVALIDOS.getTitle()),
                    jsonPath("objects[*].name").value("restaurante")
            )
            .andReturn().response
    }

    //TODO: Testar restaurante sem nome
    @Test
    void restaurante_InserirSemNome() {
        Cozinha cozinha = cozinhaService.get(1)
        def restaurante = new Restaurante(
            nome: "",
            taxaFrete: 1,
            cozinha: cozinha
        )

        def response = mvc.perform(
            post("/restaurantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(restaurante)))
            .andDo(MockMvcResultHandlers.print())
            .andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("title").value(ProblemExceptionType.DADOS_INVALIDOS.getTitle()),
                jsonPath("objects[*].name").value("nome")
            )
    }
}
