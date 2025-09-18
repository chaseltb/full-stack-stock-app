import { useState, useEffect } from "react";
import {
  Container,
  Box,
  Typography,
  Paper,
  Alert,
  Button,
  MenuItem,
  TextField,
  Dialog,
  DialogTitle,
  DialogActions,
  DialogContent

} from "@mui/material";
import { Link } from "react-router-dom";
import React from "react";

// Insert Orderhistory, pass portfolio id to Order history

function Portfolios() {
  const [portfolios, setPortfolios] = useState([]);
  const [error, setError] = useState("");
  const [errors, setErrors] = useState([]);
  const [portfolioDetails, setPortfolioDetails] = useState({});
  const [newPortfolio, setNewPortfolio] = useState({ name: "", type: "RETIREMENT" });
  const [openDialog, setOpenDialog] = useState(false);
  const token = localStorage.getItem("token") || sessionStorage.getItem("token");
  const userId = localStorage.getItem("userId") || sessionStorage.getItem("token");
  const url = "http://localhost:8080/api/portfolio";

  const accountTypes = [
    { value: "RETIREMENT", label: "Retirement" },
    { value: "INVESTMENT", label: "Investment" },
    { value: "ROTH_IRA", label: "Roth IRA" },
  ];

  useEffect(() => {
    loadPortfolios();
  }, []);

  useEffect(() => {
  if (portfolios.length > 0) {
    portfolios.forEach((p) => loadPortfolioDetails(p.id));
  }
}, [portfolios]);

  const loadPortfolios = async () => {
    setError("");
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
      if (!portfolioResponse.ok) {
        const errorData = await portfolioResponse.json();
        setError(errorData.message || "Failed to load portfolios");
        return;
      }
      const data = await portfolioResponse.json();
      console.log(data);
      if (Array.isArray(data)) {
        setPortfolios(data);
      } else if (Array.isArray(data.portfolios)) {
        setPortfolios(data.portfolios);
      } else {
        setPortfolios([]);
        console.warn("Unexpected portfolios response:", data);
      }
    } catch (error) {
      setError(error.message || "Portfolios failed to load");
    }
  };

  const loadPortfolioDetails = async (portfolioId) => {
    try {
      const date = new Date().toISOString().split("T")[0];
      const [valueResponse, sharesResponse] = await Promise.all([
        fetch(`${url}/${portfolioId}/value?date=${date}`, {
          headers: { Authorization: `Bearer ${token}` },
        }),
        fetch(`${url}/${portfolioId}/shares?date=${date}`, {
          headers: { Authorization: `Bearer ${token}` },
        }),
      ]);

      let value = null;
      let totalShares = null;

      if (valueResponse.ok) {
        value = await valueResponse.json();
      } else {
        console.warn(`Failed to fetch value for portfolio ${portfolioId}`);
      }

      if (sharesResponse.ok) {
        const sharesData = await sharesResponse.json();

        if (!Array.isArray(sharesData) && typeof sharesData === "object") {
          totalShares = Object.values(sharesData)
            .map((n) => Number(n))
            .reduce((acc, val) => acc + val, 0);
        } else if (Array.isArray(sharesData)) {
          totalShares = sharesData
            .map((entry) => Number(entry.shares))
            .reduce((acc, val) => acc + val, 0);
        }
      } else {
        console.warn(`Failed to fetch shares for portfolio ${portfolioId}`);
      }

      setPortfolioDetails((prev) => ({
        ...prev,
        [portfolioId]: { value, totalShares },
      }));

      console.log(`${portfolioId}: ${value} + ${totalShares}`);
    } catch (err) {
      console.error(`Error fetching details for portfolio ${portfolioId}:`, err);
    }
  };

  const handleCreatePortfolio = async () => {
    setErrors([]);
    try {
      const payload = {
        userId: parseInt(userId, 10),
        accountType: newPortfolio.type, // enum constant
        name: newPortfolio.name,
      };
      const response = await fetch(`${url}/${userId}`, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        const errorData = await response.json();
        console.log(errorData);
        if (Array.isArray(errorData)) {
          setErrors(errorData);
        } else if (errorData != null && errorData.message != null) {
          setErrors([errorData.message]);
        } else {
          setErrors(["Failed to create portfolio"]);
        }
        return;
      }

      const created = await response.json();
      setPortfolios((prev) => [...prev, created]);
      setNewPortfolio({ name: "", type: "RETIREMENT" });
      setOpenDialog(false);
      loadPortfolioDetails(created.id);
    } catch (error) {
      console.log(`catch error: ${error}`);
      setError(error.message || "Failed to create portfolio");
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
          <Button variant="contained" sx={{ borderRadius: 5 }} onClick={() => setOpenDialog(true)}>
            Add Portfolio
          </Button>
        </Box>

        {errors.length > 0 && (
          <Box sx={{ mb: 2 }}>
            {errors.map((msg, idx) => (
              <Alert severity="error" key={idx} sx={{ mb: 1 }}>
                {msg}
              </Alert>
            ))}
          </Box>
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
                <Typography variant="body1">
                  {accountTypes.find((t) => t.value === portfolio.accountType)?.label || portfolio.accountType}
                </Typography>
              </Box>

              <Box sx={{ mb: 4 }}>
                <Typography variant="body1">
                  Total Shares: {portfolioDetails[portfolio.id]?.totalShares ?? "—"}
                </Typography>
                <Typography variant="body1">
                  Total Value: {portfolioDetails[portfolio.id]?.value ?? "—"}
                </Typography>
              </Box>

              <Button
                component={Link}
                variant="outlined"
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
      {/* create portoflio dialog*/}
      <Dialog open={openDialog} onClose={() => setOpenDialog(false)}>
        <DialogTitle>Create Portfolio</DialogTitle>
        <DialogContent sx={{ display: "flex", flexDirection: "column", gap: 2, mt: 1 }}>

          {errors.length > 0 && (
            <Box sx={{ mb: 1 }}>
              {errors.map((msg, idx) => (
                <Typography key={idx} variant="body2" color="error">
                  {msg}
                </Typography>
              ))}
            </Box>
          )}

          <TextField
            label="Portfolio Name"
            value={newPortfolio.name}
            onChange={(e) => setNewPortfolio({ ...newPortfolio, name: e.target.value })}
            fullWidth
          />
          <TextField
            select
            label="Account Type"
            value={newPortfolio.type}
            onChange={(e) => setNewPortfolio({ ...newPortfolio, type: e.target.value })}
            fullWidth
          >
            {accountTypes.map((option) => (
              <MenuItem key={option.value} value={option.value}>
                {option.label}
              </MenuItem>
            ))}
          </TextField>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenDialog(false)}>Cancel</Button>
          <Button variant="contained" onClick={handleCreatePortfolio}>
            Save
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
}

export default Portfolios;
