package parking;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import parking.model.ParkingRateCalculator;
import parking.model.ParkingTicket;
import parking.model.TicketStatus;
import parking.model.VehicleType;
import parking.service.ParkingLotService;

public class ParkingLotServiceTest {

    private final ParkingRateCalculator calc = new ParkingRateCalculator();

    @Test
    void registerEntry_shouldCreateOpenTicket() {
        ParkingLotService service = new ParkingLotService(calc);

        ParkingTicket t = service.registerEntry("ABC123", VehicleType.AUTO);

        assertNotNull(t);
        assertEquals("ABC123", t.getLicensePlate());
        assertEquals(TicketStatus.OPEN, t.getStatus());
        assertEquals(1, t.getId());
    }

    @Test
    void registerExit_shouldCloseTicket() {
        ParkingLotService service = new ParkingLotService(calc);
        LocalDateTime entryTime = LocalDateTime.now().minusHours(1);
        ParkingTicket t = service.registerEntry("BBB222", VehicleType.AUTO, entryTime);
        ParkingTicket closed = service.registerExit(t.getId());

        assertEquals(TicketStatus.CLOSED, closed.getStatus());
        assertNotNull(closed.getAmountCharged());
    }

    @Test
    void exitNonexistentTicket_shouldThrow() {
        ParkingLotService service = new ParkingLotService(calc);

        assertThrows(IllegalArgumentException.class,
                () -> service.registerExit(999));
    }

    @Test
    void exitAlreadyClosedTicket_shouldThrow() {
        ParkingLotService service = new ParkingLotService(calc);

        LocalDateTime entryTime = LocalDateTime.now().minusHours(2);
        ParkingTicket t = service.registerEntry("CCC333", VehicleType.AUTO, entryTime);
        
        service.registerExit(t.getId());

        assertThrows(IllegalStateException.class,
                () -> service.registerExit(t.getId()));
    }

    @Test
    void getDailyTotal_shouldSumOnlyClosedTicketsFromSpecificDay() {
        ParkingLotService service = new ParkingLotService(calc);
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime yesterday = today.minusDays(1);
        ParkingTicket t1 = service.registerEntry("AAA-111", VehicleType.AUTO, today.minusHours(1));
        service.registerExit(t1.getId(), today); 

        ParkingTicket t2 = service.registerEntry("BBB-222", VehicleType.MOTO, yesterday.minusHours(1));
        service.registerExit(t2.getId(), yesterday);

        service.registerEntry("CCC-333", VehicleType.CAMIONETA, today.minusHours(1));

        long totalHoy = service.getDailyTotal(today);
        long totalAyer = service.getDailyTotal(yesterday);

        assertEquals(1600, totalHoy, "El total de hoy debería ser 1600");
        assertEquals(1000, totalAyer, "El total de ayer debería ser 1000");
    }

    @Test
    void listTickets_shouldFilterCorrectly() {
        ParkingLotService service = new ParkingLotService(calc);

        LocalDateTime entry = LocalDateTime.now().minusHours(1);
        ParkingTicket closedTicket = service.registerEntry("CERRADO", VehicleType.AUTO, entry);
        service.registerExit(closedTicket.getId());

        service.registerEntry("ABIERTO", VehicleType.AUTO);

        List<ParkingTicket> closed = service.listClosedTickets();
        List<ParkingTicket> open = service.listOpenTickets();

        assertEquals(1, closed.size(), "Debe haber 1 ticket cerrado");
        assertEquals(1, open.size(), "Debe haber 1 ticket abierto");
        
        assertEquals("CERRADO", closed.get(0).getLicensePlate());
        assertEquals("ABIERTO", open.get(0).getLicensePlate());
    }
}