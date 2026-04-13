// // package com.daw.cinemadaw.Controllers;


// // import java.util.HashMap;
// // import java.util.List;
// // import java.util.Map;

// // import org.springframework.stereotype.Controller;
// // import org.springframework.ui.Model;
// // import org.springframework.web.bind.annotation.PostMapping;

// // import com.daw.cinemadaw.DTO.SeatsListDTO;

// // import jakarta.servlet.http.HttpSession;

// // @Controller
// // public class CurrentController {

// //     @PostMapping("/screenings/seats/confirm/{id}"){
// //         public String confirmSeats(@PathVariable Long id, @ModelAttribute SeatsListDTO selectedSeats, Model model, HttpSession session) {
// //             Map<Long, List<Long>> cart = (Map<Long, List<Long>>) session.getAttribute("cart");
            
// //             if (cart == null) {
// //                 cart = new HashMap<>();
// //             }

// //             cart.put(id, selectedSeats.getSeatIds());
// //             session.setAttribute("cart", cart);
// //             return "/";  // Redirigeix a una pàgina de confirmació
// //         }
// // }
// // }

    
// @GetMapping("/screenings/seats/{id}")
// public String selectedSeats(@PathVariable Long id, Model model, HttpSession session) {
    
//     Optional <Screening> screeningOpt = screeningService.getScreeningById(id);
//     if (screeningOpt.isEmpty()) {
//         return "redirect:/";  
//     }

//     Map<Long, List<Long>> cart = (Map<Long, List<Long>>) session.getAttribute("cart");

//     if(cart ==null){
//         cart = new HashMap<>();
//     }

//     SeatsListDTO seatsListDTO = new SeatsListDTO();
//     model.addAttribute("selectedSeats", seatsListDTO);
//     model.addAttribute("screening", screeningOpt.get());
//     return "seats";  // Retorna el nom de la vista que mostrarà les butaques
// }

    
