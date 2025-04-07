package com.veterinaria.demo.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.veterinaria.demo.ErrorHeading.NotFoundException;
import com.veterinaria.demo.entidad.Cliente;
import com.veterinaria.demo.servicio.ClienteService;

/*Documentacion
 * http://localhost:8082/swagger-ui/index.html#/mascota-controller/detalleMascota
 */


/**
 * Controlador REST para gestionar las operaciones relacionadas con los clientes.
 * Todos los métodos devuelven datos en formato JSON.
 *
 * URL base: http://localhost:8082/cliente
 */
@RestController
@RequestMapping("/cliente")
@CrossOrigin(origins = "http://localhost:4200")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    /**
     * Obtiene la lista de todos los clientes registrados.
     * 
     * @return Lista de objetos Cliente en formato JSON.
     * 
     * Ejemplo de uso:
     * GET http://localhost:8082/cliente
     */
    @GetMapping
    public List<Cliente> listar() {
        return clienteService.obtenerTodosClientes();
    }

    /**
     * Obtiene el detalle de un cliente específico a partir de su ID.
     *
     * @param id ID del cliente a consultar.
     * @return Cliente correspondiente al ID si existe; error 404 si no se encuentra.
     * 
     * Ejemplo de uso:
     * GET http://localhost:8082/cliente/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> detalleCliente(@PathVariable Integer id) {
        Cliente cliente = clienteService.obtenerClientePorId(id);
        if (cliente != null) {
            return ResponseEntity.ok(cliente);
        } else {
            throw new NotFoundException(id, "El cliente con ID " + id + " no existe.");
        }
    }

    /**
     * Crea un nuevo cliente.
     *
     * @param cliente Objeto Cliente recibido en el cuerpo de la solicitud.
     * @param confirmPassword Confirmación de contraseña enviada como parámetro.
     * @return Mensaje de éxito o error si las contraseñas no coinciden.
     * 
     * Ejemplo de uso:
     * POST http://localhost:8082/cliente/crear?confirm_password=123
     * Body (JSON):
     * {
     *   "nombre": "Juan",
     *   "correo": "juan@mail.com",
     *   "contrasena": "123"
     * }
     */
    @PostMapping("/crear")
    public void crearCliente(@RequestBody Cliente cliente) {
        clienteService.crearCliente(cliente);
    }

    

    /**
     * Actualiza la información de un cliente existente.
     *
     * @param id ID del cliente a actualizar.
     * @param cliente Objeto Cliente con los nuevos datos.
     * @return Mensaje de éxito tras la actualización.
     * 
     * Ejemplo de uso:
     * PUT http://localhost:8082/cliente/actualizar/1
     */
    @PutMapping("/actualizar/{id}")
    public void actualizarCliente(@PathVariable Integer id, @RequestBody Cliente cliente) {
        clienteService.editarCliente(id, cliente);
    }

    /**
     * Elimina un cliente por su ID.
     *
     * @param id ID del cliente a eliminar.
     * @return Mensaje indicando que el cliente fue eliminado.
     * 
     * Ejemplo de uso:
     * DELETE http://localhost:8082/cliente/eliminar/1
     */
    @DeleteMapping("/eliminar/{id}")
    public void eliminarCliente(@PathVariable Integer id) {
        clienteService.eliminarCliente(id);
    }
}

/*package com.veterinaria.demo.controlador;

import com.veterinaria.demo.ErrorHeading.NotFoundException;
import com.veterinaria.demo.entidad.Cliente;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


import com.veterinaria.demo.servicio.ClienteService;

import java.util.List;

@Controller
@RequestMapping("/cliente")
public class ClienteController{

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String listar(Model model){
        List<Cliente> clientes = clienteService.obtenerTodosClientes();
        model.addAttribute("clientes", clientes);
        return "clientes";
    }

    @GetMapping("/{id}")
    public String detalleCliente(@PathVariable Integer id, Model model){
        Cliente cliente = clienteService.obtenerClientePorId(id);
        if(cliente != null){
            model.addAttribute("cliente", cliente);
            return "detalle_cliente";
        }else {
            throw new NotFoundException(id, "El cliente con ID " + id + " no existe.");
        }
    }

    @GetMapping("/crear")
    public String mostrarFormularioCreacion( Model model){
        model.addAttribute("cliente", new Cliente());
        return "registro_usuario";
    }

    @PostMapping("/crear")
    public String crearCliente(@ModelAttribute Cliente cliente,
                               @RequestParam("confirm_password") String confirmPassword,
                               Model model) {
        if (!cliente.getContrasena().equals(confirmPassword)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "registro_usuario";
        }

        clienteService.crearCliente(cliente);
        return "redirect:/inicio_sesion";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Integer id, Model model){
        Cliente cliente = clienteService.obtenerClientePorId(id);
        model.addAttribute("cliente", cliente);
        return("editar_info_usuario");
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarCliente(@PathVariable Integer id, Cliente cliente){
        clienteService.editarCliente(id, cliente);
        return "redirect:/cliente";
        //Debería ser index, pero no encuentra al usuario
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Integer id){
        clienteService.eliminarCliente(id);
        return "redirect:/cliente";
    }
} */