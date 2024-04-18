# Descripción del Proyecto

Este proyecto implementa un sistema que permite realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) en archivos almacenados en el servicio de almacenamiento en la nube de AWS (Amazon Simple Storage Service, S3). Además, incluye un CRUD de usuarios con un endpoint que permite agregar una imagen a un usuario. La imagen se carga en AWS S3 y la URL se guarda en el campo "IMG" de la tabla de usuarios.

## Funcionalidades

- **Operaciones en AWS S3:**
  - **Agregar Archivo:** Permite cargar un archivo en AWS S3.
  - **Listar Archivos:** Muestra una lista de todos los archivos almacenados en AWS S3.
  - **Eliminar Archivo:** Elimina un archivo específico de AWS S3.
  - **Actualizar Archivo:** Actualiza un archivo existente en AWS S3.

- **CRUD de Usuarios:**
  - **Crear Usuario:** Permite crear un nuevo usuario.
  - **Leer Usuario:** Muestra la información de un usuario específico.
  - **Actualizar Usuario:** Actualiza la información de un usuario existente.
  - **Eliminar Usuario:** Elimina un usuario de la base de datos.
  - **Agregar Imagen a Usuario:** Permite agregar una imagen a un usuario. La imagen se carga en AWS S3 y la URL se guarda en el campo "IMG" del usuario.

## Tecnologías Utilizadas

- AWS S3
- Spring Boot
- Java
- [Tecnología de Base de Datos Utilizada, por ejemplo, MySQL, PostgreSQL, MongoDB, etc.]

## Instalación

1. Clona este repositorio en tu máquina local.

## Configuración

Antes de ejecutar la aplicación, asegúrate de configurar las siguientes variables de entorno:

- `AWS_ACCESS_KEY_ID`: La clave de acceso de tu cuenta de AWS.
- `AWS_SECRET_ACCESS_KEY`: La clave secreta de acceso de tu cuenta de AWS.
- Configuracion de la base de datos.

## Ejecución

1. Ejecuta el proyecto utilizando Spring Boot.


## Contribución

Las contribuciones son bienvenidas. Para sugerir nuevas características, mejorar el código existente o informar problemas, abre un "Issue" (Problema) o envía un "Pull Request" (Solicitud de extracción).

---

¡Gracias por usar nuestro proyecto! Si tienes alguna pregunta o sugerencia, no dudes en contactarnos.
