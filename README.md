# 🛍️ Sistema de Ventas

![photo7](https://github.com/cruzito-exe/muebleriaJava/assets/54298536/4f80d829-334b-4986-8eaa-9dc7e23f2d6e)

Este proyecto es un sistema de ventas básico desarrollado en Java Maven utilizando Netbeans 12.0 como entorno de desarrollo integrado (IDE) y MySQL como sistema de gestión de bases de datos (SGBD) utilizando XAMPP.

## Requisitos previos
- Netbeans 12.0 o superior instalado en el equipo.
- XAMPP (o cualquier otro software similar) instalado para configurar el servidor MySQL.

## Configuración del entorno

1. Clona o descarga este repositorio en tu equipo.
2. Abre Netbeans y selecciona "Abrir Proyecto".
3. Navega hasta la ubicación donde clonaste o descargaste el repositorio y selecciona el proyecto.
4. Asegúrate de que XAMPP esté instalado y ejecutando.
5. Abre phpMyAdmin en tu navegador y crea una nueva base de datos llamada `systemTest`.
6. Importa el archivo SQL proporcionado en el paquete `Database` en la base de datos recién creada.
7. Actualiza la configuración de conexión a la base de datos en el archivo `Config/ConexionBD.java` con los detalles correctos de tu entorno de MySQL.

```java
public class ConexionBD {
 static Statement statement;
 static Connection connection = null;
 
 public static Connection conexionBD() {
  try {
   Class.forName("com.mysql.jdbc.Driver");
   connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/systemTest", user, password);
   statement = connection.createStatement();
  } catch (ClassNotFoundException | SQLException ex) {
   System.out.println("Error al conectar con la base de datos, error: "+ex.getMessage());
  }
     
  return connection;
 }
}
```

## Ejecución del proyecto

1. Después de configurar la conexión a la base de datos, puedes ejecutar el proyecto en Netbeans seleccionando la opción "Run" o presionando la combinación de teclas `Shift + F6` en `Login.java`.
2. El sistema se abrirá en una ventana.
3. Puedes navegar por las diferentes opciones del sistema, como agregar productos, realizar ventas, ver informes, etc.

## Estructura del proyecto

El proyecto sigue una estructura básica de paquetes:

- `Config`: Contiene la configuración de la conexión a la base de datos.
- `Database`: Contiene el script SQL de la base de datos.
- `Classes`: Contiene las clases que representan los objetos del dominio del negocio, como `Inventory` y `Sales`.
- `Interface`: Contiene las clases que implementan la interfaz de usuario del sistema.
- `Reports`: Contiene los manejadores de reportería que se implementan en el sistema.
- `Icons`: Contiene las imágenes/iconos que se implementan tanto en el menú prinicipal como en `buscar` y `reporte` de cada formulario.
- `Screenshots`: Contiene las capturas de pantalla mostradas en el Help Center.

## Contribución

Si deseas contribuir a este proyecto, puedes hacerlo siguiendo estos pasos:

1. Haz un fork de este repositorio.
2. Crea una rama para tu nueva función (`git checkout -b feature/nueva-funcion`).
3. Realiza los cambios y realiza commits descriptivos de tus modificaciones (`git commit -am 'Agrega una nueva función'`).
4. Envía tus cambios a tu repositorio remoto (`git push origin feature/nueva-funcion`).
5. Abre un pull request en este repositorio.

## Licencia

Este proyecto se encuentra bajo la licencia [MIT](LICENSE).

## Contacto

Si tienes alguna pregunta o sugerencia sobre el proyecto, no dudes en contactarme a través del correo electrónico [dcruzer92@gmail.com](mailto:dcruzer92@gmail.com).

Espero que este sistema de ventas para Mueblería "El Serrucho" sea útil para tu aprendizaje. ¡Gracias por usar mi software!
