package RatelimitingAPI.Config;

public class RateLimitConfig {
    private String endpoint;
    private int replenishRate;
    private int burstCapacity;

    public RateLimitConfig(String endpoint, int replenishRate, int burstCapacity) {
        this.endpoint = endpoint;
        this.replenishRate = replenishRate;
        this.burstCapacity = burstCapacity;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public int getReplenishRate() {
        return replenishRate;
    }

    public int getBurstCapacity() {
        return burstCapacity;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setReplenishRate(int replenishRate) {
        this.replenishRate = replenishRate;
    }

    public void setBurstCapacity(int burstCapacity) {
        this.burstCapacity = burstCapacity;
    }
}
