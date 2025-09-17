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

    const url = "http://localhost:8080/api/stock-exchange";

    useEffect(() => {
        loadExchanges();
    }, []);

    const loadExchanges = () => {
        setError("");

        try {

        } catch {
            setError(error.message || "Exchanges failed to load");
        }
    }

    const handleAdd = () => {

    }

    const handleEdit = () => {
        
    }

    const handleDelete = () => {
        
    }

    const handleSubmit = () => {
        
    }

    return (
        <>
        </>
    );
}

export default ManageStockExchanges;