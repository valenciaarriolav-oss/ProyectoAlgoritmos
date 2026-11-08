# Documentación — SistemaVentas en NetBeans

Proyecto final de Algoritmos: sistema de ventas y facturación básica.  
El programa en Java vive dentro de un **proyecto de NetBeans**, no en un archivo suelto.

## 1. Cómo abrirlo

NetBeans abre la **carpeta del proyecto**, no el `.java`.

1. Instala el JDK (Java 8 o superior): https://adoptium.net/
2. Instala Apache NetBeans.
3. En NetBeans: **File → Open Project** (Archivo → Abrir proyecto).
4. Entra a la carpeta descomprimida y elige la carpeta **`SistemaVentas`**.  
   Esa carpeta es la que contiene `nbproject` y `src`. El ícono debe verse como una taza de café, no como una carpeta vacía.
5. Pulsa **Open Project**.
6. En el panel de la izquierda (Projects) expande **Source Packages → sistemaventas → SistemaVentas.java**.
7. Ejecuta con el botón **Run** (triángulo verde) o con **F6**.

La consola sale abajo, en la ventana **Output**. Ahí se escribe el menú y se leen los datos.

Si alguien abre solo `SistemaVentas.java` con **Open File**, NetBeans muestra el texto pero no hay botón Run del proyecto. Hay que cerrar eso y usar **Open Project** sobre la carpeta `SistemaVentas`.

## 2. Estructura

```
SistemaVentas/                          <-- esta carpeta es el proyecto
├── DOCUMENTACION.md                    <-- este archivo
├── build.xml                           <-- NetBeans lo usa para compilar y ejecutar
├── manifest.mf                         <-- datos del .jar; el IDE completa la clase main
├── nbproject/
│   ├── project.xml                     <-- nombre del proyecto y tipo (Java SE)
│   ├── project.properties              <-- clase main, carpeta src, versión de Java
│   ├── build-impl.xml                  <-- script interno. No se edita a mano
│   └── genfiles.properties             <-- control del IDE. No se edita a mano
└── src/
    └── sistemaventas/
        └── SistemaVentas.java          <-- aquí está todo el programa
```

Qué hace cada pieza:

| Archivo o carpeta | Para qué sirve | ¿Se modifica en la defensa? |
| --- | --- | --- |
| `src/sistemaventas/SistemaVentas.java` | El programa: menú, ventas, descuentos, caja y estadísticas | Sí. Es el único archivo de código |
| `nbproject/project.properties` | Le dice a NetBeans que la clase principal es `sistemaventas.SistemaVentas` y que el código está en `src` | Solo si hay que cambiar la versión de Java |
| `nbproject/project.xml` | Identifica el proyecto como aplicación Java SE de NetBeans y le pone el nombre SistemaVentas | No |
| `build.xml` | Ordena compilar, empaquetar y ejecutar. Importa `build-impl.xml` | No |
| `nbproject/build-impl.xml` | Detalle interno de esas órdenes. NetBeans lo regenera | No |
| `manifest.mf` | Encabezado del archivo `.jar` que NetBeans crea en `dist/` | No |
| `build/` y `dist/` | Aparecen al compilar. `dist/SistemaVentas.jar` es el programa empaquetado | No se entregan; se pueden borrar con Clean |

La primera línea del Java es obligatoria y tiene que coincidir con la carpeta:

```java
package sistemaventas;
```

`package sistemaventas` significa “este archivo está en la carpeta `src/sistemaventas`”. Si se borra esa línea, o se mueve el archivo a otra carpeta, NetBeans deja de encontrar la clase principal.

## 3. Dónde está configurada la clase principal

En `nbproject/project.properties`:

```
main.class=sistemaventas.SistemaVentas
src.dir=src
javac.source=1.8
javac.target=1.8
source.encoding=UTF-8
```

`sistemaventas.SistemaVentas` se lee así: paquete `sistemaventas`, clase `SistemaVentas`.  
El código usa Java 8 a propósito, para que abra en NetBeans de laboratorio y también en uno nuevo.

Para verlo dentro del IDE: clic derecho en el proyecto **SistemaVentas → Properties → Run**. En **Main Class** debe decir `sistemaventas.SistemaVentas`.

## 4. Cómo ejecutarlo

Dentro de NetBeans: abrir el proyecto y pulsar **F6**.

Sin NetBeans, en la carpeta donde está `ejecutar.bat`, doble clic en ese archivo. Compila `src/sistemaventas/SistemaVentas.java` y lo corre.

Al registrar una venta, los decimales van con punto: `10.50`.

Comprobación de las fórmulas. Un solo producto, precio `200`, cantidad `1`:

| Concepto | Valor |
| --- | --- |
| Subtotal | 200.00 |
| Descuento 5 % | 10.00 |
| Monto con descuento | 190.00 |
| IVA 12 % | 22.80 |
| Total | 212.80 |

Si el total da **212.80**, el descuento y el IVA están bien calculados.

## 5. Análisis del problema

El programa simula una caja. El usuario registra ventas de varios productos, elige cómo paga y puede consultar la caja o las estadísticas. El menú sigue activo hasta que confirma que quiere salir.

**Entradas:** opción del menú, cantidad de productos, código, nombre, precio, cantidad, forma de pago y, si es efectivo, el monto recibido.

**Salidas:** resumen de la factura, consulta de caja y estadísticas. Si todavía no hay ventas, las estadísticas muestran exactamente: `No existen ventas registradas.`

**Restricción del curso:** no se usan arreglos, listas, archivos ni varias clases. Por eso cada producto se procesa en el momento, dentro del `for`, y lo único que se conserva de una venta a otra son contadores y acumuladores.

