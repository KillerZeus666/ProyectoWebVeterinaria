package com.veterinaria.demo.servicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veterinaria.demo.entidad.Cliente;
import com.veterinaria.demo.entidad.Mascota;
import com.veterinaria.demo.repositorio.ClienteRepository;
import com.veterinaria.demo.repositorio.MascotaRepository;

@Service
public class MascotaServiceImpl implements MascotaService {

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public MascotaServiceImpl(MascotaRepository mascotaRepository) {
        this.mascotaRepository = mascotaRepository;
    }

    @Override
    public List<Mascota> obtenerTodasMascotas() {
        return mascotaRepository.findAll();
    }

    @Override
    public Mascota obtenerMascotaPorId(Integer id) {
        return mascotaRepository.findById(id).orElse(null);
    }

    @Override
    public Mascota crearMascota(Mascota mascota, String cedula) {
        Cliente cliente = clienteRepository.findByCedula(cedula);
        mascota.setCliente(cliente);
        return mascotaRepository.save(mascota);
    }

    @Override
    public Mascota actualizarMascota(Integer id, Mascota mascota) {
        Mascota mascotaActual = mascotaRepository.findById(id).orElse(null);
        mascotaActual.setNombre(mascota.getNombre());
        mascotaActual.setRaza(mascota.getRaza());
        mascotaActual.setEdad(mascota.getEdad());
        mascotaActual.setPeso(mascota.getPeso());
        mascotaActual.setEnfermedad(mascota.getEnfermedad());
        mascotaActual.setEstado(mascota.getEstado());
        mascotaActual.setFoto(mascota.getFoto());
        return mascotaRepository.save(mascotaActual);
    }
    @Override
    public void cambiarEstado(Integer id, Mascota mascota){
        Mascota mascotaActual = mascotaRepository.findById(id).orElse(null);
        if (mascotaActual != null) {
            mascotaActual.setEstado(mascota.getEstado());
            mascotaRepository.save(mascotaActual); 
        }
    }
    

    @Override
    public void eliminarMascota(Integer id) {
        mascotaRepository.deleteById(id);
    }

    @Override
    public List<Mascota> obtenerMascotasPorCliente(Integer idCliente) {
        return mascotaRepository.findByClienteIdCliente(idCliente);
    }
}