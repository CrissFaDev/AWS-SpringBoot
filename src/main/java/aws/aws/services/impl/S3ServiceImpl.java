package aws.aws.services.impl;

import aws.aws.services.IS3Service;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.neptunedata.model.S3Exception;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class S3ServiceImpl implements IS3Service {
    @Value("${upload.s3.localPath}")
    // Define el directorio local donde se almacenarán los archivos descargados desde S3.
    private String localPath;

    private final AmazonS3Client amazonS3; // Cliente de Amazon S3 para interactuar con el servicio.

    @Autowired
    public S3ServiceImpl(AmazonS3Client amazonS3Client) { // Constructor para inyectar el cliente de Amazon S3.
        this.amazonS3 = amazonS3Client;
    }

    //Listar los archivos de AWS
    public List<String> listFiles() throws IOException {
        try {
            // Crea una solicitud para listar objetos en el bucket "bucket-aws-login".
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName("bucket-aws-login");

            // Obtiene la lista de objetos en el bucket.
            ObjectListing objects = amazonS3.listObjects(listObjectsRequest);
            List<String> fileNames = new ArrayList<>();

            // Itera sobre cada resumen de objeto en la lista de objetos.
            for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
                // Agrega el nombre del objeto a la lista de nombres de archivos.
                fileNames.add(objectSummary.getKey());
            }

            // Devuelve la lista de nombres de archivos.
            return fileNames;
        } catch (S3Exception e) {
            // Captura excepciones específicas de Amazon y del SDK de AWS.
            throw new IOException("Error al listar archivos en Amazon S3: " + e.getMessage(), e);
        }
    }

    // subir archivo a AWS
    public String uploadFile(MultipartFile file) throws IOException {
        try {
            String fileName = file.getOriginalFilename(); // Obtiene el nombre del archivo a partir del MultipartFile.

            if (fileName == null) {// Verifica si el nombre del archivo es nulo.
                throw new IllegalArgumentException("Invalid file name");
            }

            // Crea una solicitud para subir el archivo a Amazon S3.
            PutObjectRequest putObjectRequest = new PutObjectRequest("bucket-aws-login", fileName, file.getInputStream(), null);

            // Subir el archivo al bucket S3
            amazonS3.putObject(putObjectRequest);

            return "archivo subido correctamente";
        } catch (AmazonServiceException e) {
            // Manejar excepciones específicas de Amazon
            throw new IOException("Error al interactuar con los servicios de Amazon S3: " + e.getMessage(), e);
        } catch (SdkClientException e) {
            // Manejar excepciones generales de AWS SDK
            throw new IOException("Error al interactuar con el SDK de AWS: " + e.getMessage(), e);
        }
    }

    // descargar archivo de AWS
    public String downLoadFile(String fileName) throws IOException {

        if (!doesObjectExist(fileName)) { // Verifica si el archivo existe en el bucket de S3.
            return "el archivo introducido no existe!!";
        }

        GetObjectRequest request = new GetObjectRequest("bucket-aws-login", fileName); // Crea una solicitud para descargar el archivo desde S3.

        try (S3Object s3Object = amazonS3.getObject(request);

             // Se obtiene un flujo de entrada (InputStream) del objeto S3Object, que proporciona acceso al contenido del archivo en S3.
             S3ObjectInputStream objectInputStream = s3Object.getObjectContent();

             // Se crea un flujo de salida (FileOutputStream) para escribir los datos descargados en un archivo local.
             // El archivo se crea en el directorio especificado por localPath y se le asigna el nombre del archivo que se está descargando.
             FileOutputStream fileOutputStream = new FileOutputStream(localPath + fileName)) {

            // Se crea un búfer de bytes de tamaño 1024 para leer datos del flujo de entrada en bloques.
            byte[] readBuf = new byte[1024];
            int readLen;

            while ((readLen = objectInputStream.read(readBuf)) > 0) {
                fileOutputStream.write(readBuf, 0, readLen);
            }

        } catch (IOException e) {// Captura excepciones de E/S al descargar el archivo desde S3.
            throw new IOException("Error al descargar el archivo desde Amazon S3: " + e.getMessage(), e);
        }

        return "Archivo descargado correctamente";
    }

    //actualizar archivo de AWS
    public String updateFile(MultipartFile file, String oldFileName) throws IOException {
        if (!doesObjectExist(oldFileName)) {
            return "El archivo introducido no existe";
        }

        String newFileName = file.getOriginalFilename();
        try {

            // Eliminar el archivo original
            deleteFile(oldFileName);

            // Subir el nuevo archivo con el mismo nombre
            PutObjectRequest putObjectRequest = new PutObjectRequest("bucket-aws-login", newFileName, file.getInputStream(), null);
            amazonS3.putObject(putObjectRequest);

            return "Archivo actualizado con exito en s3";
        } catch (S3Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    //delete archivo de AWS
    public String deleteFile(String fileName) throws IOException {
        if (!doesObjectExist(fileName)) {
            return "el archivo introducido no existe";
        }

        try {
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest("bucket-aws-login", fileName);
            amazonS3.deleteObject(deleteObjectRequest);

        } catch (S3Exception e) {
            throw new IOException(e.getMessage());
        }
        return "Archivo eliminado correctamente";
    }


    //existes un objeto en aws
    private boolean doesObjectExist(String objectkey) {
        try {
            // Verifica si el objeto con la clave especificada existe en el bucket de S3.
            ObjectMetadata metadata = amazonS3.getObjectMetadata("bucket-aws-login", objectkey);

        } catch (AmazonS3Exception e) {
            if (e.getStatusCode() == 404) {
                return false;
            }
        }
        return true; // Devuelve true si el objeto existe en el bucket de S3.
    }
}
