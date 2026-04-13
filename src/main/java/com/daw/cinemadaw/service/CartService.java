// package com.daw.cinemadaw.service;

// import java.util.List;
// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.daw.cinemadaw.domain.cinema.Entrada;
// import com.daw.cinemadaw.domain.user.User;
// import com.daw.cinemadaw.repository.EntradaRepository;

// @Service
// public class CartService {

//     @Autowired
//     private EntradaRepository entradaRepository;

//     /**
//      * Obtiene todas las entradas del usuario actual (su carrito)
//      * @param user Usuario autenticado
//      * @return Lista de entradas asociadas al usuario
//      */
//     public List<Entrada> getCartByUser(User user) {
//         return entradaRepository.findByUser(user);
//     }

//     /**
//      * Agrega una entrada al carrito del usuario
//      * @param entrada Entrada a agregar con user ya asignado
//      * @return Entrada guardada
//      */
//     public Entrada addToCart(Entrada entrada) {
//         return entradaRepository.save(entrada);
//     }

//     /**
//      * Elimina una entrada del carrito
//      * @param entradaId ID de la entrada a eliminar
//      */
//     public void removeFromCart(Long entradaId) {
//         entradaRepository.deleteById(entradaId);
//     }

//     /**
//      * Obtiene una entrada específica por ID
//      * @param entradaId ID de la entrada
//      * @return Optional con la entrada si existe
//      */
//     public Optional<Entrada> getEntradaById(Long entradaId) {
//         return entradaRepository.findById(entradaId);
//     }

//     /**
//      * Calcula el precio total del carrito
//      * @param entradas Lista de entradas
//      * @return Precio total
//      */
//     public double calculateTotal(List<Entrada> entradas) {
//         return entradas.stream()
//                 .mapToDouble(entrada -> entrada.getScreening().getPrice())
//                 .sum();
//     }

//     /**
//      * Limpia completamente el carrito del usuario
//      * @param user Usuario
//      */
//     public void clearCart(User user) {
//         List<Entrada> entradasDelUsuario = entradaRepository.findByUser(user);
//         entradaRepository.deleteAll(entradasDelUsuario);
//     }
// }
