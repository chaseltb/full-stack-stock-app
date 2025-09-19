import { useState, useEffect } from "react";
import React from "react";
import { jwtDecode } from "jwt-decode";
import { Container, Alert, Box, Paper, Typography, Button, TextField } from "@mui/material";
import { Link } from "react-router-dom";

function HomeScreen() {
    const [stocks, setStocks] = useState([]);
    const [watchlist, setWatchlist] = useState([]);
    const token = localStorage.getItem("token") || sessionStorage.getItem("token");
    const userId = localStorage.getItem("userId");

    const [error, setError] = useState("");
    const url = "http://localhost:8080/api/";

    useEffect(() => {
        loadStocks();
        loadWatchlist();
    }, []); // run once on page load

    
    const loadStocks = async () => {
        setError("");

        const mockStocks = [
                {
                    id: 1,
                    name: "Apple Inc.",
                    symbol: "AAPL",
                    shares: 25,
                    avgPrice: 145.67,
                    unrealizedGainOrLoss: 320.45
                },
                {
                    id: 2,
                    name: "Tesla Inc.",
                    symbol: "TSLA",
                    shares: 10,
                    avgPrice: 720.50,
                    unrealizedGainOrLoss: -150.30
                },
                {
                    id: 3,
                    name: "Amazon.com Inc.",
                    symbol: "AMZN",
                    shares: 5,
                    avgPrice: 3300.00,
                    unrealizedGainOrLoss: 1025.75
                }
            ];
            setStocks(mockStocks);

        try {
            // fetch user's stocks api
        } catch (error) {
            setError(error.message);
        }
    }

    const loadWatchlist = async () => {
        const mockWatchlist = [
                { name: "Microsoft", symbol: "MSFT", description: "Cloud, productivity, and software giant" },
                { name: "Nvidia", symbol: "NVDA", description: "Leader in GPUs and AI hardware" },
                { name: "Meta Platforms", symbol: "META", description: "Social media and metaverse company" }
            ];
            setWatchlist(mockWatchlist);
        try {
            // fetch user's watchlist (in portfolio?)
        } catch (error) {
            setError(error);
        }
    }

    return (
        <>
            <Container maxWidth="lg" sx={{ mt: 4}}>
                <Paper elevation={4} sx={{ mt: 4, p: 4, borderRadius: 10, bgcolor: 'primary.dark'}}>
                    <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", mb: 4}}>
                        <Typography variant="h2" sx={{color: 'primary.contrastText'}}>
                            Stock App
                        </Typography>
                        <Button component={Link} to="/stock" variant="contained" size = 'large' sx={{ borderRadius: 5, bgcolor: 'secondary.main', color: 'primary.contrastText'}}>
                            View Stocks
                        </Button>
                    </Box>
                </Paper>

                {error && <Alert severity="error" sx={{ mb: 4 }}>
                    {error}
                </Alert>}

                <Box sx={{ display: "flex", gap: 3, overflowX: "auto", pb: 2, mt: 3 }}>
                    {/* stocks mapped to individual paper elements (look like cards) */}
                    {stocks.map((stock) => (
                        <Paper key={stock.id} elevation={2} sx={{ p: 2, borderRadius: 6 }}>
                            <Box>
                                <Typography variant="h4">
                                    {stock.name}
                                </Typography>
                                <Typography variant="body1" color="textSecondary">
                                    {stock.symbol}
                                </Typography>
                            </Box>
                            <Box>
                                <Typography variant="body1">
                                    Total shares: {stock.shares}
                                </Typography>
                                <Typography variant="body1">
                                    Average Price: ${stock.avgPrice.toFixed(2)}
                                </Typography>
                                <Typography variant="body1" color={stock.unrealizedGainOrLoss >= 0 ? "success" : "error"}>
                                    Unrealized Gain or Loss: ${stock.unrealizedGainOrLoss.toFixed(2)}
                                </Typography>
                            </Box>
                            <Button component={Link} to="/orders" variant="contained" sx={{ borderRadius: 6 , bgcolor: 'primary.light'}}>
                                View Orders
                            </Button>
                        </Paper>
                    ))}
                </Box>

                {/* Watchlist shows a vertically scrolling list of stocks */}
                <Typography variant="h4" bgcolor={'primary.dark'} sx={{ mt: 5, mb: 5, p: 2, color: 'primary.contrastText'}}>
                    Watchlist
                </Typography>

                {watchlist.map((item, index) => (
                    <Paper key={index} elevation={2} sx={{ mb: 2, p: 2, borderRadius: 5, bgcolor: 'primary.main', color: 'primary.contrastText'}}>
                        <Typography variant="h4">
                            {item.name} ({item.symbol})
                        </Typography>
                        <Typography variant="body">
                            {item.description}
                        </Typography>
                    </Paper>
                ))}
            </Container>
        </>
    );
}

export default HomeScreen;