package com.veterinaria.demo.entidad;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCliente;

    private String nombre;
    private String correo;
    private String celular;

    @Column(name = "cedula", unique = true)
    private String cedula;

    @Column(name = "nombreUsuario", unique = true)
    private String nombreUsuario;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Mascota> mascotas;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Testimonio> testimonios;

    @ManyToOne
    @JoinColumn(name = "idRegistro", nullable = false)
    private Registro registro;
}