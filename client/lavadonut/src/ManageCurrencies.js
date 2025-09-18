import { useState, useEffect } from "react";
import React from "react";
import {
    Container, Box, Typography, Paper, Button, TextField, Alert,
    Dialog, DialogContent, DialogActions, DialogTitle, Fab,
    IconButton
} from "@mui/material";
import { Add as AddIcon, Edit as EditIcon, Delete as DeleteIcon } from "@mui/icons-material";

function ManageCurrencies() {
    const [currencies, setCurrencies] = useState([]);

    const [error, setError] = useState("");
    const [editing, setEditing] = useState(null);
    const [viewDialog, setViewDialog] = useState(false);

    // currency specific
    const [currencyName, setCurrencyName] = useState("");
    const [currencyCode, setCurrencyCode] = useState("");
    const [valueToUsd, setValueToUsd] = useState("");

    const url = "http://localhost:8080/api/currency/";

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
                return Promise.reject(`Unexpected Status Code ${response.status}`);
            } else {
                const data = await response.json();
                setCurrencies(data);
            }
        } catch (error) {
            setError(error.message || "Currencies failed to load");
        }
    };

    const handleAdd = () => {
        console.log("add clicked");
        setEditing(null);
        setCurrencyCode("");
        setCurrencyName("");
        setValueToUsd("");
        setViewDialog(true);
    }

    const handleEdit = (currency) => {
        console.log("edit clicked");
        setEditing(currency);
        setCurrencyCode(currency.code);
        setCurrencyName(currency.name);
        setValueToUsd(currency.valueToUsd);
        setViewDialog(true);
    }

    const handleDelete = async (currencyId) => {
        if (window.confirm("Are you sure that you would like to delete this?")) {
            try {
                // delete api call
                const response = await fetch(`${url}/${currencyId}`, {
                    method: "DELETE",
                    headers: {
                        Authorization: "Bearer " + localStorage.getItem("token"),
                    },
                });
                if (response.ok) {
                    setCurrencies((prev) => prev.filter((c) => c.id !== currencyId));
                } else {
                    return Promise.reject(`Unexpected Status Code ${response.status}`);
                }
            } catch (error) {
                setError(error.message || "Delete currencies failed");
            }
        }
    };

    const handleSubmit = async () => {
        try {
            if (editing) {
                // edit
                const requestBody = {
                    id: editing.id,
                    name: currencyName.trim(),
                    code: currencyCode.trim().toUpperCase(),
                    valueToUsd: parseFloat(valueToUsd),
                };
                const response = await fetch(`${url}/${editing.id}`, {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: "Bearer " + localStorage.getItem("token"),
                    },
                    body: JSON.stringify(requestBody),
                });
                if (response.ok) {
                    await loadCurrencies();
                } else {
                    return Promise.reject(`Unexpected Status Code ${response.status}`);
                }
            } else {
                // add
                const requestBody = {
                    name: currencyName.trim(),
                    code: currencyCode.trim().toUpperCase(),
                    valueToUsd: parseFloat(valueToUsd),
                };
                console.log(JSON.stringify(requestBody));
                const response = await fetch(url, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        Authorization: "Bearer " + localStorage.getItem("token"),
                    },
                    body: JSON.stringify(requestBody),
                });
                if (response.ok) {
                    await loadCurrencies();
                } else {
                    return Promise.reject(`Unexpected Status Code ${response.status}`);
                }
            }

            setViewDialog(false);
            setEditing(null);
        } catch (error) {
            setError(error.message || "Edit or add failed");
        }
    };

    return (
        <Container maxWidth="lg">
            <Paper elevation={4} sx={{ mt: 4, p: 4, borderRadius: 8 }}>
                <Typography variant="h2" sx={{ mb: 4 }}>
                    Manage Currencies
                </Typography>

                {error && <Alert severity="error" sx={{ mb: 4 }} onClose={() => setError("")}>
                    {error}
                </Alert>}

                {/* currencies list  */}
                {currencies.map((currency) => (
                    <Paper key={currency.id} elevation={2} sx={{ mb: 2, p: 4, borderRadius: 6 }}>
                        <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
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
                                    onClick={() => handleDelete(currency.id)}
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
                            color="primary"
                            onClick={() => handleAdd()}
                            sx={{ p: 2, borderRadius: 2 }}
                        >
                            <AddIcon />
                        </IconButton>
                    </Box>
                )}
            </Paper>

            {/* add/edit dialog  */}
            <Dialog open={viewDialog} onClose={() => setViewDialog(false)} fullWidth>
                <DialogTitle>
                    {editing ? "Edit Currency" : "Add Currency"}
                </DialogTitle>
                <DialogContent>
                    <Box sx={{ display: "flex", flexDirection: "column", gap: 5 }}>
                        <TextField
                            label="Currency Code"
                            value={currencyCode}
                            onChange={(e) => setCurrencyCode(e.target.value.toUpperCase())}
                            margin="dense"
                        />
                        <TextField
                            label="Currency Name"
                            value={currencyName}
                            onChange={(e) => setCurrencyName(e.target.value)}
                            margin="dense"
                        />
                        <TextField
                            label="Value to USD"
                            type="number"
                            value={valueToUsd}
                            onChange={(e) => setValueToUsd(e.target.value)}
                            margin="dense"
                        />
                    </Box>
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
                onClick={handleAdd}
                sx={{ position: 'fixed', bottom: 24, right: 24 }}
            >
                <AddIcon />
            </Fab>
        </Container>
    );
}

export default ManageCurrencies;