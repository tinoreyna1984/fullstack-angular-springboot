package com.tinexlab.backend.util.helpers;

import com.tinexlab.backend.model.dto.request.UserRequest;
import com.tinexlab.backend.model.entity.User;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

public class HelperClass {

    // codificador de password
    private final PasswordEncoder passwordEncoder;

    public static final String FRONTEND_LINK = "http://localhost:4200";

    public HelperClass() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String validaRequest(BindingResult result) {
        if(result.hasErrors()) {
            List<String> errorsList = result.getFieldErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            StringBuilder errors = new StringBuilder();
            for (String error : errorsList) {
                errors.append(error).append(" ");
            }
            return errors.toString();
        }
        return "";
    }

    public void encriptarClaveUserRequest(UserRequest userRequest) {
        String claveEncriptada = passwordEncoder.encode(userRequest.getPassword());
        userRequest.setPassword(claveEncriptada);
    }
    public void encriptarClaveUsuario(User user) {
        String claveEncriptada = passwordEncoder.encode(user.getPassword());
        user.setPassword(claveEncriptada);
    }

    public String encriptarClave(String clave){
        return passwordEncoder.encode(clave);
    }

    public String createTemplateResetPasswordEmail(String link){

        String template = 
            "<!DOCTYPE html>\r\n" + //
            "<html lang=\"en\">\r\n" + //
            "  <head>\r\n" + //
            "    <title>Home</title>\r\n" + //
            "    <meta charset=\"UTF-8\" />\r\n" + //
            "    <meta name=\"viewport\" content=\"width=device-width\" />\r\n" + //
            "    <link rel=\"stylesheet\" href=\"styles.css\" />\r\n" + //
            "    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN\" crossorigin=\"anonymous\">\r\n" + //
            "    <script src=\"https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js\" integrity=\"sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r\" crossorigin=\"anonymous\"></script>\r\n" + //
            "    <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.min.js\" integrity=\"sha384-BBtl+eGJRgqQAUMxJ7pMwbEyER4l1g+O15P+16Ep7Q9Q+zqX6gSbd85u4mG4QzX+\" crossorigin=\"anonymous\"></script>\r\n" + //
            "  </head>\r\n" + //
            "  <body>\r\n" + //
            "    <div class=\"p-1 m-3 border border-2 rounded\">\r\n" + //
            "    <h1 class=\"bg-dark text-white\">Hola</h1>\r\n" + //
            "    <p>Has pedido el reseteo de tu password.</p>\r\n" + //
            "    <p><a href=\"" + link +"\">\r\n" + //
            "      Haz clic aquí para proceder.\r\n" + //
            "    </a>\r\n" + //
            "    </p>\r\n" + //
            "    <p>Ignora este correo si recuerdas tu clave, o si no lo has solicitado.</p>\r\n" + //
            "    </div>\r\n" + //
            "  </body>\r\n" + //
            "</html>";

        return template;
    }

    public static String getSiteURL(){
        String domainName = "";
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            domainName = attributes.getRequest().getRequestURL().substring(0, 30).toString();
        }

        return domainName;
    }
    
}
