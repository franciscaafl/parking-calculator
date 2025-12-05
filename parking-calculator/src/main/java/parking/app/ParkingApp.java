package parking.app;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import parking.model.ParkingRateCalculator;
import parking.model.ParkingTicket;
import parking.model.TicketStatus;
import parking.model.VehicleType;
import parking.service.ParkingLotService;

public class ParkingApp {

    private final ParkingLotService service;
    private final Scanner scanner = new Scanner(System.in);

    public ParkingApp() {
        this.service = new ParkingLotService(new ParkingRateCalculator());
    }

    public static void main(String[] args) {
        new ParkingApp().run();
    }

    public void run() {
        while (true) {
            printMenu();
            try {
                int option = readInt("Elige una opción: ");
                switch (option) {
                    case 1 -> registerEntry();
                    case 2 -> registerExit();
                    case 3 -> listOpenTickets();
                    case 4 -> listClosedTickets();
                    case 5 -> showTicketDetails();
                    case 6 -> showDailyTotal();
                    case 7 -> exitProgram();
                    default -> System.out.println("Opción inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Debes ingresar un número válido.");
            } catch (Exception e) {
                System.out.println("Error inesperado: " + e.getMessage());
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=== SISTEMA DE ESTACIONAMIENTO ===");
        System.out.println("1. Registrar entrada de vehículo");
        System.out.println("2. Registrar salida de vehículo");
        System.out.println("3. Listar tickets abiertos");
        System.out.println("4. Listar tickets cerrados");
        System.out.println("5. Mostrar detalle de un ticket");
        System.out.println("6. Mostrar total recaudado hoy");
        System.out.println("7. Salir");
    }

    private int readInt(String msg) {
        System.out.print(msg);
        return Integer.parseInt(scanner.nextLine());
    }

    private void registerEntry() {
        System.out.print("Ingresa la patente: ");
        String plate = scanner.nextLine();

        VehicleType type = null;
        while (type == null) {
            System.out.print("Tipo de vehículo (AUTO / MOTO / CAMIONETA): ");
            String input = scanner.nextLine().toUpperCase();
            try {
                type = VehicleType.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Tipo inválido. Por favor intente nuevamente.");
            }
        }

        ParkingTicket t = service.registerEntry(plate, type);
        System.out.println("Ticket creado con ID: " + t.getId());
    }

    private void registerExit() {
        long id = readInt("Ingresa el ID del ticket: ");
        try {
            ParkingTicket t = service.registerExit(id);
            System.out.println("Ticket cerrado.");
            System.out.println("Monto cobrado: $" + t.getAmountCharged());
        } catch (Exception e) {
            System.out.println("Error al cerrar ticket: " + e.getMessage());
        }
    }

    private void listOpenTickets() {
        List<ParkingTicket> open = service.listOpenTickets();
        if (open.isEmpty()) {
            System.out.println("No hay tickets abiertos.");
            return;
        }
        System.out.println("--- Tickets Abiertos ---");
        open.forEach(t -> System.out.println(
                "ID " + t.getId() + " | Patente " + t.getLicensePlate() +
                        " | Entrada " + t.getEntryTime()
        ));
    }

    private void listClosedTickets() {
        List<ParkingTicket> closed = service.listClosedTickets();
        if (closed.isEmpty()) {
            System.out.println("No hay tickets cerrados.");
            return;
        }
        System.out.println("--- Tickets Cerrados ---");
        closed.forEach(t -> System.out.println(
                "ID " + t.getId() + " | Patente " + t.getLicensePlate() +
                        " | Monto $" + t.getAmountCharged()
        ));
    }

    private void showTicketDetails() {
        long id = readInt("Ingresa el ID del ticket: ");
        try {
            ParkingTicket t = service.getTicket(id);
            System.out.println("\n--- Detalle del Ticket ---");
            System.out.println("ID: " + t.getId());
            System.out.println("Patente: " + t.getLicensePlate());
            System.out.println("Tipo: " + t.getVehicleType());
            System.out.println("Entrada: " + t.getEntryTime());

            if (t.getStatus() == TicketStatus.OPEN) {
                System.out.println("Estado: ABIERTO");
            } else {
                System.out.println("Salida: " + t.getExitTime());
                System.out.println("Monto cobrado: $" + t.getAmountCharged());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void showDailyTotal() {
        long total = service.getDailyTotal(LocalDateTime.now());
        System.out.println("Total recaudado hoy: $" + total);
    }

    private void exitProgram() {
        System.out.println("¡Adiós!");
        System.exit(0);
    }
}