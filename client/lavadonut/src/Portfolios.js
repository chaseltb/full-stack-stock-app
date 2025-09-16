import {useState, useEffect} from "react";
import { Container, Box, Typography, Paper, Alert, Button } from "@mui/material";
import React from "react";

function Portfolios() {
    const [portfolios, setPortfolios] = useState([]);
    const [error, setError] = useState("");

    const url = "http://localhost:8080/api/";

    useEffect(() => {
        loadPortfolios();
    }, []);

    const loadPortfolios = async () => {
        setError("");

        try {
            // fetch portfolios url
        } catch (error) {
            setError(error);
        }
    };

    return (
        <Container maxWidth="lg">
            <Paper elevation={4} sx={{ mt: 4, p: 4, borderRadius: 10 }}>
                <Box sx={{ display: "flex", justifyContent: "space-between", alignItem: "center", mb: 4}}>
                    <Typography variant="h2">
                        Portfolios
                    </Typography>
                    <Button variant="contained" sx={{ borderRadius: 5 }}>
                        Add Portfolio
                    </Button>
                </Box>

                {error && <Alert severity="error" sx={{ mb: 4 }}>
                    {error}
                </Alert>}

                {/* list of portfolios */}
                <Box sx={{ display: "grid", gap: 3}}>
                    {portfolios.map((portfolio) => (
                        <Paper key={portfolio.id} elevation={2} sx={{ p: 2, borderRadius: 10}}>
                            <Box sx={{ p: 2, mb: 2}}>
                                <Typography variant="body1">
                                    {portfolio.name}
                                </Typography>
                                <Typography variant="body1">
                                    {portfolio.type}
                                </Typography>
                            </Box>

                            <Box sx={{ mb: 4 }}>
                                <Typography variant="body1">
                                    Total Shares: 
                                </Typography>
                                <Typography variant="body1">
                                    Total Value:
                                </Typography>
                                <Typography variant="body1" 
                                    color={portfolio.unrealizedGainOrLoss >= 0 ? "success" : "error"}>
                                        Unrealized Gain/Loss: 
                                </Typography>
                            </Box>
                        </Paper>
                    ))}
                </Box>

                {portfolios.length === 0 && (
                    <Box sx={{ textAlign: "center" }}>
                        <Typography variant="h4" sx={{ mb: 2 }}>
                            You haven't added any portfolios
                        </Typography>
                        <Button variant="contained" sx={{ borderRadius: 4}}>
                            Create a portfolio
                        </Button>
                    </Box>
                )}
            </Paper>
        </Container>
    );

}

export default Portfolios;