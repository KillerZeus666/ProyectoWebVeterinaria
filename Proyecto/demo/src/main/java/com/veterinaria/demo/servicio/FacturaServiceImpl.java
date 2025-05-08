package com.veterinaria.demo.servicio;

import com.veterinaria.demo.entidad.*;
import com.veterinaria.demo.repositorio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FacturaServiceImpl implements FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private TratamientoRepository tratamientoRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Override
    public void pagarFactura(Factura factura) {

        if (Boolean.FALSE.equals(factura.getPagada())) {
            factura.setPagada(true);
            facturaRepository.save(factura);
        }
    }


    @Override
    public Factura guardarFactura(Factura factura) {
        return facturaRepository.save(factura);
    }

    @Override
    public List<Factura> obtenerTodasFacturas() {
        return facturaRepository.findAll();
    }

    @Override
    public Factura obtenerFacturaPorID(Integer id) {
        return facturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));
    }

    @Override
    public void pagarFacturas(List<Integer> idsFacturas) {
        List<Factura> facturas = facturaRepository.findAllById(idsFacturas);

        for (Factura factura : facturas) {
            if(Boolean.FALSE.equals(factura.getPagada())) {
                factura.setPagada(true);
                facturaRepository.save(factura);
            }
        }

    }

    @Override
    public Factura crearFacturaPorTratamiento(Integer idCliente, Integer idTratamiento, Factura factura) {
        float total = 0f;
        float totalMedicamentos = 0f;

        Tratamiento tratamiento = tratamientoRepository.findById(idTratamiento)
                .orElseThrow(() -> new RuntimeException("Tratamiento no encontrado"));


        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Servicio servicio = tratamiento.getServicio();
        if (servicio != null) {
            total += servicio.getPrecioBase();
            factura.setServicio(servicio);
        }

        // Calcular total por medicamentos
        List<TratamientoMedicamento> listaTratMed = tratamiento.getTratamientoMedicamentos();
        List<FacturaMedicamento> facturaMedicamentos = new ArrayList<>();

        Factura facturanueva = new Factura();

        for (TratamientoMedicamento tm : listaTratMed) {
            Medicamento med = tm.getMedicamento();
            int cantidad = tm.getCantidad();
            float subtotal = med.getPrecioVenta() * cantidad;
            totalMedicamentos += subtotal;

            // Crear entrada en FacturaMedicamento
            FacturaMedicamento fm = new FacturaMedicamento();
            fm.setMedicamento(med);
            fm.setFactura(facturanueva);
            facturaMedicamentos.add(fm);
        }

        total += totalMedicamentos;

        facturanueva.setFechaHora(factura.getFechaHora());
        facturanueva.setTotal(total);
        facturanueva.setPagada(factura.getPagada());
        facturanueva.setMetododepago(factura.getMetododepago());
        facturanueva.setCliente(cliente);
        facturanueva.setTratamiento(tratamiento);
        facturanueva.setServicio(servicio);
        facturanueva.setFacturaMedicamentos(facturaMedicamentos);

        facturaRepository.save(facturanueva);

        return facturanueva;
    }


    @Override
    public Factura crearFacturaPorServicio(Integer idCliente, Integer idServicio, Factura factura) {

        float total = 0f;

        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Servicio servicio = servicioRepository.findById(idServicio)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        if (servicio != null) {
            total += servicio.getPrecioBase();
            factura.setServicio(servicio);
        }

        Factura facturanueva = new Factura();
        facturanueva.setFechaHora(factura.getFechaHora());
        facturanueva.setTotal(total);
        facturanueva.setPagada(factura.getPagada());
        facturanueva.setMetododepago(factura.getMetododepago());
        facturanueva.setCliente(cliente);
        facturanueva.setServicio(servicio);

        facturaRepository.save(facturanueva);

        return facturanueva;
    }

    @Override
    public Factura crearFacturaPorMedicamentos(Integer idCliente, List<Integer> idMedicamentos, Factura factura) {
        float total = 0f;

        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Factura facturanueva = new Factura();

        List<Medicamento> medicamentos = medicamentoRepository.findAllById(idMedicamentos);
        for (Medicamento medicamento : medicamentos) {
            if(medicamento.getUnidadesDisponibles() <= 0){
                throw new RuntimeException("No hay unidades disponibles del medicamento: " + medicamento.getNombre());
            }
        }

        List<FacturaMedicamento> facturaMedicamentos = new ArrayList<>();
        for (Medicamento medicamento : medicamentos) {
            medicamento.setUnidadesDisponibles(medicamento.getUnidadesDisponibles() - 1);
            medicamento.setUnidadesVendidas(medicamento.getUnidadesVendidas() + 1);
            medicamentoRepository.save(medicamento);

            FacturaMedicamento fm = new FacturaMedicamento();
            fm.setMedicamento(medicamento);
            fm.setFactura(facturanueva);
            facturaMedicamentos.add(fm);
            total += medicamento.getPrecioVenta();
        }

        facturanueva.setFechaHora(factura.getFechaHora());
        facturanueva.setTotal(total);
        facturanueva.setPagada(factura.getPagada());
        facturanueva.setMetododepago(factura.getMetododepago());
        facturanueva.setCliente(cliente);
        facturanueva.setFacturaMedicamentos(facturaMedicamentos);

        facturaRepository.save(facturanueva);

        return facturanueva;
    }
}
