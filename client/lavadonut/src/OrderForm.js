import { useEffect, useState } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";
import { Box, Button, TextField, Typography, Alert, Grid, Select, FormControl, InputLabel, MenuItem } from "@mui/material";

const ORDER_DEFAULT = {
    transactionType: "BUY",
    stockId: 0,
    numberOfShares: 0.0,
    date: "",
    price: 0.0
}

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
        const token = localStorage.getItem('token');

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
        const token = localStorage.getItem('token');
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
            if (response.status === 201 || response.status === 400) {
                return response.json();
            } else {
                return Promise.reject(`Unexpected Status Code: ${response.status}`);
            }
        })
        .then(data => {
            if (data.id) {
                navigate('/orders');
            } else {
                setErrors(data);
            }
        })
        .catch(console.log);
    }

    const updateOrder = () => {
        order.id = id;
        const token = localStorage.getItem('token');
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
            if (response.status === 204) {
                return null;
            } else if (response.status === 400) {
                return response.json();
            } else {
                return Promise.reject(`Unexpected Status Code ${response.status}`);
            }
        })
        .then(data => {
            if (data.id) {
                navigate('/orders');
            } else {
                setErrors(data);
            }
        })
        .catch(console.log);
    }

    return (
        <>
            <Box sx={{ maxWidth: 600, mx: "auto", mt: 4 }}>
                <Typography variant="h2" gutterBottom>
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
            <Box component="form" onSubmit={handleSubmit} noValidate>
                <Grid container spacing={2}>
                    {/* Select either BUY or SELL */}
                    <Grid item xs={12}>
                        <FormControl fullWidth>
                            <InputLabel id="transactionType-label">Transaction Type</InputLabel>
                            <Select
                                labelId="transactionType-label"
                                name="transactionType"
                                value={order.transactionType}
                                label="Transaction Type"
                                onChange={handleChange}
                            >
                                <MenuItem value="BUY">BUY</MenuItem>
                                <MenuItem value="SELL">SELL</MenuItem>
                            </Select>
                        </FormControl>
                    </Grid>
                    {/* Choose stock by stockId */}
                    <Grid item xs={12}>
                        <FormControl fullWidth>
                            <InputLabel id="stockId-label">Stock</InputLabel>
                            <Select
                                labelId="stockId-label"
                                name="stockId"
                                value={order.stockId}
                                label="Stock"
                                onChange={handleChange}
                            >
                                {stocks.map((stock) => {
                                    <MenuItem key={stock.id} value={stock.id}>
                                        {stock.ticker} - {stock.name}
                                    </MenuItem>
                                })}
                            </Select>
                        </FormControl>
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
                            label="date"
                            name="Date"
                            type="date"
                            value={order.date}
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