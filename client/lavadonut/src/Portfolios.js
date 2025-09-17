import { useState, useEffect } from "react";
import {
  Container,
  Box,
  Typography,
  Paper,
  Alert,
  Button,
} from "@mui/material";
import { Link} from "react-router-dom";
import React from "react";

// Insert Orderhistory, pass portfolio id to Order history

function Portfolios() {
  const [portfolios, setPortfolios] = useState([]);
  const [error, setError] = useState("");
  const token = localStorage.getItem("token") || sessionStorage.getItem("token");
  const userId = localStorage.getItem("userId");
  const url = "http://localhost:8080/api/portfolio";

  useEffect(() => {
    loadPortfolios();
  }, []);

  const loadPortfolios = async () => {
    setError("");
    const token = localStorage.getItem("token");
    try {
      // fetch portfolios url
      const portfolioResponse = await fetch(
        `${url}/${userId}`, // needs to be provided userId
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );
      setPortfolios(portfolioResponse); 
    } catch (error) {
      setError(error.message || "Portfolios failed to load");
    }
  };

  //place button that has link to OrderHistory

  return (
    <Container maxWidth="lg">
      <Paper elevation={4} sx={{ mt: 4, p: 4, borderRadius: 10 }}>
        <Box
          sx={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            mb: 4,
          }}
        >
          <Typography variant="h2">Portfolios</Typography>
          <Button variant="contained" sx={{ borderRadius: 5 }}>
            Add Portfolio
          </Button>
        </Box>

        {error && (
          <Alert severity="error" sx={{ mb: 4 }}>
            {error}
          </Alert>
        )}

        {/* list of portfolios */}
        <Box sx={{ display: "grid", gap: 3 }}>
          {portfolios.map((portfolio) => (
            <Paper
              key={portfolio.id}
              elevation={2}
              sx={{ p: 2, borderRadius: 10 }}
            >
              <Box sx={{ p: 2, mb: 2, width: 1 / 3 }}>
                <Typography variant="body1">{portfolio.name}</Typography>
                <Typography variant="body1">{portfolio.type}</Typography>
              </Box>

              <Box sx={{ mb: 4 }}>
                <Typography variant="body1">Total Shares:</Typography>
                <Typography variant="body1">Total Value:</Typography>
                <Typography
                  variant="body1"
                  color={
                    portfolio.unrealizedGainOrLoss >= 0
                      ? "success.main"
                      : "error.main"
                  }
                >
                  Unrealized Gain/Loss:
                </Typography>
              </Box>

              <Button
                variant="outlined"
                component={Link}
                to={`/portfolios/order-history/${portfolio.id}`}
              >
                Order History
              </Button>
            </Paper>
          ))}
        </Box>

        {portfolios.length === 0 && (
          <Box sx={{ textAlign: "center" }}>
            <Typography variant="h4" sx={{ mb: 2 }}>
              You haven't added any portfolios
            </Typography>
            <Button variant="contained" sx={{ borderRadius: 4 }}>
              Create a portfolio
            </Button>
          </Box>
        )}
      </Paper>
    </Container>
  );
}

export default Portfolios;
