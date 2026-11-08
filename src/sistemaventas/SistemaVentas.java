package sistemaventas;

import java.util.Locale;
import java.util.Scanner;

/**
 * Sistema de Ventas y Facturacion Basica.
 * Proyecto final de Algoritmos. Proyecto NetBeans: SistemaVentas.
 *
 * Descuento sobre el subtotal de la venta, antes del IVA:
 *   menor de 200     -> 0%
 *   200 a 499.99     -> 5%   (0.05)
 *   500 a 999.99     -> 8%   (0.08)
 *   1000 o mas       -> 10%  (0.10)
 *
 * descuento         = subtotalVenta * porcentaje
 * montoConDescuento = subtotalVenta - descuento
 * iva               = montoConDescuento * 0.12
 * total             = montoConDescuento + iva
 * cambio            = efectivo - total
 *
 * Los decimales se escriben con punto: 10.50
 * Prueba: precio 200 y cantidad 1 -> descuento 10.00, IVA 22.80, total 212.80
 */
public class SistemaVentas {

    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        entrada.useLocale(Locale.US);

        // Datos de la venta que se esta registrando ahora.
        int cantidadProductos;
        int i;
        int cantidad;
        int unidadesVenta;
        int formaPago;
        int opcion;

        String codigo;
        String nombre;
        String nombrePago;
        String confirmar;

        double precio;
        double subtotalProducto;
        double subtotalVenta;
        double porcentaje;
        double descuento;
        double montoConDescuento;
        double iva;
        double total;
        double efectivo;
        double cambio;

        // Caja y estadisticas. Se crean una vez y no se reinician al volver al menu.
        int ventasRealizadas = 0;
        int productosVendidos = 0;
        int ventasEfectivo = 0;
        int ventasTarjeta = 0;
        int ventasTransferencia = 0;
        double totalVendido = 0;
        double ventaMayor = 0;
        double ventaMenor = 0;
        double promedio;
        boolean hayVentas = false;
        boolean confirmarSalida = false;

