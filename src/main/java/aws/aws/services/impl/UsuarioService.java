package aws.aws.services.impl;

import aws.aws.dto.UsuarioDTO;
import aws.aws.model.Usuario;
import aws.aws.repository.UsuarioRepository;
import aws.aws.services.IUsuarioService;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements IUsuarioService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UsuarioRepository usuarioRepository;

    private final AmazonS3Client amazonS3; // Cliente de Amazon S3 para interactuar con el servicio.

    public UsuarioService(AmazonS3Client amazonS3) {
        this.amazonS3 = amazonS3;
    }


    @Override
    public List<UsuarioDTO> listarUsuario() throws Exception {
        var clientes = this.usuarioRepository.findAll();
        return clientes.stream()
                .map(usuario -> this.modelMapper.map(usuario, UsuarioDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioDTO guardarUsuario(UsuarioDTO usuarioDTO) {
        var usuario = this.usuarioRepository.save(modelMapper.map(usuarioDTO, Usuario.class));
        return modelMapper.map(usuario, UsuarioDTO.class);
    }

    @Override
    public UsuarioDTO actualizarUsuario(UsuarioDTO usuarioDTO) {
        var usuarioEncontrado = usuarioRepository.findById(usuarioDTO.getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuarioEncontrado.setFullName(usuarioDTO.getFullName());
        usuarioEncontrado.setEmail(usuarioDTO.getEmail());
        usuarioEncontrado.setImg(usuarioDTO.getImg());

        var usuarioAcutalizado = this.usuarioRepository.save(usuarioEncontrado);
        return modelMapper.map(usuarioAcutalizado , UsuarioDTO.class);
    }

    @Override
    public void eliminarUsuarioPorId(Integer idUsuario) {
        var usuario = this.usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        this.usuarioRepository.delete(usuario);
    }

    @Override
    public UsuarioDTO guardarImagenEnAWS(int idUsuario, MultipartFile file) throws IOException {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String imageUrl = uploadFile(file);
        usuario.setImg(imageUrl);
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        return modelMapper.map(usuarioGuardado, UsuarioDTO.class);
    }

    private String uploadFile(MultipartFile file) throws IOException {
        try {
            String fileName = file.getOriginalFilename();

            if (fileName == null) {
                throw new IllegalArgumentException("Nombre de archivo no válido");
            }
            PutObjectRequest putObjectRequest = new PutObjectRequest("bucket-aws-login", fileName, file.getInputStream(), null);
            amazonS3.putObject(putObjectRequest);
            return amazonS3.getUrl("bucket-aws-login", fileName).toString();
        } catch (Exception e) {
            throw new IOException("Error al cargar el archivo en Amazon S3: " + e.getMessage(), e);
        }
    }
}
