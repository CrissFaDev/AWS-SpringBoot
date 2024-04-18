package aws.aws.services;

import aws.aws.dto.UsuarioDTO;
import aws.aws.model.Usuario;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IUsuarioService  {

    public List<UsuarioDTO> listarUsuario() throws Exception;

    public UsuarioDTO guardarUsuario(UsuarioDTO usuarioDTO);

    public UsuarioDTO actualizarUsuario(UsuarioDTO usuarioDTO);

    public UsuarioDTO guardarImagenEnAWS(int idUsuario, MultipartFile file) throws IOException;
    public void eliminarUsuarioPorId(Integer idUsuario);
}
