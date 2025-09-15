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
            const token = localStorage.getItem('token');
            fetch(url, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            })
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
        const { name, value } = event.target;
        if (name.startsWith('currency.')) {
            const field = name.split('.')[1];
            setCountry({
                ...country,
                currency: {
                    ...country.currency,
                    [field]: value
                }
            });
        } else {
            setCountry({
                ...country,
                [name]: value
            });
        }
    }

    // CRUD Functions
    const addCountry = () => {
        const token = localStorage.getItem('token');
        const init = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(country)
        };

        fetch(url, init)
        .then(response => {
            if (response.status === 201 || response.status === 400) {
                return response.json();
            } else {
                return Promise.reject(`Unexpected Status Code: ${response.status}`);
            }
        })
        .then(data => {
            if (data.id) {
                navigate('/');
            } else {
                setErrors(data);
            }
        })
        .catch(console.log);
    }

    const updateCountry = () => {
        country.id = id;
        const token = localStorage.getItem('token');
        const init = {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(country)
        };

        fetch(`${url}/${id}`, init)
        .then(response => {
            if (response.status === 204) {
                return null;
            } else if (response.status === 400) {
                return response.json();
            } else {
                return Promise.reject(`Unexpected Status Code ${response.status}`);
            }
        })
        .then(data => {
            if (data.id) {
                navigate('/');
            } else {
                setErrors(data);
            }
        })
        .catch(console.log);
    }

    return (
        <>
            <section>
                <h2>
                    {id > 0 ? "Update a Country" : "Add a Country"}
                </h2>
                {errors.length > 0 && (
                    <div>
                        <p>The following errors were found:</p>
                        <ul>
                            {errors.map((error) => (
                                <li key={error}>{error}</li>
                            ))}
                        </ul>
                    </div>
                )}
                <form onSubmit={handleSubmit}>
                    <fieldset>
                        <label htmlFor="countryCode">Country Code</label>
                        <input
                            id="countryCode"
                            name="countryCode"
                            type="text"
                            value={country.code}
                            onChange={handleChange}
                        />
                    </fieldset>
                    <fieldset>
                        <label htmlFor="countryName">Country Name</label>
                        <input
                            id="countryName"
                            name="countryName"
                            type="text"
                            value={country.name}
                            onChange={handleChange}
                        />
                    </fieldset>
                    <fieldset>
                        <label htmlFor="currencyCode">Currency Code</label>
                        <input
                            id="currencyCode"
                            name="currencyCode"
                            type="text"
                            value={country.currency.code}
                            onChange={handleChange}
                        />
                    </fieldset>
                    <fieldset>
                        <label htmlFor="currencyName">Currency Name</label>
                        <input
                            id="currencyName"
                            name="currencyName"
                            type="text"
                            value={country.currency.name}
                            onChange={handleChange}
                        />
                    </fieldset>
                    <fieldset>
                        <label htmlFor="valueToUsd">Value to USD</label>
                        <input
                            id="valueToUsd"
                            name="valueToUsd"
                            type="number"
                            value={country.currency.valueToUsd}
                            onChange={handleChange}
                        />
                    </fieldset>
                    <div>
                        <button type="submit">
                            {id > 0 ? "Update" : "Submit"}
                        </button>
                        <Link type="button" to={'/countries'}>
                            Cancel
                        </Link>
                    </div>
                </form>
            </section>
        </>
    );
}

export default CountryForm;