import { useEffect, useState } from "react";
import { useNavigate, Navigate, useLocation } from "react-router-dom";
import {
    Container, Box, Typography, Paper, TextField, Checkbox,
    Button, FormControlLabel, Link, Alert,
    FormControl,
    InputLabel,
    Select,
    MenuItem
} from "@mui/material";

const CURRENCY_DATA = [
    {
        id: 1,
        name: 'United States Dollar',
        code: 'USD',
        valueToUsd: 1.0
    },
    {
        id: 2,
        name: 'Euro',
        code: 'EUR',
        valueToUsd: 1.17
    },
    {
        id: 3,
        name: 'Chinese Yuan',
        code: 'CNY',
        valueToUsd: 0.14
    }
];

function AuthPage() {
    // state variables
    const [isLogin, setIsLogin] = useState(false);
    const [keepLogin, setKeepLogin] = useState(false);
    const [error, setError] = useState("");

    // for login and register (username=email)
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    // for register only
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [currencyId, setCurrencyId] = useState("");
    const [currencies, setCurrencies] = useState(CURRENCY_DATA);

    const navigate = useNavigate();
    const url = "http://localhost:8080/api/";
    const location = useLocation();
    const from = location.state?.from?.pathname || "/";
    const safeFrom = from && from !== "/auth" ? from : "/"; // redirect away from register

    // Get currencies with useEffect
    useEffect(() => {
        if (!isLogin) {
            fetch("http://localhost:8080/api/currency")
            .then(response => {
                if (!response.ok) {
                    return response.json();
                } else {
                    return Promise.reject(`Unexpected Status Code: ${response.status}`);
                }
            })
            .then(data => {
                if (Array.isArray(data)) {
                    setCurrencies(data);
                } else if (data.currencies) {
                    setCurrencies(data.currencies);
                } else {
                    setCurrencies(CURRENCY_DATA);
                }
            })
            .catch(console.log);
        }
    }, [isLogin]);

    // Redirect away if already logged in
    const token = localStorage.getItem("token");
    if (token) {
        return <Navigate to="/" replace />;
    }

    const handleSubmit = async (event) => {
        event.preventDefault();
        setError("");

        try {
            const apiEndpoint = isLogin ? "auth/authenticate" : "auth/register";
            const requestBody = isLogin ? { username: username, password: password }
                : {
                    username: username, password: password, firstName: firstName, lastName: lastName,
                    currencyId: Number(currencyId)
                };
                console.log("Request Body:", requestBody);


            const response = await fetch(`${url}${apiEndpoint}`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(requestBody)
            });

            const data = await response.json();
            if (response.ok) {
                if (isLogin) {
                    // login successful
                    if (data.jwt_token) {
                        if (keepLogin) {
                            localStorage.setItem("token", data.jwt_token);
                        } else {
                            sessionStorage.setItem("token", data.jwt_token);
                        }
                        alert("Login successful!");
                        navigate('/');
                    } else {
                        setError("Login failed: token not received.");
                    }
                } else {
                    // registration successful, no token returned from backend
                    alert("Registration successful! Please log in.");
                    setIsLogin(true);
                    // optionally reset registration fields here
                }
            } else {
                // Backend returns errors as array of strings
                if (Array.isArray(data)) {
                    setError(data.join(", "));
                } else if (data.message) {
                    setError(data.message);
                } else {
                    setError("Request failed");
                }
            }
        } catch (error) {
            setError(error);
        }

    };

    const handleChange = (event) => {
        switch (event.target.name) {
            case "username":
                setUsername(event.target.value);
                break;
            case "password":
                setPassword(event.target.value);
                break;
            case "firstName":
                setFirstName(event.target.value);
                break;
            case "lastName":
                setLastName(event.target.value);
                break;
            case "currency":
                setCurrencyId(Number(event.target.value));
                break;
            default:
                break;
        }
    };

    return (
        <>
            <Container maxWidth="sm">
                 {/* paper makes a drop shadow to make containers look 3d */}
                <Paper elevation={4} sx={{ mt: 4, p: 4, borderRadius: 10 }}>
                    <Typography variant="h2" align="center">
                        {isLogin ? "Login" : "Register"}
                    </Typography>
                    <Typography align="center" variant="h4" sx={{ mt: 4 }}>
                        Stock App
                    </Typography>

                    {/* error goes here */}
                    {error && <Alert severity="error" sx={{ p: 4 }}>
                        {error}</Alert>}


                    <Box component="form" onSubmit={handleSubmit} 
                        sx={{ display: "flex", flexDirection: "column"}}>
                     {/* register and login use username+password */}
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
                             {/* replace with a dropdown of currency and store as currency id? might have to call currency api in useEffect though?? */}
                                <FormControl fullWidth margin="normal">
                                    <InputLabel id="currency-label">Currency</InputLabel>
                                    <Select
                                        labelId="currency-label"
                                        name="currency"
                                        value={currencyId}
                                        label="Currency"
                                        onChange={handleChange}
                                    >
                                        {currencies.map((c) => (
                                            <MenuItem key={c.id} value={c.id}>
                                                {c.code} ({c.name})
                                            </MenuItem>
                                        ))}
                                    </Select>
                                </FormControl>
                            </>
                        )}

                        <FormControlLabel control={<Checkbox checked={keepLogin} onChange={(e) => setKeepLogin(e.target.checked)}/>}
                            label="Keep me logged in"
                        />        

                        <Button type="submit" variant="contained" color="primary" sx={{ mt: 2, borderRadius: 5, width: "50%", alignSelf: "center" }}>
                            {isLogin ? "Login" : "Register"}
                        </Button>
                    </Box>
                </Paper>
            </Container>

            <Typography align="center" sx={{ p: 4 }}>
                {isLogin ? (
                    <>
                        Don't have an account?{" "}
                        <Link component="button" type="button" underline="hover" onClick={() => setIsLogin(false)}>
                            Register for a free account
                        </Link>
                    </>
                ) : (
                    <>
                        Already have an account?{" "}
                        <Link component="button" type="button" underline="hover" onClick={() => setIsLogin(true)}>
                            Login
                        </Link>
                    </>
                )}
            </Typography>
        </>
    )


}

export default AuthPage;