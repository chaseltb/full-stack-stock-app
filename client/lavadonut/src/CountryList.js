import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

function CountryList() {
    // State variables
    const [countries, setCountries] = useState([]);
    const url = 'http://localhost:8080/api/country';

    // Use Effect
    useEffect(() => {
        fetch(url)
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

    // Handle deleting an agent
    const handleDeleteCountry = (id) => {
        const country = countries.find(c => c.id === id);
        if (window.confirm(`Delete Country: ${country.id} - ${country.code}`)) {
            const init = {
                method: 'DELETE'
            };
            fetch(`${url}/${id}`, init)
            .then(response => {
                if (response === 200) {
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
            <section>
                <h2>List of Countries</h2>
                <Link
                    to={'/country/add'}
                >
                    Add a Country
                </Link>
                <table>
                    <thead>
                        <tr>
                            <th>Country Code</th>
                            <th>Country Name</th>
                            <th>&nbsp;</th>
                        </tr>
                    </thead>
                    <tbody>
                        {countries.map((country) => (
                            <tr key={country.id}>
                                <td>{country.code}</td>
                                <td>{country.name}</td>
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
                </table>
            </section>
        </>
    );
}

export default CountryList;