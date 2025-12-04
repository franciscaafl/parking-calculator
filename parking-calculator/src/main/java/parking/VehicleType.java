package parking;

public enum VehicleType {
    AUTO(800),
    MOTO(500),
    CAMIONETA(1000);

    private final long pricePerBlock;

    VehicleType(long pricePerBlock) {
        this.pricePerBlock = pricePerBlock;
    }

    public long getPricePerBlock() {
        return pricePerBlock;
    }
}
