package com.geospatial.sccon.controller;

import com.geospatial.sccon.entity.Pessoa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.geospatial.sccon.service.PessoaService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/geospatial")
public class PessoaController {
    @Autowired
    private PessoaService pessoaService;

    @GetMapping("/{id}")
    public Pessoa getPessoaById(@PathVariable int id) {
        return pessoaService.getPessoaById(id);
    }

    @GetMapping("/person")
    public List<Pessoa> getPessoasOrdenadas() {
        return pessoaService.getAllPessoas();
    }

    @PostMapping
    public void addPessoa(@RequestBody Pessoa pessoa) {
        pessoaService.addPessoa(pessoa);
    }

    @DeleteMapping("/person/{id}")
    public void removePessoa(@PathVariable int id) {
        pessoaService.removePessoa(id);
    }

    @PutMapping("/person/{id}")
    public void updatePessoa(@PathVariable int id, @RequestBody Pessoa pessoa) {
        pessoaService.updatePessoa(id, pessoa);
    }

    @PatchMapping("/person/{id}")
    public void updatePessoaParcial(@PathVariable int id, @RequestBody Map<String, Object> updates) {
        pessoaService.updatePessoaParcial(id, updates);
    }

    @GetMapping("/person/{id}/age")
    public int getPessoaAgeById(@PathVariable int id, @RequestParam String output) {
        return pessoaService.getPessoaIdadeById(id, output);
    }

    @GetMapping("/person/{id}/salary")
    public BigDecimal getPessoaSalaryById(@PathVariable int id, @RequestParam String output) {
        return pessoaService.getPessoaSalaryById(id, output);
    }
}
