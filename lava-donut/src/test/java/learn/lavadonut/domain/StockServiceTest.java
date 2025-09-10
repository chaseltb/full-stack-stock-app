package learn.lavadonut.domain;

import learn.lavadonut.data.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class StockServiceTest {

    @Autowired
    StockService service;

    @MockBean
    StockRepository repository;
    
}