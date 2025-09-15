package learn.lavadonut.domain;

import learn.lavadonut.data.StockRepository;
import learn.lavadonut.models.Stock;
import net.jacobpeterson.alpaca.AlpacaAPI;
import net.jacobpeterson.alpaca.model.util.apitype.MarketDataWebsocketSourceType;
import net.jacobpeterson.alpaca.model.util.apitype.TraderAPIEndpointType;
import net.jacobpeterson.alpaca.openapi.marketdata.ApiException;
import net.jacobpeterson.alpaca.openapi.marketdata.model.StockFeed;
import net.jacobpeterson.alpaca.openapi.marketdata.model.StockTrade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@Service
public class AlpacaService {

//    @Value("${alpaca.api.endpoint")
//    private String alpacaEndpoint;

    @Value("${alpaca.api.key}")
    private String alpacaKey;

    @Value("${alpaca.api.secret}")
    private String alpacaSecret;

//    @Value("${alpaca.api.key}")
//    private String alpacaKey;
//
//    @Value("${alpaca.api.secret}")
//    private String alpacaSecret;

    @Value("${alpaca.api.endpoint}")
    private String alpacaEndpoint;

    private final StockRepository repo;
    private AlpacaAPI alpacaAPI;

    public AlpacaService(StockRepository repo) {
        this.repo = repo;
    }

    @PostConstruct
    private void init() {
        TraderAPIEndpointType type = alpacaEndpoint.contains("paper") ? TraderAPIEndpointType.PAPER : TraderAPIEndpointType.LIVE;
        this.alpacaAPI = new AlpacaAPI(
                alpacaKey,
                alpacaSecret,
                type,
                MarketDataWebsocketSourceType.IEX
        );
    }

    public Result<Stock> updateStockFromAlpaca(String ticker) {
        Result<Stock> result = new Result<>();
        Stock stock = repo.findByTicker(ticker);
        if (stock == null) {
            result.addMessage(String.format("Stock ticker %s is not found", ticker), ResultType.NOT_FOUND);
            return result;
        }

        final StockTrade latestTrade;
        try {
            latestTrade = alpacaAPI.marketData().stock().stockLatestTradeSingle(ticker, StockFeed.IEX, null).getTrade();
            stock.setCurrentPrice(BigDecimal.valueOf(latestTrade.getP()));
            if(repo.update(stock)) {
                result.setPayload(stock);
            } else {
                result.addMessage("Error updating stock", ResultType.INVALID);
            }
        } catch (ApiException e) {
            result.addMessage("Error fetching data from Alpaca: " + e.getMessage(), ResultType.INVALID);
        }

        return result;

    }
}
