import { useState, useEffect } from "react";
import React from "react";
import { Container, Box, Typography, Paper, Alert, Button } from "@mui/material";
import { ArrowBack } from "@mui/icons-material";

function Stock() {
    const [orders, setOrders] = useState([]);
    const [error, setError] = useState("");

    const url = "http://localhost:8080/api/";

    useEffect(() => {
        if (Stock.id) {
            loadOrders();
        }
    }, [stock]);

    const loadOrders = async () => {
        setError("");

        try {
            // fetch orders of that stock from that user
        } catch (error) {
            setError(error);
        }
    };

    const handleAddOrder = () => {
        console.log("add order");
    }

    return (
        <>
        
        
        </>
    );

}

export default Stock;