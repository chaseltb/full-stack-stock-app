import { useState } from "react";
import React from "react";
import { Container, Box, Typography, Paper, 
    TextField, Checkbox, Button, FormControlLabel, 
    Box, Link } from "@mui/material";

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
                // paper makes a drop shadow to make containers look 3d
                <Paper elevation={4} sx={{ mt: 4, p: 4, borderRadius: 10}}>
                    <Typography variant="h2" align="center">
                        {isLogin ? "Login" : "Register"}
                    </Typography>
                    <Typography align="center" variant="h4" sx={{ mt: 4 }}>
                        Stock App
                    </Typography>

                    // error will go here 
                    
                </Paper>

                <Box component="form" onSubmit={handleSubmit}>
                    // register and login use username+password
                    <TextField label="Username" name="username" fullWidth margin="normal" 
                        value={username} onChange={handleChange} />
                    <TextField label="Password" name="password" fullWidth margin="normal" 
                        value={password} onChange={handleChange} />

                    {!isLogin && (
                        <>
                            <TextField label="First Name" name="firstName" fullWidth margin="normal"
                                value={firstName} onChange={handleChange} />
                            <TextField label="Last Name" name="lastName" fullWidth margin="normal"
                                value={lastName} onChange={handleChange} />
                            // replace with a dropdown of currency and store as currency id? might have to call currency api in useEffect though??
                            <TextField label="Currency Id" name="currency" fullWidth margin="normal"
                                value={currency} onChange={handleChange} />
                        </>
                    )}

                    <FormControlLabel control={<Checkbox/>}>
                        Keep me logged in
                    </FormControlLabel>
                    
                    <Button type="submit" fullWidth sx={{ mt: 2, borderRadius: 2}}>
                        {isLogin ? "Login": "Register"}
                    </Button>
                </Box>

            </Container>
        
        </>
    )


}

export default AuthPage;