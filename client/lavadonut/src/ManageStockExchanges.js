import { useState, useEffect } from "react";
import React from "react";
import {
    Container, Box, Typography, Paper, Button, TextField, Alert,
    Dialog, DialogContent, DialogActions, Chip, MenuItem, Fab,
    IconButton, DialogTitle
} from "@mui/material";
import { Add as AddIcon, Edit as EditIcon, Delete as DeleteIcon, Visibility as ViewIcon } from "@mui/icons-material";

function ManageStockExchanges() {
    const [exchanges, setExchanges] = useState([]);
    const [error, setError] = useState("");
    const [editing, setEditing] = useState(false);

    // whether or not the view/add dialog is open, and respective exchange
    const [viewDialog, setViewDialog] = useState(false);
    const [createDialog, setCreateDialog] = useState(false);
    const [selectedExchange, setSelectedExchange] = useState(null);

    // stock exchange specific fields
    const [exchangeName, setName] = useState("");
    const [code, setCode] = useState("");
    const [timeZone, setTimeZone] = useState("");

    const url = "http://localhost:8080/api/stock-exchange";

    const mockExchanges = [
        { id: 1, name: "New York Stock Exchange", code: "NYSE", timeZone: "EST" },
        { id: 2, name: "NASDAQ", code: "NASDAQ", timeZone: "EST" },
        { id: 3, name: "London Stock Exchange", code: "LSE", timeZone: "GMT" }
    ];

    useEffect(() => {
        loadExchanges();
    }, []);

    const loadExchanges = () => {
        setError("");

        try {
            setExchanges(mockExchanges);
        } catch {
            setError(error.message || "Exchanges failed to load");
        }
    }

    const handleAdd = () => {
        setName("");
        setCode("");
        setTimeZone("");
        setEditing(false);
        setCreateDialog(true);
    }

    const handleEdit = (exchange) => {
        setSelectedExchange(exchange);
        setName(exchange.name);
        setCode(exchange.code);
        setTimeZone(exchange.timeZone);
        setEditing(true);
        setCreateDialog(true);
    }

    const handleDelete = (exchangeId) => {
        if (window.confirm("Are you sure that you would like to delete this exchange?")) {
            try {
                // delete api call
                setExchanges(exchanges.filter((x) => x.id !== exchangeId));
            } catch (error) {
                setError(error);
            }
        }
    }

    const handleSubmit = () => {
        const newExchange = {
            id: editing && selectedExchange ? selectedExchange.id : exchanges.length + 1,
            name: exchangeName,
            code: code,
            timeZone: timeZone,
        };
        
        setCreateDialog(false);

        try {
            if (editing) {
                // edit
                setExchanges(exchanges.map((e) =>
                    e.id === newExchange.id ? newExchange : e
                ));
            } else {
                // add
                setExchanges([...exchanges, newExchange]);
            }
        } catch (error) {
            setError(error);
        }

        setCreateDialog(false);
        setEditing(false);
        setSelectedExchange(null);
        setName("");
        setCode("");
        setTimeZone("");
    }

    const handleViewExchange = (exchange) => {
        setSelectedExchange(exchange);
        setViewDialog(true);
    }

    return (
        <Container maxWidth="lg">
            <Paper elevation={4} sx={{ mt: 4, p: 4, borderRadius: 10 }}>
                <Typography sx={{ mb: 4 }}>
                    Manage Stock Exchanges
                </Typography>

                {error && <Alert severity="error" sx={{ mb: 4 }}>
                    {error}
                </Alert>}

                {/* exchanges list  */}
                {exchanges.map((exchange) => (
                    <Paper key={exchange.id} elevation={2} sx={{ mb: 2, p: 1, borderRadius: 4 }}>
                        <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                            <Box sx={{ ml: 2 }}>
                                <Typography sx={{ mb: 1 }}>
                                    ID: {exchange.id} - {exchange.name}
                                </Typography>
                                <Typography variant="body1" color="textSecondary">
                                    Exchange Code: {exchange.code}
                                </Typography>
                                <Box sx={{ mb: 1, mt: 2, display: "flex", gap: 2 }}>
                                    <Chip label={exchange.timeZone} size="small">
                                    </Chip>
                                </Box>
                            </Box>
                            <Box>
                                <Button variant="outlined" startIcon={<ViewIcon />} onClick={() => handleViewExchange(exchange)} sx={{ mr: 2, color: "blue", borderRadius: 4 }}>
                                    View
                                </Button>
                                <Button variant="outlined" color="secondary" startIcon={<EditIcon />} onClick={() => handleEdit(exchange)} sx={{ mr: 2, borderRadius: 4 }}>
                                    Edit
                                </Button>
                                <Button variant="outlined" color="error" startIcon={<DeleteIcon />} onClick={() => handleDelete(exchange.id)} sx={{ borderRadius: 4 }}>
                                    Delete
                                </Button>
                            </Box>
                        </Box>

                    </Paper>
                ))}

                {exchanges.length === 0 && (
                    <Box sx={{ textAlign: "center" }}>
                        <Typography variant="h5">
                            No stock exchanges were found
                        </Typography>
                        <IconButton
                            label="Add a stock exchange"
                            color="primary"
                            onClick={() => handleAdd()}
                            sx={{ p: 2, borderRadius: 2 }}
                        >
                            <AddIcon />
                        </IconButton>
                    </Box>
                )}
            </Paper>

            {/* view dialog if open  */}
            <Dialog open={viewDialog} onClose={() => setViewDialog(false)}>
                <DialogTitle>
                    View Stock Exchange:
                </DialogTitle>
                <DialogContent>
                    {selectedExchange && (
                        <Box sx={{ display: "flex", flexDirection: "column", gap: 5 }}>
                            <Typography>
                                ID: {selectedExchange.id}
                            </Typography>
                            <Typography>
                                Exchange Name: {selectedExchange.name}
                            </Typography>
                            <Typography>
                                Exchange Code: {selectedExchange.code}
                            </Typography>
                            <Typography>
                                Exchange Time Zone: {selectedExchange.timeZone}
                            </Typography>
                        </Box>
                    )}
                </DialogContent>
            </Dialog>


            {/* create/edit dialog if open  */}
            <Dialog open={createDialog} onClose={() => setCreateDialog(false)} maxWidth="sm">
                <DialogTitle>
                    {editing ? "Edit Stock Exchange" : "Create New Stock Exchange"}
                </DialogTitle>
                <DialogContent>
                    <Box sx={{ display: "flex", flexDirection: "column", gap: 3 }}>
                        <TextField
                            label="Name"
                            value={exchangeName}
                            onChange={(e) => setName(e.target.value)}
                            fullWidth
                            margin="dense"
                        />
                        <TextField
                            label="Code"
                            value={code}
                            onChange={(e) => setCode(e.target.value)}
                            fullWidth
                            margin="dense"
                        />
                        <TextField
                            label="Time Zone"
                            value={timeZone}
                            onChange={(e) => setTimeZone(e.target.value)}
                            fullWidth
                            margin="dense"
                        />
                    </Box>
                </DialogContent>
                <DialogActions sx={{ p: 2 }}>
                    <Button onClick={() => setCreateDialog(false)} sx={{ borderRadius: 4 }}>
                        Cancel
                    </Button>
                    <Button onClick={handleSubmit} variant="contained" sx={{ borderRadius: 4 }}>
                        {editing ? "Update Exchange" : "Create Stock Exchange"} 
                    </Button>
                </DialogActions>
            </Dialog>

            {/* floating action button for add  */}
            <Fab
                color="primary"
                sx={{ position: 'fixed', bottom: 36, right: 36 }}
                onClick={handleAdd}
            >
                <AddIcon />
            </Fab>
        </Container>
    );
}

export default ManageStockExchanges;