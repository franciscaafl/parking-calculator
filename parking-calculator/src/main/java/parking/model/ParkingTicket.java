package parking.model;

import java.time.LocalDateTime;

public class ParkingTicket {

    private final long id;
    private final String licensePlate;
    private final VehicleType vehicleType;
    private final LocalDateTime entryTime;

    private LocalDateTime exitTime;
    private Long amountCharged;
    private TicketStatus status;

    public ParkingTicket(long id, String licensePlate, VehicleType vehicleType, LocalDateTime entryTime) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.vehicleType = vehicleType;
        this.entryTime = entryTime;
        this.status = TicketStatus.OPEN;
    }

    public long getId() {
        return id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public Long getAmountCharged() {
        return amountCharged;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void close(LocalDateTime exitTime, long amountCharged) {
        if (status == TicketStatus.CLOSED) {
            throw new IllegalStateException("El ticket ya está cerrado");
        }
        this.exitTime = exitTime;
        this.amountCharged = amountCharged;
        this.status = TicketStatus.CLOSED;
    }
}
