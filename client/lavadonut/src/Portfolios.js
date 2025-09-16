import { Typography } from "@mui/material";
import {useState, useEffect} from "react";
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
        <>
        </>
    );

}

export default Portfolios;