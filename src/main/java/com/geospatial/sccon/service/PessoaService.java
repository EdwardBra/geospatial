package com.geospatial.sccon.service;

import com.geospatial.sccon.entity.Pessoa;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PessoaService {
    private Map<Integer, Pessoa> pessoaMap = new HashMap<>();
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final BigDecimal SALARIO_BASE = new BigDecimal("1558.00");
    private static final BigDecimal SALARIO_MINIMO = new BigDecimal("1302.00");

    @PostConstruct
    public void init() {
        // Populando o mapa com alguns dados ao iniciar a aplicação
        pessoaMap.put(1, new Pessoa(1, "João Silva", "1990-01-01", "2020-06-15"));
        pessoaMap.put(2, new Pessoa(2, "Maria Oliveira", "1985-02-20", "2018-03-10"));
        pessoaMap.put(3, new Pessoa(3, "Ana Pereira", "1995-03-30", "2019-04-22"));
    }

    public Pessoa getPessoaById(int id) {
        Pessoa pessoa = pessoaMap.get(id);
        if (pessoa == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa com este ID não encontrada.");
        }
        return pessoa;
    }

    public void addPessoa(Pessoa pessoa) {
        if (pessoa.getId() == 0) {
            int maxId = pessoaMap.keySet().stream().max(Integer::compareTo).orElse(0);
            pessoa.setId(maxId + 1);
        } else if (pessoaMap.containsKey(pessoa.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Pessoa com este ID já existe.");
        }
        pessoaMap.put(pessoa.getId(), pessoa);
    }
    public List<Pessoa> getAllPessoas() {
            List<Pessoa> pessoas = new ArrayList<>(pessoaMap.values());
            pessoas.sort(Comparator.comparing(Pessoa::getNome));
            return pessoas;
        }

    public void removePessoa(int id) {
        if (!pessoaMap.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa com este ID não encontrada.");
        }
        pessoaMap.remove(id);
    }

    public void updatePessoa(int id, Pessoa pessoaAtualizada) {
        if (!pessoaMap.containsKey(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa com este ID não encontrada.");
        }
        pessoaMap.put(id, pessoaAtualizada);
    }

    public void updatePessoaParcial(int id, Map<String, Object> updates) {
        Pessoa pessoa = pessoaMap.get(id);
        if (pessoa == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pessoa com este ID não encontrada.");
        }
        updates.forEach((key, value) -> {
            switch (key) {
                case "nome":
                    pessoa.setNome((String) value);
                    break;
                case "dataDeNascimento":
                    pessoa.setDataDeNascimento((String) value);
                    break;
                case "dataDeAdmissao":
                    pessoa.setDataDeAdmissao((String) value);
                    break;
                default:
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Atributo inválido: " + key);
            }
        });
        pessoaMap.put(id, pessoa);
    }

    public int calculaIdadeEmAnos(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public int calculaIdadeEmMeses(LocalDate birthDate) {
        Period period = Period.between(birthDate, LocalDate.now());
        return period.getYears() * 12 + period.getMonths();
    }

    public long calculaIdadeEmDias(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).toTotalMonths() * 30 + Period.between(birthDate, LocalDate.now()).getDays();
    }

    public int getPessoaIdadeById(int id, String outputFormat) {
        Pessoa pessoa = getPessoaById(id);
        LocalDate birthDate = LocalDate.parse(pessoa.getDataDeNascimento(), dateFormatter);

        switch (outputFormat.toLowerCase()) {
            case "years":
                return calculaIdadeEmAnos(birthDate);
            case "months":
                return calculaIdadeEmMeses(birthDate);
            case "days":
                return (int) calculaIdadeEmDias(birthDate);
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de saída inválido: " + outputFormat);
        }
    }

    public BigDecimal calculateSalary(Pessoa pessoa) {
        LocalDate dataAdmissao = LocalDate.parse(pessoa.getDataDeAdmissao(), dateFormatter);
        int anosNaEmpresa = Period.between(dataAdmissao, LocalDate.now()).getYears();

        BigDecimal salario = SALARIO_BASE;
        for (int i = 0; i < anosNaEmpresa; i++) {
            salario = salario.multiply(new BigDecimal("1.18")).add(new BigDecimal("500"));
        }

        return salario.setScale(2, RoundingMode.CEILING);
    }

    public BigDecimal getPessoaSalaryById(int id, String outputFormat) {
        Pessoa pessoa = getPessoaById(id);
        BigDecimal salario = calculateSalary(pessoa);

        switch (outputFormat.toLowerCase()) {
            case "full":
                return salario;
            case "min":
                return salario.divide(SALARIO_MINIMO, 2, RoundingMode.CEILING);
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato de saída inválido: " + outputFormat);
        }
    }

}
