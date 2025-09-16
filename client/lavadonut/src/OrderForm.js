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