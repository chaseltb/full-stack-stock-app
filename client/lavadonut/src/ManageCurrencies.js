import { useState, useEffect } from "react";
import React from "react";
import { Container, Box, Typography, Paper, Button, TextField, Alert,
    Dialog, DialogContent, DialogActions } from "@mui/material";
import { Add, Edit, Delete} from "@mui/icons-material";

function ManageCurrencies() {
    const [currencies, setCurrencies] = useState([]);
    const [error, setError] = useState("");

    const [editing, setEditing] = useState(null);

    const url = "http://localhost:8080/api/";

    useEffect(() => {
        loadCurrencies();
    }, []);

    const loadCurrencies = async () => {
        setError("");

        try {
            // get currencies
        } catch (error) {
            setError(error);
        }
    };

    const handleAdd = () => {
        console.log("add clicked");
    }

    const handleEdit = () => {
        console.log("edit clicked");
    }

    const handleDelete = async (currencyId) => {
        if (window.confirm("Are you sure that you would like to delete this?")) {
            try {
                // delete api call
            } catch (error) {
                setError(error);
            }
        }
    }

    const handleSubmit = async () => {
        try {
            if (editing) {
                // edit
            } else {
                // add
            }

        } catch (error) {
            setError(error);
        }
    }

    return (
        <>
        
        </>
    );
}