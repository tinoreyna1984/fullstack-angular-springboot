package com.tinexlab.backend.service.crud.impl;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.tinexlab.backend.model.dto.request.UserRequest;
import com.tinexlab.backend.model.dto.response.GenericResponse;
import com.tinexlab.backend.model.entity.User;
import com.tinexlab.backend.repository.UserRepository;
import com.tinexlab.backend.service.crud.GenericService;
import com.tinexlab.backend.util.Role;
import com.tinexlab.backend.util.helpers.HelperClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService implements GenericService<User, UserRequest> {

    @Autowired
    private UserRepository userRepository;
    private final HelperClass helperClass = new HelperClass();

    @Override
    public GenericResponse<?> get(Integer page, Integer size){
        try{
            if (page != null && size != null) {
                // Si se proporcionan los parámetros de paginación, devuelve una lista paginada
                Pageable pageable = PageRequest.of(page, size);
                Page<User> pageResult = userRepository.findAll(pageable);
                return GenericResponse.getResponse(200, "Se encuentran los usuarios", pageResult);
            } else {
                // Si no se proporcionan los parámetros de paginación, devuelve una lista completa
                List<User> users = userRepository.findAll();
                return GenericResponse.getResponse(200, "Se encuentran los usuarios", users);
            }
        } catch (DataAccessException e){
            return GenericResponse
                    .getResponse(500,
                            "Error al consultar usuarios: " + e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()),
                            null);
        } catch (Exception e){
            return GenericResponse
                    .getResponse(500,
                            "Error inesperado: " + e.getMessage(),
                            null);
        }
    }

    @Override
    public GenericResponse<User> getById(Long id){
        Optional<User> optionalUser = userRepository.findById(id);
        User usuario;
        try {
            if(optionalUser.isEmpty()){
                return GenericResponse
                        .getResponse(400,
                                "No se encuentra el usuario con ID " + id,
                                null);
            }
            usuario = optionalUser.get();
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
        return GenericResponse.getResponse(200, "Usuario encontrado", usuario);
    }

    @Override
    public GenericResponse<?> save(UserRequest request, BindingResult result){
        // verificar que el nombre de usuario no haya sido previamente usado
        Optional<User> optionalUser = userRepository.findByUsername(request.getUsername());
        if(optionalUser.isPresent()){
            return GenericResponse
                    .getResponse(400,
                            "Ya existe usuario con nombre de sesión " + request.getUsername(),
                            null);
        }

        User usuarioNuevo = new User();

        // si no viaja el ROL, por defecto debe ser el de USUARIO
        if(request.getRole() == null)
            request.setRole(Role.USER);

        // proceso de validación
        String errors = helperClass.validaRequest(result);
        if (!errors.isEmpty())
            return GenericResponse.getResponse(400, "Error al crear usuario: " + errors, errors);

        // encripta clave
        helperClass.encriptarClaveUserRequest(request);

        usuarioNuevo.setEmail(request.getEmail());
        usuarioNuevo.setUsername(request.getUsername());
        usuarioNuevo.setPassword(request.getPassword());
        usuarioNuevo.setRole(request.getRole());

        try {
            usuarioNuevo = userRepository.save(usuarioNuevo);
        } catch(DataAccessException e) {
            return GenericResponse
                    .getResponse(500,
                            "Error al crear usuario: " + e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()),
                            null);
        } catch (Exception e){
            return GenericResponse
                    .getResponse(500,
                            "Error inesperado: " + e.getMessage(),
                            null);
        }

        return GenericResponse.getResponse(201, "Usuario creado", usuarioNuevo);
    }

    @Override
    public GenericResponse<?> update(UserRequest request, Long id, BindingResult result){
        // proceso de validación
        String errors = helperClass.validaRequest(result);
        if (!errors.isEmpty())
            return GenericResponse.getResponse(400, "Error al actualizar usuario", errors);

        Optional<User> optionalUser = userRepository.findById(id);
        User usuarioEditado = null;
        if(optionalUser.isEmpty())
            return GenericResponse
                    .getResponse(400,
                            "No se encuentra el usuario con ID " + id,
                            null);
        User usuarioActual = optionalUser.get();
        try {
            usuarioActual.setEmail(request.getEmail());
            usuarioActual.setUsername(request.getUsername());
            usuarioActual.setPassword(request.getPassword());
            // si no viaja el ROL, por defecto debe ser el de USUARIO
            if(request.getRole() == null)
                usuarioActual.setRole(Role.USER);
            else
                usuarioActual.setRole(request.getRole());
            // encripta clave
            helperClass.encriptarClaveUsuario(usuarioActual);
            usuarioEditado = userRepository.save(usuarioActual);
        } catch(DataAccessException e) {
            return GenericResponse
                    .getResponse(500,
                            "Error al actualizar usuario: " + e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()),
                            null);
        } catch (Exception e){
            return GenericResponse
                    .getResponse(500,
                            "Error inesperado: " + e.getMessage(),
                            null);
        }
        return GenericResponse.getResponse(200, "Usuario actualizado", usuarioEditado);
    }

    @Override
    public GenericResponse<?> delete(Long id){
        try {
            userRepository.deleteById(id);
        }catch(DataAccessException e) {
            return GenericResponse
                    .getResponse(500,
                            "Error al realizar la consulta en la base de datos: " + e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()),
                            null);
        } catch (Exception e){
            return GenericResponse
                    .getResponse(500,
                            "Error inesperado: " + e.getMessage(),
                            null);
        }

        return GenericResponse.getResponse(200, "Usuario borrado", null);
    }

    public GenericResponse<?> dashboard(){
        Map<String, Object> dashboard = new HashMap<>();
        long totalUsers = 0L;
        long activeUsers = 0L;
        long lockedUsers = 0L;
        try {
            totalUsers = userRepository.totalUsers();
            activeUsers = userRepository.totalActiveUsers();
            lockedUsers = userRepository.totalLockedUsers();
            dashboard.put("total", totalUsers);
            dashboard.put("activos", activeUsers);
            dashboard.put("bloqueados", lockedUsers);
            return GenericResponse.getResponse(200, "Registro encontrado", dashboard);
        }catch(DataAccessException e) {
            return GenericResponse
                    .getResponse(500,
                            "Error al obtener valores del dashboard: " + e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()),
                            null);
        }
    }

    public GenericResponse<?> cargarDesdeCSV(MultipartFile archivo) throws IOException {
        try (
             Reader reader = new InputStreamReader(archivo.getInputStream());
             CSVReader csvReader = new CSVReaderBuilder(reader).build()
        ) {
            List<String[]> filas = csvReader.readAll();

            for (String[] fila : filas) {
                UserRequest userRequest = pasarValores(fila);
                User user = new User();
                user.setUsername(userRequest.getUsername());
                helperClass.encriptarClaveUserRequest(userRequest);
                user.setPassword(userRequest.getPassword());
                user.setEmail(userRequest.getEmail());
                user.setName(userRequest.getName());
                user.setLastName(userRequest.getLastName());
                user.setRole(userRequest.getRole());
                userRepository.save(user);
            }
            return GenericResponse.getResponse(201, "Se creó registro desde CSV", archivo.getName());
        } catch (CsvException e) {
            return GenericResponse
                    .getResponse(400,
                            "Error al cargar desde CSV, revisar archivo ",
                            null);
        } catch (DataAccessException e){
            return GenericResponse
                    .getResponse(500,
                            "Error al cargar desde CSV: " + e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()),
                            null);
        }
    }

    private UserRequest pasarValores(String[] fila) {
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername(fila[0]);
        userRequest.setPassword(fila[1]);
        userRequest.setEmail(fila[2]);
        userRequest.setName(fila[3]);
        userRequest.setLastName(fila[4]);
        userRequest.setRole(Role.valueOf(fila[5]));
        return userRequest;
    }

}
