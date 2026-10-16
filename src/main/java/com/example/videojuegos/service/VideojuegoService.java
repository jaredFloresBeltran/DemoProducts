package com.example.videojuegos.service;

import com.example.videojuegos.model.Videojuego;
import com.example.videojuegos.repository.VideojuegoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VideojuegoService {

    @Autowired
    private VideojuegoRepository videojuegoRepository;

    public List<Videojuego> getAllVideojuegos() {
        return videojuegoRepository.findAll();
    }

    public Optional<Videojuego> getVideojuegoById(Long id) {
        return videojuegoRepository.findById(id);
    }

    public Videojuego saveVideojuego(Videojuego videojuego) {
        return videojuegoRepository.save(videojuego);
    }

    public Videojuego updateVideojuego(Long id, Videojuego videojuegoDetails) {
        Optional<Videojuego> optionalVideojuego = videojuegoRepository.findById(id);
        if (optionalVideojuego.isPresent()) {
            Videojuego videojuego = optionalVideojuego.get();
            videojuego.setTitulo(videojuegoDetails.getTitulo());
            videojuego.setGenero(videojuegoDetails.getGenero());
            videojuego.setPrecio(videojuegoDetails.getPrecio());
            return videojuegoRepository.save(videojuego);
        }
        return null;
    }

    public boolean deleteVideojuego(Long id) {
        if (videojuegoRepository.existsById(id)) {
            videojuegoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}