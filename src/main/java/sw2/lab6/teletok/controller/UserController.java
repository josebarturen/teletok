package sw2.lab6.teletok.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sw2.lab6.teletok.entity.User;
import sw2.lab6.teletok.repository.UserRepository;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/user/signIn")
    public String signIn(){
        return "user/signIn";
    }

    @GetMapping("/user/signInRedirect")
    public String signInRedirect(Authentication auth, HttpSession session){
        User user = userRepository.findByUsername(auth.getName());
        session.setAttribute("user", user);
        return "redirect:/";
    }

    @GetMapping("/user/signUp")
    public String signUp(@ModelAttribute("user") User user){
        return "user/signUp";
    }

    @PostMapping("/user/save")
    public String saveUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, RedirectAttributes attr) {
        if(bindingResult.hasErrors()){
            return "user/signUp";
        } else {
            try {
                attr.addFlashAttribute("msg","Usuario Registrado Exitosamente. Puede Iniciar Sesión.");
                PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                user.setEnable(1);
                user.setRoleId(2);
                userRepository.save(user);
                return "redirect:/";
            } catch (DataIntegrityViolationException ex) {
                bindingResult.rejectValue("username", "typeMismatch"); // pass an error message to the view
                return "user/signUp";
            }
        }
    }
}
