package br.com.alanms.alurafood.pagamentos.controller;

import br.com.alanms.alurafood.pagamentos.dto.PagamentoDTO;
import br.com.alanms.alurafood.pagamentos.model.enumeration.Status;
import br.com.alanms.alurafood.pagamentos.service.PagamentoService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @GetMapping
    public Page<PagamentoDTO> getAll(@PageableDefault(size = 10) Pageable  pageable) {
        return pagamentoService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoDTO> getOne(@PathVariable @NotNull Long id) {
        return ResponseEntity.ok(pagamentoService.getOne(id));
    }

    @PostMapping
    public ResponseEntity<PagamentoDTO> save(@RequestBody @Valid PagamentoDTO pagamentoDTO, UriComponentsBuilder uriBuilder) {
        PagamentoDTO pagamento = pagamentoService.save(pagamentoDTO);
        URI addressUri = uriBuilder.path("/pagamentos/{id}").buildAndExpand(pagamento.getId()).toUri();
        return ResponseEntity.created(addressUri).body(pagamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagamentoDTO> update(@PathVariable @NotNull Long id, @RequestBody @Valid PagamentoDTO pagamentoDTO) {
        return ResponseEntity.ok(pagamentoService.update(id, pagamentoDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PagamentoDTO> delete(@PathVariable @NotNull Long id) {
        pagamentoService.delete(id);
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/confirmar")
    @CircuitBreaker(name = "atualizaPedido", fallbackMethod = "pagamentoAutorizadoComIntegracaoPendente")
    public void confirmarPagamento(@PathVariable @NotNull Long id) {
        pagamentoService.confirmarPagamento(id);
    }

    public void pagamentoAutorizadoComIntegracaoPendente(Long id, Exception e) {
        pagamentoService.alteraStatus(id, Status.CONFIRMADO_SEM_INTEGRACAO);
    }
}
