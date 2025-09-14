import { useState } from "react";
import React from "react";
import { Container, Box, Typography, Paper, 
    TextField, Checkbox, Button, FormControlLabel, 
    Alert, Box, Link } from "@mui/material";

function AuthPage () {
    // state variables
    const [isLogin, setIsLogin] = useState(false);
    const [keepLogin, setKeepLogin] = useState(false);
    const [error, setError] = useState("");

    // for login and register
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    // for register only
    const [username, setUsername] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [currency, setCurrency] = useState("");

    const url = "http://localhost:8080/api/";

    const handleSubmit = async (event) => {
        event.preventDefault();
        setError("");

        try {
            const apiEndpoint = isLogin ? "auth/authenticate" : "auth/register";
            const requestBody = isLogin ? { username: username, password: password }
            : { username: username, password: password, firstName: firstName, lastName: lastName, 
                currencyId: currency};
            
            const response = await fetch(`${url}${apiEndpoint}`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(requestBody)
            });

            const data = response.json();
            if (isLogin) {
                // login
            } else {
                // register
            }

        } catch (error) {
            setError(error);
        }

    };

    return (
        <>
            <Container maxWidth="sm">
            
            </Container>
        
        </>
    )


}

export default AuthPage;