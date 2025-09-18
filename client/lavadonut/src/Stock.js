import { useState, useEffect } from "react"; 
import React from "react"; 
import { Container, Box, Typography, Paper, Button, CircularProgress } from "@mui/material"; 
import { Link } from "react-router-dom";

function Stocks() {
    const [stocks, setStocks] = useState([]);  // State to hold the list of stocks
    const [orders, setOrders] = useState({});   // State to hold orders for each stock (by stock ID)
    const [ordersVisible, setOrdersVisible] = useState({});  // State to track visibility of orders for each stock
    const [error, setError] = useState("");    // Error state
    const [loading, setLoading] = useState(true);  // Loading state
    const [loadingOrders, setLoadingOrders] = useState(false);  // Loading state for orders
    const url = "http://localhost:8080/api/";  // Backend URL
    const token = localStorage.getItem("token") || sessionStorage.getItem("token");
    // Fetch the list of stocks when the component mounts
    useEffect(() => {
        const fetchStocksData = async () => {
            setLoading(true); // Start loading
            try {
                const token = localStorage.getItem('token') || sessionStorage.getItem('token');
                const response = await fetch(`${url}stocks`, {  // Fetching all stocks
                    method: "GET",
                    headers: {
                        "Authorization": `Bearer ${token}`,
                    }
                });

                if (!response.ok) {
                    throw new Error("Failed to load stocks data.");
                }

                const data = await response.json();
                setStocks(data);  // Set the stocks data
            } catch (error) {
                setError(error.message);
            } finally {
                setLoading(false); // Stop loading
            }
        };

        fetchStocksData();
    }, []);

    // Fetch orders for a specific stock
    const loadOrders = async (stockId) => {
        if (orders[stockId]) return; // Prevent fetching orders if they are already loaded for this stock
        setLoadingOrders(true); // Start loading orders for this stock
        setError(""); // Reset previous errors

        try {
            const token = localStorage.getItem('token') || sessionStorage.getItem('token');
            const response = await fetch(`${url}order/stock/${stockId}`, {
                method: "GET",
                headers: {
                    "Authorization": `Bearer ${token}`,
                }
            });

            if (!response.ok) {
                throw new Error("Failed to load orders.");
            }

            const data = await response.json();
            setOrders((prevOrders) => ({
                ...prevOrders,
                [stockId]: data,  // Store orders by stockId
            }));
        } catch (error) {
            setError(error.message);
        } finally {
            setLoadingOrders(false); // Stop loading orders for this stock
        }
    };

    // Toggle visibility of orders for a specific stock
    const toggleOrdersVisibility = (stockId) => {
        setOrdersVisible((prevState) => ({
            ...prevState,
            [stockId]: !prevState[stockId],  // Toggle the visibility flag
        }));
    };

    // Calculate total shares from orders
    const totalShares = (stockId) => {
        const stockOrders = orders[stockId] || [];
        return stockOrders.reduce((acc, order) => {
            if (order.transactionType === "BUY") return acc + order.numberOfShares;
            if (order.transactionType === "SELL") return acc - order.numberOfShares;
            return acc;
        }, 0);
    };

    if (loading) {
        return <CircularProgress />;  // Show loading spinner while fetching data
    }

    if (error) {
        return (
            <Container maxWidth="lg" sx={{ textAlign: "center", py: 4 }}>
                <Paper elevation={4} sx={{ padding: 4, borderRadius: 10, maxWidth: 600, margin: "auto" }}>
                    <Box sx={{ mb: 4 }}>
                        <Typography variant="h3" sx={{ mb: 2, color: "text.primary" }}>
                            Error: {error}
                        </Typography>
                    </Box>
                </Paper>
            </Container>
        );
    }

    return (
        <Container maxWidth="lg">
            <Paper elevation={4} sx={{ mt: 4, p: 4, borderRadius: 10 }}>
                <Typography variant="h3" sx={{ mb: 2, color: "text.primary" }}>
                    Stocks List
                </Typography>

                {/* Stocks list */}
                {stocks.length === 0 ? (
                    <Box sx={{ textAlign: "center" }}>
                        <Typography variant="h4" sx={{ mb: 2 }}>No stocks available.</Typography>
                        <Button variant="contained" component={Link} to="/stock/add" sx={{ p: 2, borderRadius: 4 }}>
                            Add a Stock
                        </Button>
                    </Box>
                ) : (
                    stocks.map((stock) => (
                        <Paper key={stock.id} elevation={2} sx={{ mb: 2, p: 2, borderRadius: 6 }}>
                            <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                                <Box>
                                    <Typography variant="h4" sx={{ mb: 1 }}>
                                        {stock.name} ({stock.ticker})
                                    </Typography>
                                    <Typography variant="body1">
                                        Total Shares: {totalShares(stock.id)}
                                    </Typography>
                                </Box>

                                {/* Button to toggle orders visibility */}
                                <Button
                                    variant="contained"
                                    sx={{ borderRadius: 5 }}
                                    onClick={() => {
                                        loadOrders(stock.id);  // Load orders if not already loaded
                                        toggleOrdersVisibility(stock.id);  // Toggle visibility of orders
                                    }}
                                >
                                    {ordersVisible[stock.id] ? "Hide Orders" : "View Orders"}
                                </Button>
                            </Box>

                            {/* Show orders for the stock if orders are visible */}
                            {ordersVisible[stock.id] && orders[stock.id] && (
                                <Box sx={{ mt: 2 }}>
                                    {orders[stock.id].map((order) => (
                                        <Paper key={order.id} elevation={2} sx={{ mb: 2, p: 2, borderRadius: 6 }}>
                                            <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                                                <Box>
                                                    <Typography variant="h4" color={order.transactionType === "BUY" ? "success.main" : "error.main"}>
                                                        {order.transactionType}: {order.date}
                                                    </Typography>
                                                    <Typography variant="body1">Shares: {order.shares}</Typography>
                                                    <Typography variant="body1">Price: ${order.price}</Typography>
                                                </Box>
                                            </Box>
                                        </Paper>
                                    ))}
                                </Box>
                            )}
                        </Paper>
                    ))
                )}
            </Paper>
        </Container>
    );
}

export default Stocks;
