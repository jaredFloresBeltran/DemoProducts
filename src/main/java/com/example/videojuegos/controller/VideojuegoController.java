package com.example.videojuegos.controller;

import com.example.videojuegos.model.Videojuego;
import com.example.videojuegos.service.VideojuegoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/videojuegos")
public class VideojuegoController {

    @Autowired
    private VideojuegoService videojuegoService;

    @GetMapping
    public List<Videojuego> getAllVideojuegos() {
        return videojuegoService.getAllVideojuegos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Videojuego> getVideojuegoById(@PathVariable Long id) {
        Optional<Videojuego> videojuego = videojuegoService.getVideojuegoById(id);
        return videojuego.map(ResponseEntity::ok)
                         .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Videojuego> createVideojuego(@RequestBody Videojuego videojuego) {
        Videojuego savedVideojuego = videojuegoService.saveVideojuego(videojuego);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVideojuego);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Videojuego> updateVideojuego(@PathVariable Long id, @RequestBody Videojuego videojuegoDetails) {
        Videojuego updatedVideojuego = videojuegoService.updateVideojuego(id, videojuegoDetails);
        if (updatedVideojuego != null) {
            return ResponseEntity.ok(updatedVideojuego);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideojuego(@PathVariable Long id) {
        if (videojuegoService.deleteVideojuego(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}