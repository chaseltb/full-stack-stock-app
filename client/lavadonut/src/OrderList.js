import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Box, Typography, IconButton, Card, CardContent, CardActions, Stack, Button } from "@mui/material";
import { Add, Edit, Delete } from "@mui/icons-material";

const STOCK_EXCHANGE_DATA = [
    {
        id: 1,
        name: 'New York Stock Exchange',
        code: 'NYSE',
        timezone: -5
    },
    {
        id: 2,
        name: 'Frankfurt Stock Exchange',
        code: 'XETR',
        timezone: 1
    },
    {
        id: 3,
        name: 'Shanghai Stock Exchange',
        code: 'SSE',
        timezone: 8
    }
];

const CURRENCY_DATA = [
    {
        id: 1,
        name: 'United States Dollar',
        code: 'USD',
        valueToUsd: 1.0
    },
    {
        id: 2,
        name: 'Euro',
        code: 'EUR',
        valueToUsd: 1.17
    },
    {
        id: 3,
        name: 'Chinese Yuan',
        code: 'CNY',
        valueToUsd: 0.14
    }
];

const COUNTRY_DATA = [
    {
        id: 1,
        name: 'United States of America',
        code: 'US',
        currency: CURRENCY_DATA[0]
    },
    {
        id: 2,
        name: 'Federal Republic of Germany',
        code: 'DE',
        currency: CURRENCY_DATA[1]
    },
    {
        id: 3,
        name: 'People\'s Republic of China',
        code: 'CN',
        currency: CURRENCY_DATA[2]
    }
];

const STOCK_DATA = [
    {
        id: 1,
        name: 'AMERICAN AIRLINES GROUP INC',
        ticker: 'TEST-TICKER1',
        assetType: 'STOCK',
        industry: 'Airline and Aviation',
        currentPrice: 12.915,
        country: COUNTRY_DATA[0],
        stockExchange: STOCK_EXCHANGE_DATA[0]
    },
    {
        id: 2,
        name: 'AMERICAN TEST STOCK 1',
        ticker: 'TEST-TICKER2',
        assetType: 'ETF',
        industry: 'Agriculture',
        currentPrice: 5.0,
        country: COUNTRY_DATA[0],
        stockExchange: STOCK_EXCHANGE_DATA[0]
    },
    {
        id: 3,
        name: 'AMERICAN TEST STOCK 2',
        ticker: 'TEST-TICKER3',
        assetType: 'BOND',
        industry: 'Technology',
        currentPrice: 6.8,
        country: COUNTRY_DATA[0],
        stockExchange: STOCK_EXCHANGE_DATA[0]
    },
    {
        id: 4,
        name: 'GERMAN TEST STOCK 1',
        ticker: 'TEST-TICKER4',
        assetType: 'STOCK',
        industry: 'Airline and Aviation',
        currentPrice: 9.6,
        country: COUNTRY_DATA[1],
        stockExchange: STOCK_EXCHANGE_DATA[1]
    },
    {
        id: 5,
        name: 'GERMAN TEST STOCK 2',
        ticker: 'TEST-TICKER5',
        assetType: 'ETF',
        industry: 'Agriculture',
        currentPrice: 78.5,
        country: COUNTRY_DATA[1],
        stockExchange: STOCK_EXCHANGE_DATA[1]
    },
    {
        id: 6,
        name: 'GERMAN TEST STOCK 3',
        ticker: 'TEST-TICKER6',
        assetType: 'STOCK',
        industry: 'Technology',
        currentPrice: 95.4,
        country: COUNTRY_DATA[1],
        stockExchange: STOCK_EXCHANGE_DATA[1]
    },
    {
        id: 7,
        name: 'CHINESE TEST STOCK 1',
        ticker: 'TEST-TICKER7',
        assetType: 'STOCK',
        industry: 'Airline and Aviation',
        currentPrice: 0.01,
        country: COUNTRY_DATA[2],
        stockExchange: STOCK_EXCHANGE_DATA[2]
    },
    {
        id: 8,
        name: 'CHINESE TEST STOCK 2',
        ticker: 'TEST-TICKER8',
        assetType: 'ETF',
        industry: 'Agriculture',
        currentPrice: 0.45,
        country: COUNTRY_DATA[2],
        stockExchange: STOCK_EXCHANGE_DATA[2]
    },
    {
        id: 9,
        name: 'CHINESE TEST STOCK 3',
        ticker: 'TEST-TICKER9',
        assetType: 'BOND',
        industry: 'Technology',
        currentPrice: 0.001,
        country: COUNTRY_DATA[2],
        stockExchange: STOCK_EXCHANGE_DATA[2]
    }
];

const ORDER_DATA = [
    {
        id: 1,
        transactionType: 'BUY',
        numberOfShares: 20,
        price: 2000,
        date: '2025-05-17',
        stockId: 1
    },
    {
        id: 2,
        transactionType: 'SELL',
        numberOfShares: 5,
        price: 232.50,
        date: '2024-03-08',
        stockId: 3
    },
    {
        id: 3,
        transactionType: 'BUY',
        numberOfShares: 1,
        price: 250.876,
        date: '2022-08-09',
        stockId: 5
    },
    {
        id: 4,
        transactionType: 'SELL',
        numberOfShares: 100,
        price: 45.085,
        date: '2002-12-16',
        stockId: 6
    },
    {
        id: 5,
        transactionType: 'BUY',
        numberOfShares: 22,
        price: 67.95,
        date: '2015-10-01',
        stockId: 7
    },
    {
        id: 6,
        transactionType: 'SELL',
        numberOfShares: 5,
        price: 64.73,
        date: '2023-07-05',
        stockId: 9
    }
];

function OrderList() {
    // State variables
    const [orders, setOrders] = useState([]);
    const [stocks, setStocks] = useState([]);
    // const [orders, setOrders] = useState(ORDER_DATA);
    // const [stocks, setStocks] = useState(STOCK_DATA);
    const [loading, setLoading] = useState(true);
    const url = 'http://localhost:8080/api/order';

    // Use Effect
    useEffect(() => {
        async function fetchData() {
            const token = localStorage.getItem('token') || sessionStorage.getItem('token');
            try {
                const ordersResponse = await fetch("http://localhost:8080/api/order", {
                    headers: {
                        "Authorization": `Bearer ${token}`,
                        "Content-Type": "application/json"
                    }
                });

                const stocksResponse = await fetch("http://localhost:8080/api/stocks", {
                    headers: {
                        "Authorization": `Bearer ${token}`,
                        "Content-Type": "application/json"
                    }
                });

                if (!ordersResponse.ok || !stocksResponse.ok) {
                    throw new Error("Unauthorized or failed fetch");
                }

                const ordersData = await ordersResponse.json();
                const stocksData = await stocksResponse.json();

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
                    to={'/order/add'}
                    startIcon={<Add />}
                >
                    Add an Order
                </Button>
            </Stack>
            <Stack spacing={3}>
                {orders.map((order) => (
                    <Card key={order.id} variant="outlined" sx={{ backgroundColor: "#f4f4f4" }}>
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
                                Price: ${order.price.toFixed(2)}
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