import { AppBar, Box, Button, Toolbar, Typography } from "@mui/material";
import { Link, useNavigate } from "react-router-dom";

function NavBar() {
    // State variables
    const navigate = useNavigate();
    const token = localStorage.getItem("token") || sessionStorage.getItem("token");

    // Handle logout
    const handleLogout = () => {
        localStorage.removeItem("token");
        sessionStorage.removeItem("token");
        navigate("/auth");
    }

    return(
        <>
            <AppBar position="static" color="primary" sx={{ borderRadius: 0 }}>
                <Toolbar>
                    <Typography variant="h6" component={Link} to="/" sx={{ flexGrow: 1, color: 'inherit', textDecoration: 'none' }}>
                        Stock App
                    </Typography>
                    <Box sx={{ display: 'flex', gap: 2 }}>
                        <Button color="inherit" component={Link} to="/">
                            Home
                        </Button>
                        <Button color="inherit" component={Link} to="/orders">
                            Orders
                        </Button>
                        <Button color="inherit" component={Link} to="/stock">
                            Stocks
                        </Button>
                        <Button color="inherit" component={Link} to="/portfolios">
                            Portfolios
                        </Button>
                        <Button color="inherit" component={Link} to="/portfolios/order-history/1">
                            Order History
                        </Button>
                        {!token ? (
                            <Button color="inherit" component={Link} to="/auth">
                                Login / Register
                            </Button>
                        ) : (
                            <Button color="inherit" onClick={handleLogout}>
                                Logout
                            </Button>
                        )}
                    </Box>
                </Toolbar>
            </AppBar>
        </>
    );
}

export default NavBar;