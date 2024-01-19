package com.tinexlab.backend.service;

import com.tinexlab.backend.config.implementation.CustomUserDetails;
import com.tinexlab.backend.model.dto.request.AuthenticationRequest;
import com.tinexlab.backend.model.dto.request.RegistrationRequest;
import com.tinexlab.backend.model.dto.request.ResetPasswordEmailRequest;
import com.tinexlab.backend.model.dto.request.ResetPasswordRequest;
import com.tinexlab.backend.model.dto.request.SessionRequest;
import com.tinexlab.backend.model.dto.response.GenericResponse;
import com.tinexlab.backend.model.entity.Session;
import com.tinexlab.backend.model.entity.User;
import com.tinexlab.backend.repository.UserRepository;
import com.tinexlab.backend.util.Role;
import com.tinexlab.backend.util.helpers.HelperClass;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private JavaMailSender mailSender;

    private final HelperClass helperClass = new HelperClass();

    @Autowired
    private JwtService jwtService;

    public GenericResponse<?> login(AuthenticationRequest authRequest) {

        Optional<User> optionalUser = userRepository.findByUsername(authRequest.getUsername());
        if(optionalUser.isEmpty()){
            return GenericResponse
                    .getResponse(400,
                            "No se encuentra el usuario.",
                            null);
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(), authRequest.getPassword()
        );

        try {
            authenticationManager.authenticate(authToken);
        } catch (AuthenticationException e) {
            return GenericResponse
                    .getResponse(403,
                            "La contraseña es incorrecta.",
                            null);
        }catch (DataAccessException e) {
            return GenericResponse
                    .getResponse(500,
                            "Error al dar acceso: " + e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()),
                            null);
        } catch (Exception e){
            return GenericResponse
                    .getResponse(500,
                            "Error inesperado: " + e.getMessage(),
                            null);
        }

        User user = optionalUser.get();

        String jwt = jwtService.generateToken(user, generateExtraClaims(user));
        return GenericResponse.getResponse(200, "Se dio acceso al usuario", jwt);

    }

    public GenericResponse<?> register(RegistrationRequest registrationRequest, BindingResult result){

        // proceso de validación (lo hace el servicio)
        if(userRepository.findByUsername(registrationRequest.getUsername()).isPresent()){
            return GenericResponse.getResponse(400, "Error al registrar usuario", "El usuario ya existe");
        }

        // proceso de validación
        String errors = helperClass.validaRequest(result);
        if (!errors.isEmpty())
            return GenericResponse.getResponse(400, "Error al registrar usuario: " + errors, errors);

        User user = registrationRequestToUser(registrationRequest);

        try {
            user = userRepository.save(user);
            String jwt = jwtService.generateToken(user, generateExtraClaims(user));
            return GenericResponse.getResponse(201, "Usuario registrado con éxito", jwt);
        } catch(DataAccessException e) {
            return GenericResponse
                    .getResponse(500,
                            "Error al registrar usuario: " + e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()),
                            null);
        }
    }

    public GenericResponse<?> logout (SessionRequest request){
        String jwt = request.getJwt();
        sessionService.finSesion(jwt);
        return GenericResponse.getResponse(200, "Cierra sesión exitosamente", null);
    }

    public GenericResponse<?> sendResetPasswordToken(ResetPasswordEmailRequest request){
        String token = RandomStringUtils.randomAlphanumeric(10);
        String resetPasswordLink = "";
        String frontendLink = "";
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        User user = null;
        try {
            if(optionalUser.isEmpty()){
                return GenericResponse
                        .getResponse(400,
                                "No se encuentra el usuario con email " + request.getEmail(),
                                null);
            }
            user = optionalUser.get();
            userRepository.updateResetPasswordToken(user.getEmail(), token);
            frontendLink = HelperClass.FRONTEND_LINK;
            resetPasswordLink = frontendLink + "/auth/reset-password?token=" + token;
            sendEmail(request.getEmail(), resetPasswordLink);

        }catch(DataAccessException e) {
            return GenericResponse
                    .getResponse(500,
                            "Error al buscar usuario: " + e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()),
                            null);
        } catch (Exception e){
            return GenericResponse
                    .getResponse(500,
                            "Error inesperado: " + e.getMessage(),
                            null);
        }
        return GenericResponse.getResponse(200, "Se envía token de reseteo de clave a " + request.getEmail(), token);
    }

    public GenericResponse<?> resetPassword(ResetPasswordRequest request){
        Optional<User> optionalUser = userRepository.findByResetPasswordToken(request.getResetPasswordToken());
        User user = null;
        try {
            if(optionalUser.isEmpty()){
                return GenericResponse
                        .getResponse(400,
                                "No se encuentra el usuario con token " + request.getResetPasswordToken(),
                                null);
            }
            user = optionalUser.get();
            user.setPassword(helperClass.encriptarClave(request.getPassword()));
            userRepository.save(user);
        }catch(DataAccessException e) {
            return GenericResponse
                    .getResponse(500,
                            "Error al buscar usuario: " + e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()),
                            null);
        } catch (Exception e){
            return GenericResponse
                    .getResponse(500,
                            "Error inesperado: " + e.getMessage(),
                            null);
        }
        return GenericResponse.getResponse(200, "Se actualiza clave exitosamente", null);
    }

    private void sendEmail(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        
        helper.setFrom("noreply@fullstack.com");
        helper.setTo(recipientEmail); 
        helper.setSubject("Reseteo de clave"); 
        
        String content = helperClass.createTemplateResetPasswordEmail(link);
        
        helper.setText(content, true);
        
        mailSender.send(message);
    }

    private User registrationRequestToUser(RegistrationRequest registrationRequest) {
        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setPassword(registrationRequest.getPassword());
        helperClass.encriptarClaveUsuario(user);
        user.setName(registrationRequest.getName());
        user.setLastName(registrationRequest.getLastName());
        user.setEmail(registrationRequest.getEmail());
        if(registrationRequest.getRole() == null)
            user.setRole(Role.USER);
        else
            user.setRole(registrationRequest.getRole());
        return user;
    }

    private Map<String, Object> generateExtraClaims(User user) {

        CustomUserDetails customUserDetails = new CustomUserDetails(user); // implementación con clases Custom

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId()); //
        extraClaims.put("name", user.getName());
        extraClaims.put("username", user.getUsername());
        extraClaims.put("lastName", user.getLastName()); //
        extraClaims.put("email", user.getEmail()); //
        extraClaims.put("role", user.getRole().name());
        extraClaims.put("failedAttempts", user.getFailedAttempts());
        extraClaims.put("permissions", customUserDetails.getAuthorities());
        extraClaims.put("authorizedRoutes",
                user.getRole()
                        .getRoutes().stream()
                        .map(route -> Map.of("name", route.getName(), "path", route.getPath()))
                        .collect(Collectors.toList())); // rutas
        if(user.getSessions() != null){ // si el usuario ya existe y vuelve a iniciar sesión
            extraClaims.put("lastSessions", new ArrayList<>(
                            user
                                    .getSessions()
                                    .stream().filter(s -> s.getFechaFinSesion() != null)
                                    .map(s -> {
                                        Session session = new Session();
                                        session.setJwt("");
                                        session.setId(s.getId());
                                        session.setUser(s.getUser());
                                        session.setFechaInicioSesion(s.getFechaInicioSesion());
                                        session.setFechaFinSesion(s.getFechaFinSesion());
                                        return session;
                                    })
                                    .collect(Collectors.toList())
                    )
            );
        }
        else { // soluciona el error en el registro
            extraClaims.put("lastSessions", "no previous sessions");
        }

        return extraClaims;
    }

    /* private String crearEmailDesdeNombreUsuario(User user) {
        String[] fullLastName = user.getLastName().toLowerCase().split(" ");
        String emailLastName = "";
        if(fullLastName.length > 1){
            emailLastName = fullLastName[0] + fullLastName[1].charAt(0);
        }
        else emailLastName = user.getLastName().toLowerCase();
        String email = user.getName().toLowerCase().charAt(0) + emailLastName + "@mail.com";
        if(userRepository.findByEmail(email).isPresent()){
            email = user.getName().toLowerCase().charAt(0) + emailLastName + 1 + "@mail.com";
        }
        return email;
    } */

}
