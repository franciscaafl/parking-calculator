package parking;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import parking.model.ParkingTicket;
import parking.model.TicketStatus;
import parking.model.VehicleType;

public class ParkingTicketTest {

    @Test
    void newTicket_shouldStartOpen() {
        ParkingTicket t = new ParkingTicket(1, "ABC123", VehicleType.AUTO,
                LocalDateTime.of(2025, 12, 1, 10, 0));

        assertEquals(TicketStatus.OPEN, t.getStatus());
        assertNull(t.getExitTime());
        assertNull(t.getAmountCharged());
    }

    @Test
    void closeTicket_shouldSetExitTimeAmountAndStatus() {
        ParkingTicket t = new ParkingTicket(1, "ABC123", VehicleType.AUTO,
                LocalDateTime.of(2025, 12, 1, 10, 0));

        LocalDateTime exit = LocalDateTime.of(2025, 12, 1, 11, 0);

        t.close(exit, 1600);

        assertEquals(TicketStatus.CLOSED, t.getStatus());
        assertEquals(exit, t.getExitTime());
        assertEquals(1600, t.getAmountCharged());
    }
    
    @Test
    void closingAlreadyClosedTicket_shouldThrow() {
        ParkingTicket t = new ParkingTicket(1, "ABC123", VehicleType.AUTO,
                LocalDateTime.of(2025, 12, 1, 10, 0));
        
        t.close(LocalDateTime.now(), 1000);

        assertThrows(IllegalStateException.class, () ->
            t.close(LocalDateTime.now().plusHours(1), 2000)
        );
    }
}