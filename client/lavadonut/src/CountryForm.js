import { useEffect, useState } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";

const COUNTRY_DEFAULT = {
    code: "",
    name: "",
    currency: {
        name: "",
        code: "",
        valueToUsd: 0.0
    }
}

function CountryForm() {
    // State management
    const [country, setCountry] = useState(COUNTRY_DEFAULT);
    const [errors, setErrors] = useState([]);
    const url = 'http://localhost:8080/api/country';

    // Hook for programmatic navigation
    const navigate = useNavigate();

    // Hook to access URL parameters
    const { id } = useParams();

    // useEffect
    useEffect(() => {
        if (id) {
            fetch(`${url}/${id}`)
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else {
                    return Promise.reject(`Unexpected Status Code: ${response.status}`);
                }
            })
            .then((data) => {
                setCountry(data);
            })
            .catch(console.log);
        } else {
            setCountry(COUNTRY_DEFAULT);
        }
    }, [id]);

    // Handle form submission
    const handleSubmit = (event) => {
        event.preventDefault();
        if (id) {
            updateCountry();
        } else {
            addCountry();
        }
    }

    // Handle input changes in form
    const handleChange = (event) => {
        const newCountry = {...country};
        newCountry[event.target.name] = event.target.value;
        setCountry(newCountry);
    }
}

export default CountryForm;