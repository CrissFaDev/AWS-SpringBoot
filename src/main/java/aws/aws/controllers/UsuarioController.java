package aws.aws.controllers;

import aws.aws.dto.UsuarioDTO;
import aws.aws.services.impl.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api-app")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/usuario")
    public List<UsuarioDTO> listarCliente() throws Exception {
        return this.usuarioService.listarUsuario();
    }

    @PostMapping("/usuario")
    public UsuarioDTO agregarUsuario(@RequestBody UsuarioDTO usuario){
        var usuarioentrada = this.usuarioService.guardarUsuario(usuario);
        return usuarioentrada;
    }

    @PutMapping("/usuario")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(@RequestBody UsuarioDTO usuarioDTO){
        UsuarioDTO UsuarioActualizado = usuarioService.actualizarUsuario(usuarioDTO);
        return  ResponseEntity.ok(UsuarioActualizado);
    }

    @DeleteMapping("/usuario/{id}")
    public ResponseEntity eliminarUsuario(@PathVariable int id){
        usuarioService.eliminarUsuarioPorId(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/usuario/{id}/imagen")
    public ResponseEntity<UsuarioDTO> guardarImagenEnAWS(@PathVariable int id, @RequestParam("file") MultipartFile file) {
        try {
            UsuarioDTO usuarioActualizado = usuarioService.guardarImagenEnAWS(id, file);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
