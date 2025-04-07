package com.veterinaria.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.veterinaria.demo.servicio.CargaMedicamentosService;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    @Autowired
    private CargaMedicamentosService cargaMedicamentosService;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        cargaMedicamentosService.cargarMedicamentosDesdeCSV();
        System.out.println("✅ Medicamentos cargados desde el CSV.");
    }
}
