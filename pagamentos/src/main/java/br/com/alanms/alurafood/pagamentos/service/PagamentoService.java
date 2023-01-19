package br.com.alanms.alurafood.pagamentos.service;

import br.com.alanms.alurafood.pagamentos.dto.PagamentoDTO;
import br.com.alanms.alurafood.pagamentos.http.PedidoClient;
import br.com.alanms.alurafood.pagamentos.model.Pagamento;
import br.com.alanms.alurafood.pagamentos.model.enumeration.Status;
import br.com.alanms.alurafood.pagamentos.repository.PagamentoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;

    private final ModelMapper modelMapper;

    private final PedidoClient pedidoClient;

    public PagamentoService(PagamentoRepository pagamentoRepository, ModelMapper modelMapper, PedidoClient pedidoClient) {
        this.pagamentoRepository = pagamentoRepository;
        this.modelMapper = modelMapper;
        this.pedidoClient = pedidoClient;
    }

    public PagamentoDTO save(PagamentoDTO pagamentoDTO) {
        Pagamento pagamento = modelMapper.map(pagamentoDTO, Pagamento.class);
        pagamento.setStatus(Status.CRIADO);
        pagamentoRepository.save(pagamento);
        return modelMapper.map(pagamento, PagamentoDTO.class);
    }

    public PagamentoDTO update(Long id, PagamentoDTO pagamentoDTO) {
        Pagamento pagamento = modelMapper.map(pagamentoDTO, Pagamento.class);
        pagamento.setId(id);
        pagamento = pagamentoRepository.save(pagamento);
        return modelMapper.map(pagamento, PagamentoDTO.class);
    }

    public Page<PagamentoDTO> getAll(Pageable pageable) {
        return pagamentoRepository.findAll(pageable)
                .map(p -> modelMapper.map(p, PagamentoDTO.class));
    }

    public PagamentoDTO getOne(Long id) {
        Pagamento pagamento = pagamentoRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        return modelMapper.map(pagamento, PagamentoDTO.class);
    }

    public void delete(Long id) {
        pagamentoRepository.deleteById(id);
    }

    public void confirmarPagamento(Long id) {
        Optional<Pagamento> pagamento = pagamentoRepository.findById(id);
        if (pagamento.isEmpty())
            throw new EntityNotFoundException();

        pagamento.get().setStatus(Status.CONFIRMADO);
        pagamentoRepository.save(pagamento.get());
        pedidoClient.atualizaPagamento(id);
    }

    public void alteraStatus(Long id, Status status) {
        Optional<Pagamento> pagamento = pagamentoRepository.findById(id);
        if (pagamento.isEmpty())
            throw new EntityNotFoundException();
        pagamento.get().setStatus(status);
        pagamentoRepository.save(pagamento.get());
    }
}
