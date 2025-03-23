package com.inde.indytrack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.inde.indytrack.repository.MinorRepository;
import com.inde.indytrack.exception.MinorNotFoundException;
import com.inde.indytrack.model.Minor;

import java.util.List;

@RestController
@RequestMapping("/minors")
public class MinorController {

    @Autowired
    private MinorRepository repository;

    public MinorController(MinorRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Minor> retrieveAllMinors() {
        return repository.findAll();
    }

    @GetMapping("/{name}")
    public Minor retrieveMinorByName(@PathVariable String name) {
        return repository.findById(name)
            .orElseThrow(() -> new MinorNotFoundException(name));
    }

    @PostMapping
    public Minor createMinor(@RequestBody Minor minor) {
        if (repository.existsById(minor.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The " + minor.getName() + " minor already exists");
        }
        return repository.save(minor);
    }

    @PutMapping("/{name}")
    public Minor updateMinor(@PathVariable String name, @RequestBody Minor minor) {
        return repository.findById(name)
            .map(existingMinor -> {
                existingMinor.setName(name);
                existingMinor.setRequirements(minor.getRequirements());
                return repository.save(existingMinor);
            })
            .orElseThrow(() -> new MinorNotFoundException(name));
    }

    @DeleteMapping("/{name}")
    public void deleteMinor(@PathVariable String name) {
        Minor minor = repository.findById(name)
            .orElseThrow(() -> new MinorNotFoundException(name));
        repository.delete(minor);
    }
}
