import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Box, Typography, IconButton, Card, CardContent, CardActions, Stack, Button } from "@mui/material";
import { Add, Edit, Delete } from "@mui/icons-material";

function OrderList() {
    // State variables
    const [orders, setOrders] = useState([]);
    const [stocks, setStocks] = useState([]);
    const [loading, setLoading] = useState(true);
    const url = 'http://localhost:8080/api/order';

    // Use Effect
    useEffect(() => {
        async function fetchData() {
            const token = localStorage.getItem('token');
            try {
                const ordersRes = await fetch("http://localhost:8080/api/order", {
                    headers: {
                        "Authorization": `Bearer ${token}`,
                        "Content-Type": "application/json"
                    }
                });

                const stocksRes = await fetch("http://localhost:8080/api/stocks", {
                    headers: {
                        "Authorization": `Bearer ${token}`,
                        "Content-Type": "application/json"
                    }
                });

                if (!ordersRes.ok || !stocksRes.ok) {
                    throw new Error("Unauthorized or failed fetch");
                }

                const ordersData = await ordersRes.json();
                const stocksData = await stocksRes.json();

                setOrders(ordersData);
                setStocks(stocksData);
            } catch (err) {
                console.error("Error fetching data:", err);
            } finally {
                setLoading(false);
            }
        }

        fetchData();
    }, []);

    // Handle deleting an order
    const handleDeleteOrder = (id) => {
        const token = localStorage.getItem('token');
        const order = orders.find(o => o.id === id);
        if (window.confirm(`Delete Order: ${order.id}`)) {
            const init = {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            };
            fetch(`${url}/${id}`, init)
            .then(response => {
                if (response.status === 200) {
                    const newOrders = orders.filter(o => o.id !== id);
                    setOrders(newOrders);
                } else {
                    return Promise.reject(`Unexpected Status Code: ${response.status}`);
                }
            })
            .catch(console.log);
        }
    }

    if (loading) {
        return (
            <>
                <Typography>Loading...</Typography>
            </>
        );
    }

    return (
        <Box sx={{ padding: 4 }}>
            <Stack
                direction="row"
                justifyContent="space-between"
                alignItems="center"
                mb={4}
            >
                <Typography variant="h2" fontWeight="bold">
                    List of Orders ({orders.length})
                </Typography>
                <Button 
                    variant="contained" 
                    color="primary"
                    component={Link}
                    to={'order/add'}
                    startIcon={<Add />}
                >
                    Add an Order
                </Button>
            </Stack>
            <Stack spacing={3}>
                {orders.map((order) => (
                    <Card key={order.id} variant="outlined" sx={{ backgroundColor: "f4f4f4" }}>
                        <CardContent>
                            <Typography variant="h6" gutterBottom>
                                Order {order.id} - {
                                    // Getting stock information
                                    (() => {
                                        const stock = stocks.find(s => s.id === order.stockId);
                                        return stock ? `${stock.ticker} (${stock.name})` : `Stock ID ${order.stockId}`;
                                    })()
                                } 
                            </Typography>
                            <Typography variant="body2" color="textSecondary">
                                Transaction Type: {order.transactionType}
                            </Typography>
                            <Typography variant="body2" color="textSecondary">
                                Number of Shares: {order.numberOfShares}
                            </Typography>
                            <Typography variant="body2" color="textSecondary">
                                Date: {order.date}
                            </Typography>
                            <Typography variant="body2" color="textSecondary">
                                Price: ${order.price}
                            </Typography>
                        </CardContent>
                        <CardActions sx={{ justifyContent: "flex-end" }}>
                                <IconButton
                                    color="primary"
                                    component={Link}
                                    to={`/order/edit/${order.id}`}
                                >
                                    <Edit />
                                </IconButton>
                                <IconButton
                                    color="error"
                                    onClick={() => handleDeleteOrder(order.id)}
                                >
                                    <Delete />
                                </IconButton>
                            </CardActions>
                    </Card>
                ))}
            </Stack>
        </Box>
    );
}

export default OrderList;