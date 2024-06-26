package it.marconi.login_registrazione.controllers;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import it.marconi.login_registrazione.domains.LoginForm;
import it.marconi.login_registrazione.domains.RegistrationForm;


@Controller //gli dico che è un controller
//il controller gestisce l'interazione con l'utente


//mapping a livello di classe
public class UserController {

    ArrayList<RegistrationForm> users = new ArrayList<>();

    @RequestMapping(value={"", "/", "/home"})
    public ModelAndView viewHomePage() {

        users.clear();
        return new ModelAndView("homepage");
    }

    //handler con requestParam
    //localhost:8090/home/user?type=
    @GetMapping("/user")
    public ModelAndView handlerUserAction(@RequestParam("type") String type) {

        //gli passo 
        if(type.equals("new"))
            return new ModelAndView("user-registration").addObject(new RegistrationForm());
        
        else if(type.equals("login"))
            return new ModelAndView("user-login").addObject(new LoginForm());
        
        else 
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pagina non trovata!");

    }

    //@ModelAttribut salvo newUser nel dizionario chiave valore (chiave = ModelAttribute("newUser"))
    //@ModelAttirbut serve per recuperare un oggetto dalla view e renderlo accessibile al metodo

    @PostMapping(value={"/user/new"})
    public  ModelAndView redirect_R(@ModelAttribute("newUser") RegistrationForm newUser) {

        String username = newUser.getUsername();
        users.add(newUser);

        //return new ModelAndView("user-detail").addObject("newUser", newUser);
        return new ModelAndView("redirect:/user/"+username);
        
    }

    @PostMapping({"/user/login"})
    public  ModelAndView redirect_L(@ModelAttribute("newUser") LoginForm newUser) {

        String username = newUser.getUsername();
        RegistrationForm r = new RegistrationForm();
        r.setUsername(username);
        r.setPassword(newUser.getPassword());

        users.add(r);

        //return new ModelAndView("user-detail").addObject("newUser", newUser);
        return new ModelAndView("redirect:/user/"+username);
        
    }
    

    //forse non funziona perché il redirect taglia i ponti con il Model?

    @GetMapping("/user/{username}")
    public  ModelAndView userDetail(@PathVariable("username") String username) {

        RegistrationForm user = new RegistrationForm();

        for (RegistrationForm u : users) {
            if(u.getUsername().equals(username)){
                user = u;
            }
        }

        return new ModelAndView("user-detail").addObject("user", user);
    }
    
    

    
}
