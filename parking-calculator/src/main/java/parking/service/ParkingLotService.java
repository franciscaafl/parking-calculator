package parking.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import parking.model.ParkingRateCalculator;
import parking.model.ParkingTicket;
import parking.model.TicketStatus;
import parking.model.VehicleType;

public class ParkingLotService {

    private final ParkingRateCalculator rateCalculator;
    private final Map<Long, ParkingTicket> tickets = new HashMap<>();
    private long nextId = 1;

    public ParkingLotService(ParkingRateCalculator rateCalculator) {
        this.rateCalculator = rateCalculator;
    }

    public ParkingTicket registerEntry(String licensePlate, VehicleType type) {
        return registerEntry(licensePlate, type, LocalDateTime.now());
    }

    public ParkingTicket registerEntry(String licensePlate, VehicleType type, LocalDateTime entryTime) {
        if (licensePlate == null || type == null || entryTime == null) {
            throw new IllegalArgumentException("Los argumentos no pueden ser nulos");
        }

        long id = nextId++;
        ParkingTicket ticket = new ParkingTicket(id, licensePlate, type, entryTime);
        tickets.put(id, ticket);
        return ticket;
    }

    public ParkingTicket registerExit(long ticketId) {
        return registerExit(ticketId, LocalDateTime.now());
    }

    public ParkingTicket registerExit(long ticketId, LocalDateTime exitTime) {
        ParkingTicket ticket = tickets.get(ticketId);

        if (ticket == null) {
            throw new IllegalArgumentException("El ticket no existe");
        }

        if (ticket.getStatus() == TicketStatus.CLOSED) {
            throw new IllegalStateException("El ticket ya está cerrado");
        }

        long amount = rateCalculator.calculateAmount(ticket.getEntryTime(), exitTime, ticket.getVehicleType());

        ticket.close(exitTime, amount);

        return ticket;
    }

    public List<ParkingTicket> listOpenTickets() {
        return tickets.values().stream()
                .filter(t -> t.getStatus() == TicketStatus.OPEN)
                .toList();
    }

    public List<ParkingTicket> listClosedTickets() {
        return tickets.values().stream()
                .filter(t -> t.getStatus() == TicketStatus.CLOSED)
                .toList();
    }

    public ParkingTicket getTicket(long id) {
        ParkingTicket t = tickets.get(id);
        if (t == null) {
            throw new IllegalArgumentException("El ticket no existe");
        }
        return t;
    }

    public long getDailyTotal(LocalDateTime day) {
        return tickets.values().stream()
                .filter(t -> t.getStatus() == TicketStatus.CLOSED)
                .filter(t -> t.getExitTime().toLocalDate().equals(day.toLocalDate()))
                .mapToLong(ParkingTicket::getAmountCharged)
                .sum();
    }
}