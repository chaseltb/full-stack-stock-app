package learn.lavadonut.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class KnownGoodState {

    @Autowired
    JdbcTemplate jdbcTemplate;

    static boolean hasRun = false;

    void set() {
        if (!hasRun) {
            hasRun = true;

            jdbcTemplate.update("DELETE FROM portfolio_orders WHERE portfolio_id IN (SELECT portfolio_id FROM portfolio WHERE user_id IN (SELECT user_id FROM user WHERE app_user_id = (SELECT app_user_id FROM app_user WHERE username = 'john@smith.com')))");
            jdbcTemplate.update("DELETE FROM portfolio WHERE user_id IN (SELECT user_id FROM user WHERE app_user_id = (SELECT app_user_id FROM app_user WHERE username = 'john@smith.com'))");
            jdbcTemplate.update("DELETE FROM user WHERE app_user_id = (SELECT app_user_id FROM app_user WHERE username = 'john@smith.com')");
            jdbcTemplate.update("DELETE FROM app_user WHERE username = 'john@smith.com'");

            // Repeat the above process for 'sally@jones.com'
            jdbcTemplate.update("DELETE FROM portfolio_orders WHERE portfolio_id IN (SELECT portfolio_id FROM portfolio WHERE user_id IN (SELECT user_id FROM user WHERE app_user_id = (SELECT app_user_id FROM app_user WHERE username = 'sally@jones.com')))");
            jdbcTemplate.update("DELETE FROM portfolio WHERE user_id IN (SELECT user_id FROM user WHERE app_user_id = (SELECT app_user_id FROM app_user WHERE username = 'sally@jones.com'))");
            jdbcTemplate.update("DELETE FROM user WHERE app_user_id = (SELECT app_user_id FROM app_user WHERE username = 'sally@jones.com')");
            jdbcTemplate.update("DELETE FROM app_user WHERE username = 'sally@jones.com'");


            jdbcTemplate.update("call set_known_good_state();");
        }
    }
}
