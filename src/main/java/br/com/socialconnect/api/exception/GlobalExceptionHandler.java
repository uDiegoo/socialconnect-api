package br.com.socialconnect.api.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.List;
import java.util.Map;
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String TIPO_BASE = "https://socialconnect.api/errors/";
    private static final String CODIGO_ESTOQUE_NEGATIVO = "EstoqueNaoNegativo";

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ProblemDetail handleNaoEncontrado(RecursoNaoEncontradoException ex) {
        return problema(HttpStatus.NOT_FOUND, "nao-encontrado", "Recurso não encontrado", ex.getMessage());
    }

    @ExceptionHandler(ConflitoException.class)
    public ProblemDetail handleConflito(ConflitoException ex) {
        return problema(HttpStatus.CONFLICT, "conflito", "Conflito de dados", ex.getMessage());
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ProblemDetail handleRegraNegocio(RegraNegocioException ex) {
        return problema(HttpStatus.UNPROCESSABLE_ENTITY, "regra-negocio", "Regra de negócio violada", ex.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<Map<String, String>> erros = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> Map.of("field", erro.getField(), "message", String.valueOf(erro.getDefaultMessage())))
                .toList();

        boolean estoqueNegativo = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getCode)
                .anyMatch(CODIGO_ESTOQUE_NEGATIVO::equals);

        ProblemDetail problema = estoqueNegativo
                ? problema(HttpStatus.UNPROCESSABLE_ENTITY, "estoque-negativo", "Estoque não pode ser negativo",
                        "O estoque não pode ser negativo.")
                : problema(HttpStatus.BAD_REQUEST, "validacao", "Erro de validação",
                        "Um ou mais campos são inválidos.");
        problema.setProperty("errors", erros);

        return ResponseEntity.status(problema.getStatus()).body(problema);
    }

    private ProblemDetail problema(HttpStatus status, String tipo, String titulo, String detalhe) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(status, detalhe);
        problema.setType(URI.create(TIPO_BASE + tipo));
        problema.setTitle(titulo);
        return problema;
    }
}