## 6. Recorrido del código

Todo está en `main`, en este orden. En el archivo cada bloque tiene un comentario con el mismo nombre.

1. **Variables de la venta actual.** Código, nombre, precio, cantidad, subtotal, descuento, IVA y total. Sirven para una sola factura.
2. **Variables de caja.** `ventasRealizadas`, `productosVendidos`, `totalVendido`, contadores por forma de pago, `ventaMayor`, `ventaMenor` y `hayVentas`. Empiezan en cero o en `false` y viven todo el programa.
3. **`do` / `while (!confirmarSalida)`.** Es el menú. Equivale al `Repetir ... Hasta Que` de PSeInt.
4. **`while` de la opción.** No deja seguir si escriben un número distinto de 1, 2, 3 o 4, o si escriben letras.
5. **`switch (opcion)`.**  
   - 1 registra la venta  
   - 2 muestra la caja  
   - 3 muestra estadísticas  
   - 4 pregunta si de verdad desea salir
6. **Dentro de la opción 1:**
   - `while` para que la cantidad de productos sea mayor que cero
   - `for` para pedir cada producto (código, nombre, precio, cantidad)
   - `if / else` para el porcentaje de descuento
   - fórmulas de descuento, IVA y total
   - `while` y `switch` de la forma de pago
   - si es efectivo, otro `while` hasta que el monto alcance el total
   - resumen en pantalla
   - actualización de la caja y de la venta mayor / menor
7. **Opción 4.** Solo pone `confirmarSalida = true` si responden `S` o `s`. Si responden otra cosa, el menú vuelve a aparecer.

Estructuras que el curso pide y dónde están:

| Estructura | Dónde |
| --- | --- |
| `do-while` | Menú principal |
| `while` | Opción válida, datos válidos y efectivo suficiente |
| `for` | Productos de una venta |
| `if / else` | Descuentos, mayor/menor y el mensaje de “no existen ventas” |
| `switch` | Menú y forma de pago |

## 7. Variables que hay que saber explicar

| Variable | Qué guarda |
| --- | --- |
| `cantidadProductos` | Cuántos renglones tiene la factura |
| `unidadesVenta` | Suma de las cantidades de esos renglones |
| `subtotalProducto` | `precio * cantidad` de un producto |
| `subtotalVenta` | Suma de los subtotales de producto |
| `porcentaje` | 0, 0.05, 0.08 o 0.10 |
| `ventasRealizadas` | Cuántas facturas se cerraron |
| `productosVendidos` | Unidades vendidas en todas las facturas |
| `totalVendido` | Dinero cobrado, ya con IVA |
| `hayVentas` | `false` hasta la primera venta. Sirve para iniciar mayor y menor |
| `confirmarSalida` | `true` solo cuando confirman la salida con S |

`subtotalVenta = 0` va **antes** del `for`. Si estuviera dentro, cada producto borraría lo acumulado.

`ventasRealizadas = ventasRealizadas + 1` va **después** del resumen, una sola vez por venta. Si estuviera dentro del `for`, contaría productos en lugar de ventas.

Q200 ya lleva 5 %. El 0 % es solo cuando `subtotalVenta < 200`.

El IVA se calcula sobre el monto **ya descontado**, no sobre el subtotal original.

## 8. Errores frecuentes al abrirlo en NetBeans

| Qué se ve | Qué hacer |
| --- | --- |
| No aparece el botón Run | Se abrió el archivo, no el proyecto. **File → Open Project** y elegir la carpeta `SistemaVentas` |
| “Project folder is not a valid NetBeans project” | La carpeta elegida no es la que contiene `nbproject`. Baja un nivel y elige `SistemaVentas` |
| Aviso de plataforma Java | Clic derecho en el proyecto → **Properties → Libraries**. En **Java Platform** elige el JDK instalado |
| “cannot find symbol” o error en `package` | La primera línea debe ser `package sistemaventas;` y el archivo debe seguir en `src/sistemaventas/` |
| El programa no lee `10.50` | Escribir el punto. La coma `10,50` no sirve en esta configuración |
| La caja sigue en cero después de vender | Los tres acumuladores (`ventasRealizadas`, `productosVendidos`, `totalVendido`) se movieron o quedaron dentro del `for` |
| Al salir con N el programa se cierra | El `while` final debe ser `while (!confirmarSalida)`, no `while (opcion != 4)` |

Si NetBeans avisa que va a regenerar `nbproject/build-impl.xml`, se acepta. Es normal la primera vez que el proyecto se abre en otra computadora. No hay que editar ese archivo.

## 9. Modificarlo en la defensa

Los cambios se hacen solo en `SistemaVentas.java`. La guía `GUIA-PASO-A-PASO.md`, al final, trae las siete modificaciones en sintaxis Java:

- cambiar los porcentajes `0.05`, `0.08` y `0.10` (el IVA `0.12` se queda)
- agregar una categoría, por ejemplo 15 % desde Q1500
- agregar la forma de pago Cheque (opción 4)
- contar ventas con total mayor a Q500
- contar ventas con descuento (`porcentaje > 0`)
- cambiar una validación, por ejemplo el `while (precio <= 0)`
- agregar la opción 5 al menú

Para la opción 5 hay que cambiar **las dos** condiciones `opcion > 4` (el `while` y el `if`) a `opcion > 5`, agregar el texto en el menú y un `case 5`. La salida sigue dependiendo de `confirmarSalida`.

Después de cada cambio: guardar (Ctrl+S) y volver a pulsar F6. NetBeans compila solo.
