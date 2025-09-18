import { useEffect, useState } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";
import { Box, Button, TextField, Typography, Alert, Grid, Select, FormControl, InputLabel, MenuItem, Radio, RadioGroup, FormControlLabel, Autocomplete } from "@mui/material";

const ORDER_DEFAULT = {
    transactionType: "BUY",
    stockId: 0,
    numberOfShares: 0.0,
    date: "",
    price: 0.0
}

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

function OrderForm() {
    // State management
    const [order, setOrder] = useState(ORDER_DEFAULT);
    const [stocks, setStocks] = useState([]);
    const [errors, setErrors] = useState([]);
    const url = 'http://localhost:8080/api/order';

    // Hook for programmatic navigation
    const navigate = useNavigate();

    // Hook to access URL parameters
    const { id } = useParams();

    // useEffect
    useEffect(() => {
        const token = localStorage.getItem('token') || sessionStorage.getItem('token');

        // Fetch order
        if (id) {
            fetch(`${url}/${id}`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            })
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else {
                    return Promise.reject(`Unexpected Status Code: ${response.status}`);
                }
            })
            .then((data) => {
                setOrder(data);
            })
            .catch(console.log);
        } else {
            setOrder(ORDER_DEFAULT);
        }

        // Fetch stocks
        fetch("http://localhost:8080/api/stocks", {
            headers: {
                    'Authorization': `Bearer ${token}`
                }
        })
        .then((response) => {
            if(response.status === 200) {
                return response.json();
            } else {
                return Promise.reject(`Unexpected Status Code: ${response.status}`);
            }
        })
        .then(setStocks)
        .catch(console.log);
    }, [id]);

    // Handle form submission
    const handleSubmit = (event) => {
        event.preventDefault();
        if (id) {
            updateOrder();
        } else {
            addOrder();
        }
    }

    // Handle input changes in form
    const handleChange = (event) => {
        const { name, value } = event.target;
        setOrder({ ...order, [name]: value });
    }

    // CRUD Functions
    const addOrder = () => {
        const token = localStorage.getItem('token') || sessionStorage.getItem('token');
        const init = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(order)
        };

        fetch(url, init)
        .then(response => {
            if (!response.ok) {
                return Promise.reject(`Unexpected Status Code: ${response.status}`);
            }
            return response.text();  // backend may or may not return body
        })
        .then((text) => {
            if (text) {
                JSON.parse(text); // optional: still parse if JSON exists
            }
            navigate('/orders', { replace: true }); // always go to /orders
        })
        .catch(console.log);
    }

    const updateOrder = () => {
        order.id = id;
        const token = localStorage.getItem('token') || sessionStorage.getItem('token');
        const init = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(order)
        };

        fetch(`${url}/${id}`, init)
        .then(response => {
            if (!response.ok) {
                return Promise.reject(`Unexpected Status Code: ${response.status}`);
            }
            return response.text();
        })
        .then((text) => {
            if (text) {
                JSON.parse(text);
            }
            navigate('/orders', { replace: true }); // always go to /orders
        })
        .catch(console.log);
    }


    return (
        <>
            <Box sx={{ maxWidth: 400, mx: "auto", mt: 6 }}>
                <Typography variant="h2" align="center" gutterBottom>
                    {id > 0 ? "Update an Order" : "Add an Order"}
                </Typography>
                {errors.length > 0 && (
                    <Alert severity="error" sx={{ mb: 2 }}>
                        <Typography variant="subtitle2">The following errors were found:</Typography>
                        <ul>
                            {errors.map((error) => (
                                <li key={error}>{error}</li>
                            ))}
                        </ul>
                    </Alert>
                )}
            </Box>
            <Box 
                component="form" 
                onSubmit={handleSubmit} 
                sx={{
                    width: "100%",
                    maxWidth: 480,
                    mx: "auto",
                    p: 4,
                    backgroundColor: "#fff",
                    borderRadius: 2,
                    boxShadow: 3
                }} 
                noValidate
            >
                <Grid container spacing={2}>
                    {/* Select either BUY or SELL */}
                    <Box sx={{ display: "flex", flexDirection: "column" }}>
                        <Typography variant="subtitle1" gutterBottom>
                            Order Type
                        </Typography>
                        <RadioGroup
                            row
                            name="transactionType"
                            value={order.transactionType}
                            onChange={handleChange}
                        >
                            <FormControlLabel value="BUY" control={<Radio />} label="Buy" />
                            <FormControlLabel value="SELL" control={<Radio />} label="Sell" />
                        </RadioGroup>
                    </Box>
                    {/* Choose stock by stockId */}
                    <Grid item xs={12}>
                        <Autocomplete
                            options={stocks}
                            getOptionLabel={(option) => `${option.name} (${option.ticker})`}
                            renderInput={(params) => <TextField {...params} label="Stock" />}
                            value={stocks.find(s => s.id === order.stockId) || null}
                            onChange={(event, newValue) => {
                                setOrder({ ...order, stockId: newValue ? newValue.id : 0 });
                            }}
                        />
                    </Grid>
                    <Grid item xs={12}>
                        <TextField
                            fullWidth
                            label="Number of Shares"
                            name="numberOfShares"
                            type="number"
                            value={order.numberOfShares}
                            onChange={handleChange}
                        />
                    </Grid>
                    <Grid item xs={12}>
                        <TextField
                            fullWidth
                            label="Date"
                            name="date"
                            type="date"
                            value={order.date}
                            InputLabelProps={{ shrink: true }}
                            onChange={handleChange}
                        />
                    </Grid>
                    <Grid item xs={12}>
                        <TextField
                            fullWidth
                            label="Price"
                            name="price"
                            type="number"
                            value={order.price}
                            onChange={handleChange}
                        />
                    </Grid>
                </Grid>
                <Box sx={{ mt: 4, display: 'flex', justifyContent: 'space-between'}}>
                    <Button
                        type="submit"
                        variant="contained"
                        color="primary"
                    >
                        {id > 0 ? "Update" : "Submit"}
                    </Button>
                    <Button
                        component={Link}
                        to="/orders"
                        variant="outlined"
                        color="error"
                    >
                        Cancel
                    </Button>
                </Box>
            </Box>
        </>
    );
}

export default OrderForm;