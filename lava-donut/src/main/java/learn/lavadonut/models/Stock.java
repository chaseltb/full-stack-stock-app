package learn.lavadonut.models;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Stock {

    private int id;
    private String name;
    private String ticker;
    private AssetType assetType;
    private String industry;
    private BigDecimal currentPrice;
    private Country country;
    private StockExchange stockExchange;

    public StockExchange getStockExchange() {
        return stockExchange;
    }

    public void setStockExchange(StockExchange stockExchange) {
        this.stockExchange = stockExchange;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public AssetType getAssetType() {
        return assetType;
    }

    public void setAssetType(AssetType assetType) {
        this.assetType = assetType;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public BigDecimal getCurrentPrice() {return currentPrice; }

    public void setCurrentPrice(BigDecimal currentPrice){
        this.currentPrice = currentPrice.setScale(4, RoundingMode.HALF_EVEN).stripTrailingZeros();
    }

    public boolean equals(Stock stock){
        return (this.name.equalsIgnoreCase(stock.getName())
                && this.ticker.equalsIgnoreCase(stock.getTicker())
                && this.assetType == stock.getAssetType()
                && this.country.getId() == stock.getCountry().getId()
                && this.currentPrice.compareTo(stock.getCurrentPrice()) == 0
                && this.stockExchange.getId() == stock.getStockExchange().getId());
    }
}
