package br.com.felipelima.api.expressfood.domain.model

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@ToString(includePackage = false, includeNames = true)
@EqualsAndHashCode
@Embeddable
class Endereco {

    @Column(name = "endereco_cep")
    String cep

    @Column(name = "endereco_logradouro")
    String logradouro

    @Column(name = "endereco_numero")
    String numero

    @Column(name = "endereco_complemento")
    String complemento

    @Column(name = "endereco_bairro")
    String bairro

    @ManyToOne
    @JoinColumn(name = "endereco_cidade_id")
    Cidade cidade
}
