import { useState, useEffect } from "react";
import React from "react";
import { Container, Box, Typography, Paper, Alert, Button, IconButton } from "@mui/material";
import { ArrowBack } from "@mui/icons-material";

function Stock() {
    const [orders, setOrders] = useState([]);
    const [error, setError] = useState("");

    const url = "http://localhost:8080/api/";

    useEffect(() => {
        if (Stock.id) {
            loadOrders();
        }
    }, [stock]);

    const loadOrders = async () => {
        setError("");

        try {
            // fetch orders of that stock from that user
        } catch (error) {
            setError(error);
        }
    };

    const handleAddOrder = () => {
        console.log("add order");
    }

    return (
        <Container maxWidth="lg">
            <Paper elevation={4} sx={{ mt: 4, p: 4, borderRadius: 10 }}>
                {/* header has a back button */}
                <Box sx={{ display: "flex", alignItems: "center", mb: 4 }}>
                    <IconButton onClick={onBack}>
                        <ArrowBack />
                    </IconButton>
                </Box>

                {error && <Alert severity="error" sx={{ mb: 4 }}>
                    {error}
                </Alert>}

                {/* info about the stock */}
                <Box sx={{ mb: 4 }}>
                    <Typography variant="h2" sx={{ mb: 2, mt: 2, mr: 2 }}>
                        // stock name
                    </Typography>
                    <Typography variant="h4">
                        // total shares
                    </Typography>
                </Box>

                {/* header for orders section  */}
                <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center", p: 2, mb: 4 }}>
                    <Typography variant="h2">
                        Orders
                    </Typography>
                    <Button variant="contained" onClick={handleAddOrder} sx={{ borderRadius: 4 }}>
                        Add Order
                    </Button>
                </Box>

                {/* orders list  */}
                {orders.length === 0 ? (
                    <Box sx={{ textAlign: "center" }}>
                        <Typography variant="h4" sx={{ mb: 2 }}>
                            Order history empty
                        </Typography>
                        <Button variant="contained" onClick={handleAddOrder} sx={{ p: 2, borderRadius: 4 }}>
                            Add an order
                        </Button>
                    </Box>
                ) : (
                    orders.map((order) => (
                        <Paper key={order.id} elevation={2} sx={{ mb: 2, p: 2, borderRadius: 6 }}>
                            <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                                <Box>
                                    <Typography variant="h4" color={order.type === "Buy" ? "success" : "error"}>
                                        {order.type}: {order.date}
                                    </Typography>
                                    <Typography variant="body1">
                                        Shares: {order.shares}
                                    </Typography>
                                    <Typography variant="body1">
                                        Average Price: ${order.averagePrice}
                                    </Typography>
                                </Box>
                                <Button variant="contained" sx={{ borderRadius: 5 }}>
                                    Edit
                                </Button>
                            </Box>
                        </Paper>
                    ))
                )}
            </Paper>

        </Container>
    );

}

export default Stock;