        // do-while del menu. Termina solo cuando confirmarSalida pasa a true.
        do {
            // while: la opcion tiene que ser 1, 2, 3 o 4.
            opcion = 0;
            while (opcion < 1 || opcion > 4) {
                System.out.println("================================");
                System.out.println("   SISTEMA DE VENTAS Y CAJA");
                System.out.println("================================");
                System.out.println("1. Registrar una venta");
                System.out.println("2. Consultar caja");
                System.out.println("3. Mostrar estadisticas");
                System.out.println("4. Salir");
                System.out.print("Seleccione una opcion: ");

                if (entrada.hasNextInt()) {
                    opcion = entrada.nextInt();
                    entrada.nextLine();
                } else {
                    entrada.nextLine();
                    opcion = 0;
                }

                if (opcion < 1 || opcion > 4) {
                    System.out.println("Opcion no valida. Elija 1, 2, 3 o 4.");
                    System.out.println("Presione Enter para continuar...");
                    entrada.nextLine();
                }
            }

            switch (opcion) {
                case 1:
                    // Cuantos renglones tendra la factura.
                    cantidadProductos = 0;
                    while (cantidadProductos <= 0) {
                        System.out.print("Cuantos productos tendra la compra?: ");
                        if (entrada.hasNextInt()) {
                            cantidadProductos = entrada.nextInt();
                            entrada.nextLine();
                        } else {
                            entrada.nextLine();
                            cantidadProductos = 0;
                        }
                        if (cantidadProductos <= 0) {
                            System.out.println("El numero de productos debe ser mayor que cero.");
                        }
                    }

                    // Estos dos acumuladores son de ESTA venta. Empiezan en 0 antes del for.
                    subtotalVenta = 0;
                    unidadesVenta = 0;

                    // for: un giro por cada producto.
                    for (i = 1; i <= cantidadProductos; i++) {
                        System.out.println("---------- Producto " + i + " ----------");

                        codigo = "";
                        while (codigo.equals("")) {
                            System.out.print("Codigo: ");
                            codigo = entrada.nextLine().trim();
                            if (codigo.equals("")) {
                                System.out.println("El codigo no puede estar vacio.");
                            }
                        }

                        nombre = "";
                        while (nombre.equals("")) {
                            System.out.print("Nombre: ");
                            nombre = entrada.nextLine().trim();
                            if (nombre.equals("")) {
                                System.out.println("El nombre no puede estar vacio.");
                            }
                        }

                        precio = 0;
                        while (precio <= 0) {
                            System.out.print("Precio: ");
                            if (entrada.hasNextDouble()) {
                                precio = entrada.nextDouble();
                                entrada.nextLine();
                            } else {
                                entrada.nextLine();
                                precio = 0;
                                System.out.println("Ingrese el precio con punto. Ejemplo: 10.50");
                                continue;
                            }
                            if (precio <= 0) {
                                System.out.println("El precio debe ser mayor que cero.");
                            }
                        }

                        cantidad = 0;
                        while (cantidad <= 0) {
                            System.out.print("Cantidad: ");
                            if (entrada.hasNextInt()) {
                                cantidad = entrada.nextInt();
                                entrada.nextLine();
                            } else {
                                entrada.nextLine();
                                cantidad = 0;
                                System.out.println("Ingrese una cantidad entera mayor que cero.");
                                continue;
                            }
                            if (cantidad <= 0) {
                                System.out.println("La cantidad debe ser mayor que cero.");
                            }
                        }

                        subtotalProducto = precio * cantidad;
                        subtotalVenta = subtotalVenta + subtotalProducto;
                        unidadesVenta = unidadesVenta + cantidad;
                        System.out.println("Codigo: " + codigo + "  Nombre: " + nombre);
                        System.out.printf("Subtotal del producto: Q%.2f%n", subtotalProducto);
                    }

                    // if / else: elige el porcentaje segun el subtotal de toda la venta.
                    if (subtotalVenta < 200) {
                        porcentaje = 0;
                    } else if (subtotalVenta < 500) {
                        porcentaje = 0.05;
                    } else if (subtotalVenta < 1000) {
                        porcentaje = 0.08;
                    } else {
                        porcentaje = 0.10;
                    }

                    descuento = subtotalVenta * porcentaje;
                    montoConDescuento = subtotalVenta - descuento;
                    iva = montoConDescuento * 0.12;
                    total = montoConDescuento + iva;

                    // while: la forma de pago tiene que ser 1, 2 o 3.
                    formaPago = 0;
                    while (formaPago < 1 || formaPago > 3) {
                        System.out.println("Forma de pago:");
                        System.out.println("1. Efectivo");
                        System.out.println("2. Tarjeta");
                        System.out.println("3. Transferencia");
                        System.out.print("Seleccione: ");
                        if (entrada.hasNextInt()) {
                            formaPago = entrada.nextInt();
                            entrada.nextLine();
                        } else {
                            entrada.nextLine();
                            formaPago = 0;
                        }
                        if (formaPago < 1 || formaPago > 3) {
                            System.out.println("Forma de pago no valida.");
                        }
                    }

                    efectivo = 0;
                    cambio = 0;
                    nombrePago = "";

                    // switch de la forma de pago.
                    switch (formaPago) {
                        case 1:
                            nombrePago = "Efectivo";
                            efectivo = 0;
                            // while: vuelve a pedir el dinero mientras no alcance.
                            while (efectivo < total) {
                                System.out.printf("Total a pagar: Q%.2f%n", total);
                                System.out.print("Monto recibido: ");
                                if (entrada.hasNextDouble()) {
                                    efectivo = entrada.nextDouble();
                                    entrada.nextLine();
                                } else {
                                    entrada.nextLine();
                                    efectivo = 0;
                                    System.out.println("Ingrese el monto con punto. Ejemplo: 200.00");
                                    continue;
                                }
                                if (efectivo < total) {
                                    System.out.printf("Efectivo insuficiente. Faltan Q%.2f%n", total - efectivo);
                                }
                            }
                            cambio = efectivo - total;
                            ventasEfectivo = ventasEfectivo + 1;
                            break;
                        case 2:
                            nombrePago = "Tarjeta";
                            ventasTarjeta = ventasTarjeta + 1;
                            break;
                        case 3:
                            nombrePago = "Transferencia";
                            ventasTransferencia = ventasTransferencia + 1;
                            break;
                        default:
                            break;
                    }

                    System.out.println("========== RESUMEN DE VENTA ==========");
                    System.out.println("Cantidad de productos: " + cantidadProductos);
                    System.out.println("Unidades vendidas: " + unidadesVenta);
                    System.out.printf("Subtotal: Q%.2f%n", subtotalVenta);
                    System.out.printf("Descuento: Q%.2f%n", descuento);
                    System.out.printf("Monto con descuento: Q%.2f%n", montoConDescuento);
                    System.out.printf("IVA (12%%): Q%.2f%n", iva);
                    System.out.printf("TOTAL: Q%.2f%n", total);
                    System.out.println("Forma de pago: " + nombrePago);
                    if (formaPago == 1) {
                        System.out.printf("Efectivo recibido: Q%.2f%n", efectivo);
                        System.out.printf("Cambio: Q%.2f%n", cambio);
                    }
                    System.out.println("======================================");

                    // Una vez por venta, despues del resumen. No van dentro del for.
                    ventasRealizadas = ventasRealizadas + 1;
                    productosVendidos = productosVendidos + unidadesVenta;
                    totalVendido = totalVendido + total;

                    // La primera venta es a la vez la mayor y la menor.
                    if (!hayVentas) {
                        ventaMayor = total;
                        ventaMenor = total;
                        hayVentas = true;
                    } else {
                        if (total > ventaMayor) {
                            ventaMayor = total;
                        }
                        if (total < ventaMenor) {
                            ventaMenor = total;
                        }
                    }
                    break;

                case 2:
                    System.out.println("========== CONSULTA DE CAJA ==========");
                    System.out.println("Ventas realizadas: " + ventasRealizadas);
                    System.out.println("Productos vendidos: " + productosVendidos);
                    System.out.printf("Total vendido: Q%.2f%n", totalVendido);
                    System.out.println("Ventas en efectivo: " + ventasEfectivo);
                    System.out.println("Ventas con tarjeta: " + ventasTarjeta);
                    System.out.println("Ventas por transferencia: " + ventasTransferencia);
                    System.out.println("======================================");
                    break;

                case 3:
                    System.out.println("========== ESTADISTICAS ==========");
                    if (ventasRealizadas == 0) {
                        System.out.println("No existen ventas registradas.");
                    } else {
                        promedio = totalVendido / ventasRealizadas;
                        System.out.println("Numero total de ventas: " + ventasRealizadas);
                        System.out.println("Cantidad total de productos vendidos: " + productosVendidos);
                        System.out.printf("Total de dinero vendido: Q%.2f%n", totalVendido);
                        System.out.printf("Promedio de dinero por venta: Q%.2f%n", promedio);
                        System.out.println("Ventas en efectivo: " + ventasEfectivo);
                        System.out.println("Ventas con tarjeta: " + ventasTarjeta);
                        System.out.println("Ventas por transferencia: " + ventasTransferencia);
                        System.out.printf("Venta de mayor valor: Q%.2f%n", ventaMayor);
                        System.out.printf("Venta de menor valor: Q%.2f%n", ventaMenor);
                    }
                    System.out.println("==================================");
                    break;

                case 4:
                    confirmar = "";
                    while (confirmar.equals("")) {
                        System.out.print("Confirma que desea salir? (S/N): ");
                        confirmar = entrada.nextLine().trim();
                    }
                    if (confirmar.equalsIgnoreCase("S")) {
                        confirmarSalida = true;
                        System.out.println("Gracias por usar el sistema. Hasta luego.");
                    } else {
                        System.out.println("Salida cancelada. Volviendo al menu.");
                    }
                    break;

                default:
                    break;
            }

            if (!confirmarSalida) {
                System.out.println("Presione Enter para continuar...");
                entrada.nextLine();
            }
        } while (!confirmarSalida);

        entrada.close();
    }
}
