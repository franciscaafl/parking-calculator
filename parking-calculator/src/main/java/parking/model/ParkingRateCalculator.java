package parking.model;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;

public class ParkingRateCalculator {

    private static final long BLOCK_MINUTES = 30;
    private static final long DAILY_CAP = 15000;

    public long calculateAmount(LocalDateTime entryTime, LocalDateTime exitTime, VehicleType type) {

        if (entryTime == null || exitTime == null || type == null) {
            throw new IllegalArgumentException("Los argumentos no pueden ser nulos");
        }

        long minutes = Duration.between(entryTime, exitTime).toMinutes();

        if (minutes <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor a 0 minutos");
        }

        long blocks = (long) Math.ceil((double) minutes / BLOCK_MINUTES);
        long amount = blocks * type.getPricePerBlock();

        if (amount > DAILY_CAP) {
            amount = DAILY_CAP;
        }

        DayOfWeek day = entryTime.getDayOfWeek();
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
            amount = (long) Math.floor(amount * 0.9);
        }

        return amount;
    }
}