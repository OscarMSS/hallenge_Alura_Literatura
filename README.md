# Descripción
Este proyecto es una aplicación backend construida con Spring Boot que tiene como objetivo gestionar libros de una biblioteca en línea. Utiliza una base de datos para almacenar información de los libros, como el título, autor, idioma, y número de descargas. La aplicación proporciona una API RESTful que permite realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre los libros, además de ofrecer consultas específicas como buscar libros por idioma o por título.

El proyecto está diseñado para ser escalable, eficiente y fácil de mantener, utilizando prácticas estándar de desarrollo de software y herramientas como Spring Data JPA para interactuar con la base de datos, HikariCP para la gestión de conexiones de base de datos, y Hibernate para la persistencia de objetos Java en la base de datos.

Además, se ha configurado un sistema de logging con Logback para registrar las actividades de la aplicación y facilitar la depuración y el monitoreo del sistema.

## Características
Operaciones CRUD: Crear, leer, actualizar y eliminar libros en la base de datos.
Búsqueda por idioma y título: Consultas eficientes para obtener libros en función de su idioma o título.
Gestión de conexiones a base de datos: Conexiones optimizadas utilizando HikariCP.
Manejo de transacciones de Hibernate: Control sobre las transacciones de la base de datos con un logging detallado.
Configuración personalizada de logging: Niveles de log ajustados para HikariCP y Hibernate para minimizar la verbosidad innecesaria.
Tecnologías
Spring Boot: Framework principal para la creación de aplicaciones Java basadas en microservicios.
Spring Data JPA: Para la gestión de la persistencia de datos en la base de datos.
Hibernate: Framework ORM para la interacción con la base de datos.
Logback: Sistema de logging configurado para registrar las actividades de la aplicación.
HikariCP: Pool de conexiones a base de datos de alto rendimiento.

# Autor
Oscar Manuel Sánchez Sibayiz
