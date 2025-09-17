import { useState, useEffect } from "react";
import React from "react";
import { Container, Box, Typography, Paper, Button, TextField, Alert,
    Dialog, DialogContent, DialogActions, DialogTitle, Fab,
    IconButton} from "@mui/material";
import { Add as AddIcon, Edit as EditIcon, Delete as DeleteIcon} from "@mui/icons-material";

function ManageCurrencies() {
    const [currencies, setCurrencies] = useState([]);
    const [error, setError] = useState("");

    const [editing, setEditing] = useState(null);

    const [viewDialog, setViewDialog] = useState(false);
    const [currencyName, setCurrencyName] = useState("");
    const [currencyCode, setCurrencyCode] = useState("");

    const url = "http://localhost:8080/api/";

    const mockCurrencies = [
        { id: 1, code: "USD", name: "US Dollar" },
        { id: 2, code: "EUR", name: "Euro" },
        { id: 3, code: "GBP", name: "British Pound" },
        { id: 4, code: "CAD", name: "Canadian Dollar" },
        { id: 5, code: "JPY", name: "Japanese Yen" }
    ];

    useEffect(() => {
        loadCurrencies();
    }, []);

    const loadCurrencies = async () => {
        setError("");

        try {
            // get currencies
            const response = await fetch(url, {
                headers: {
                    "Content-Type": "application/json",
                    Authorization: "Bearer " + localStorage.getItem("token"),
                },
            });
            if (!response.ok) {
                setError("Get failed");
            } else {
                const data = await response.json();
                setCurrencies(data);
            }
        } catch (error) {
            setError(error);
        }
    };

    const handleAdd = () => {
        console.log("add clicked");
        setEditing(null);
        setCurrencyCode("");
        setCurrencyName("");
        setViewDialog(true);
    }

    const handleEdit = (currency) => {
        console.log("edit clicked");
        setEditing(currency);
        setCurrencyCode(currency.code);
        setCurrencyName(currency.name);
        setViewDialog(true);
    }

    const handleDelete = async (currencyId) => {
        if (window.confirm("Are you sure that you would like to delete this?")) {
            try {
                // delete api call
                const response = await fetch(`${url}/${currency.id}`, {
                    method: "DELETE",
                    headers: {
                        Authorization: "Bearer " + localStorage.getItem("token"),
                    },
                });
                if (response.ok) {
                    setCurrencies((prev) => prev.filter((c) => c.id !== currency));
                } else {
                    setError("Delete was unsuccessful");
                }
            } catch (error) {
                setError(error.message);
            }
        }
    }

    const handleSubmit = async () => {
        try {
            const requestBody = {
                id: editing ? editing.id : 0,
                code: currencyCode,
                name: currencyName,
                valueToUsd: parseFloat(valueToUsd),
            };
            if (editing) {
                // edit
                const response = await fetch(, {
                    
                })
            } else {
                // add
            }

        } catch (error) {
            setError(error);
        }
    }

    return (
        <Container>
            <Paper elevation={4} sx={{ mt: 4, p: 4, borderRadius: 8}}>
                <Typography variant="h2" sx={{ mb: 4 }}>
                    Manage Currencies
                </Typography>

                {error && <Alert severity="error" sx={{ mb: 4 }} onClose={() => setError("")}>
                    {error}
                </Alert>}

                {/* currencies list  */}
                {currencies.map((currency) => (
                    <Paper key={currency.id} elevation={2} sx={{ mb: 2, p: 4, borderRadius: 6 }}>
                        <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center"}}>
                            <Box>
                                <Typography variant="h5">
                                    {currency.code}
                                </Typography>
                                <Typography variant="body1">
                                    {currency.name}
                                </Typography>
                                <Typography variant="body1">
                                    ID: {currency.id}
                                </Typography>
                            </Box>
                            <Box>
                                <IconButton 
                                    color="primary"
                                    onClick={() => handleEdit(currency)}
                                    sx={{ p: 1, borderRadius: 6 }}
                                >
                                    <EditIcon />
                                </IconButton>
                                <IconButton 
                                    color="error"
                                    onClick={() => handleDelete(currency)}
                                    sx={{ p: 1, borderRadius: 6 }}
                                >
                                    <DeleteIcon />
                                </IconButton>
                            </Box>
                        </Box>
                    </Paper>
                ))}

                {currencies.length === 0 && (
                    <Box sx={{ textAlign: "center" }}>
                        <Typography variant="h6" sx={{ mb: 2 }}>
                            No currencies have been added
                        </Typography>
                        <IconButton
                            label="Add a currency"
                            color="error"
                            onClick={() => handleDelete(currency)}
                            sx={{ p: 1, borderRadius: 6 }}
                        >
                            <AddIcon />
                        </IconButton>
                    </Box>
                )}
            </Paper>

            {/* add/edit dialog  */}
            <Dialog open={viewDialog} onClose={() => setViewDialog(false)} maxWidth="sm">
                <DialogTitle>
                    {editing ? "Edit Currency" : "Add Currency"}
                </DialogTitle>
                <DialogContent>
                    <TextField
                        label="Currency Code"
                        value={currencyCode}
                        onChange={(e) => setCurrencyCode(e.target.value.toUpperCase())}
                        margin="dense"
                    />
                    <TextField
                        label="Currency Name"
                        value={currencyName}
                        onChange={(e) => setCurrencyName(e.target.value.toUpperCase())}
                        margin="dense"
                    />
                </DialogContent>
                <DialogActions sx={{ p: 2 }}>
                    <Button onClick={() => setViewDialog(false)} sx={{ borderRadius: 4 }}>
                        Cancel
                    </Button>
                    <Button onClick={handleSubmit} variant="contained" sx={{ borderRadius: 4 }}>
                        {editing ? "Update" : "Add"}
                    </Button>
                </DialogActions>
            </Dialog>

            {/* floating add button  */}
            <Fab 
                color="primary"
                sx={{ position: 'fixed', bottom: 24, right: 24}}
            >
                <AddIcon />
            </Fab>
        </Container>
    );
}

export default ManageCurrencies;