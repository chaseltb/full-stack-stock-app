package learn.lavadonut.domain;

import learn.lavadonut.data.CurrencyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class CurrencyServiceTest {

    @Autowired
    CurrencyService service;

    @MockBean
    CurrencyRepository repository;

    @Test
    void shouldFindAll(){
        
    }
}