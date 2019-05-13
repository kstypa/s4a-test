package pl.parser.nbp;

public class Currency {
    private String name;
    private int multiplier;
    private String code;
    private double buyRate;
    private double sellRate;

    public Currency(String name, int multiplier, String code, double buyRate, double sellRate) {
        this.name = name;
        this.multiplier = multiplier;
        this.code = code;
        this.buyRate = buyRate;
        this.sellRate = sellRate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getBuyRate() {
        return buyRate;
    }

    public void setBuyRate(double buyRate) {
        this.buyRate = buyRate;
    }

    public double getSellRate() {
        return sellRate;
    }

    public void setSellRate(double sellRate) {
        this.sellRate = sellRate;
    }
}
