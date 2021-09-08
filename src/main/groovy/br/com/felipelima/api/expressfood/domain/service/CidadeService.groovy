package br.com.felipelima.api.expressfood.domain.service

import br.com.felipelima.api.expressfood.domain.exception.EntidadeNotFoundException
import br.com.felipelima.api.expressfood.domain.model.Cidade
import br.com.felipelima.api.expressfood.domain.repository.CidadeRepository
import br.com.felipelima.api.expressfood.domain.repository.EstadoRepository
import org.springframework.beans.BeanUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

import javax.transaction.Transactional

@Service
class CidadeService {

    @Autowired
    CidadeRepository cidadeRepository

    @Autowired
    EstadoRepository estadoRepository

    List<Cidade> findAll() {
        return cidadeRepository.findAll()
    }

    Cidade findById(Long id){
        return cidadeRepository.findById(id).orElseThrow{
            new EntidadeNotFoundException(
                    HttpStatus.NOT_FOUND,
                    String.format("Cidade de código %d não encontrada.", id)
            )
        }
    }

    @Transactional
    Cidade create(Cidade cidade){
        def estadoId = cidade.estado.id
        def estadoExists = estadoRepository.findById(estadoId).orElseThrow{
            new EntidadeNotFoundException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Estado de código %d não encontrado.", estadoId)
            )
        }

        cidade.estado = estadoExists

        return cidadeRepository.save(cidade)
    }

    @Transactional
    Cidade update(Cidade cidade, Long id){
        def cidadeUpdated = findById(id)
        def estadoId = cidade.estado.id
        def estadoExists = estadoRepository.findById(estadoId).orElseThrow{
            new EntidadeNotFoundException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Estado de código %d não encontrado.", estadoId)
            )
        }

        BeanUtils.copyProperties(cidade, cidadeUpdated, "id")
        BeanUtils.copyProperties(estadoExists, cidadeUpdated.estado)

        return cidadeRepository.save(cidadeUpdated)
    }

    @Transactional
    Cidade remove(Long id){
        def cidadeRemoved = findById(id)

        return cidadeRepository.delete(cidadeRemoved)
    }

}
