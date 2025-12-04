# Sistema de Gestión de Estacionamiento (Parking System) 🅿️

Aplicación de consola (CLI) para la gestión y cobro de tarifas de estacionamiento. Este proyecto ha sido desarrollado en **Java 21** utilizando **Maven**, aplicando principios de **Diseño Orientado a Objetos** y metodología **TDD (Test Driven Development)**.

## Descripción del Diseño
El sistema se basa en una **Arquitectura en Capas** para asegurar la separación de responsabilidades:

1.  **Capa de Presentación (`ParkingApp`):** Maneja la interacción con el usuario (entradas/salidas) y validaciones básicas de formato.
2.  **Capa de Servicio (`ParkingLotService`):** Orquestador del sistema. Mantiene el estado de los tickets en memoria y coordina las operaciones. Utiliza **inyección de dependencias** para delegar el cálculo de tarifas.
3.  **Capa de Dominio (`ParkingRateCalculator`, `ParkingTicket`):** Contiene las reglas de negocio (tarifas, topes, descuentos) y las entidades de datos.

### Diagrama de Clases (UML)
![](./UML.png)

## Instrucciones de Ejecución
Prerrequisitos
- Java JDK: Versión 21 o superior.
- Maven: Versión 3.6 o superior.

1. Compilar el proyecto
Sitúate en la raíz del proyecto y ejecuta para limpiar y empaquetar:
mvn clean package

2. Ejecutar la aplicación
Una vez generado el artefacto, inicia el programa con:
java -cp target/parking-calculator-1.0-SNAPSHOT.jar parking.ParkingApp

## Reporte de Cobertura
Herramienta utilizada: VS Code Java Test Runner (Motor JaCoCo).

Para este proyecto, he priorizado la Cobertura de Ramas (Branch Coverage) sobre la simple cobertura de líneas. Esta decisión estratégica se debe a que el núcleo del dominio (ParkingRateCalculator) concentra reglas de negocio basadas en condiciones complejas —como la aplicación de topes diarios o descuentos de fin de semana—; por consiguiente, era indispensable validar matemáticamente cada bifurcación lógica (if/else) tanto en sus caminos verdaderos como falsos, asegurando así la robustez del cálculo de tarifas y la ausencia de código muerto en la capa de servicio.


## Licencia
Este proyecto se distribuye bajo la licencia **MIT**. Consulta el archivo [LICENSE](LICENSE) para más detalles.