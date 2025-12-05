package parking;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import parking.model.ParkingRateCalculator;
import parking.model.VehicleType;

class ParkingRateCalculatorTest {

    private final ParkingRateCalculator calc = new ParkingRateCalculator();

    @Test
    void motorcycle_oneBlock_shouldBe500() {
        LocalDateTime entry = LocalDateTime.of(2025, 12, 1, 10, 0);
        LocalDateTime exit  = entry.plusMinutes(30);
        long amount = calc.calculateAmount(entry, exit, VehicleType.MOTO); 
        assertEquals(500, amount);
    }

    @Test
    void truck_oneBlock_shouldBe1000() {
        LocalDateTime entry = LocalDateTime.of(2025, 12, 1, 10, 0);
        LocalDateTime exit  = entry.plusMinutes(30);
        long amount = calc.calculateAmount(entry, exit, VehicleType.CAMIONETA);
        assertEquals(1000, amount); 
    }

    @Test
    void car_twoBlocks_shouldBe1600() {
        LocalDateTime entry = LocalDateTime.of(2025, 12, 1, 10, 0);
        LocalDateTime exit  = entry.plusMinutes(31);
        long amount = calc.calculateAmount(entry, exit, VehicleType.AUTO);
        assertEquals(1600, amount);
    }

    @Test
    void motorcycle_weekend_discount_applies() {
        LocalDateTime entry = LocalDateTime.of(2025, 12, 6, 8, 0);
        LocalDateTime exit  = entry.plusHours(1);
        long amount = calc.calculateAmount(entry, exit, VehicleType.MOTO);
        assertEquals(900, amount);
    }

    @Test
    void truck_dailyCap_applies() {
        LocalDateTime entry = LocalDateTime.of(2025, 12, 1, 0, 0);
        LocalDateTime exit  = entry.plusHours(24);
        long amount = calc.calculateAmount(entry, exit, VehicleType.CAMIONETA);

        assertEquals(15000, amount);
    }

    @Test
    void zeroDuration_shouldThrowException() {
        LocalDateTime entry = LocalDateTime.now();
        assertThrows(IllegalArgumentException.class,
            () -> calc.calculateAmount(entry, entry, VehicleType.AUTO));
    }
}