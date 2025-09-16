import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Box, Typography, IconButton, Card, CardContent, CardActions, Stack, Button } from "@mui/material";
import { Add, Edit, Delete } from "@mui/icons-material";

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

const COUNTRY_DATA = [
    {
        id: 1,
        name: 'United States of America',
        code: 'US',
        currency: CURRENCY_DATA[0]
    },
    {
        id: 2,
        name: 'Federal Republic of Germany',
        code: 'DE',
        currency: CURRENCY_DATA[1]
    },
    {
        id: 3,
        name: 'People\'s Republic of China',
        code: 'CN',
        currency: CURRENCY_DATA[3]
    }
];

function CountryList() {
    // State variables
    // const [countries, setCountries] = useState([]);
    const [countries, setCountries] = useState(COUNTRY_DATA);
    const url = 'http://localhost:8080/api/country';

    // Use Effect
    useEffect(() => {
        const token = localStorage.getItem('token');
        fetch(url, {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
            if (response.status === 200) {
                return response.json();
            } else {
                return Promise.reject(`Unexpected Status Code: ${response.status}`);
            }
        })
        .then(data => setCountries(data))
        .catch(console.log);
    }, []);

    // Handle deleting a country
    const handleDeleteCountry = (id) => {
        const token = localStorage.getItem('token');
        const country = countries.find(c => c.id === id);
        if (window.confirm(`Delete Country: ${country.id} - ${country.code}`)) {
            const init = {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            };
            fetch(`${url}/${id}`, init)
            .then(response => {
                if (response.status === 200) {
                    const newCountries = countries.filter(c => c.id !== id);
                    setCountries(newCountries);
                } else {
                    return Promise.reject(`Unexpected Status Code: ${response.status}`);
                }
            })
            .catch(console.log);
        }
    }

    return (
        <>
            <Box sx={{ padding: 4 }}>
                {/* <h2>List of Countries</h2>
                <Link
                    to={'/country/add'}
                >
                    Add a Country
                </Link> */}
                <Stack 
                    direction="row" 
                    justifyContent="space-between" 
                    alignItems="center"
                    mb={4}
                >
                    <Typography variant="h2" fontWeight="bold">
                        List of Countries ({countries.length})
                    </Typography>
                    <Button 
                        variant="contained" 
                        color="primary"
                        component={Link}
                        to={'/country/add'}
                        startIcon={<Add />}
                    >
                        Add a Country
                    </Button>
                </Stack>
                {/* <table>
                    <thead>
                        <tr>
                            <th>Country Code</th>
                            <th>Country Name</th>
                            <th>Currency Code</th>
                            <th>Currency Name</th>
                            <th>Value to USD</th>
                            <th>&nbsp;</th>
                        </tr>
                    </thead>
                    <tbody>
                        {countries.map((country) => (
                            <tr key={country.id}>
                                <td>{country.code}</td>
                                <td>{country.name}</td>
                                <td>{country.currency?.code}</td>
                                <td>{country.currency?.name}</td>
                                <td>{country.currency?.valueToUsd}</td>
                                <td>
                                    <Link
                                        to={`country/edit/${country.id}`}
                                    >
                                        Edit
                                    </Link>
                                    <button
                                        onClick={() => handleDeleteCountry(country.id)}
                                    >
                                        Delete
                                    </button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table> */}
                <Stack spacing={3}>
                    {countries.map((country) => (
                        <Card key={country.id} variant="outlined" sx={{ backgroundColor: "#f4f4f4" }}>
                            <CardContent>
                                <Typography variant="h6" gutterBottom>
                                    {country.code} - {country.name}
                                </Typography>
                                <Typography variant="body2" color="textSecondary">
                                    Currency: {country.currency.code} - {country.currency.name}
                                </Typography>
                                <Typography variant="body2" color="textSecondary">
                                    Value to USD: {country.currency.valueToUsd}
                                </Typography>
                            </CardContent>
                            <CardActions sx={{ justifyContent: "flex-end" }}>
                                <IconButton
                                    color="primary"
                                    component={Link}
                                    to={`/country/edit/${country.id}`}
                                >
                                    <Edit />
                                </IconButton>
                                <IconButton
                                    color="error"
                                    onClick={() => handleDeleteCountry(country.id)}
                                >
                                    <Delete />
                                </IconButton>
                            </CardActions>
                        </Card>
                    ))}
                </Stack>
            </Box>
        </>
    );
}

export default CountryList;