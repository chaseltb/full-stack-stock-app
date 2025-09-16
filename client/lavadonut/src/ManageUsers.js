import { useState, useEffect } from "react";
import React from "react";
import { Container, Box, Typography, Paper, Button, TextField, Alert,
    Dialog, DialogContent, DialogActions } from "@mui/material";
import { Add, Edit, Delete} from "@mui/icons-material";

function ManageUsers() {
    const [users, setUsers] = useState([]);
    const [error, setError] = useState("");

    // whether or not the view/add dialog is open, and respective user
    const [viewDialog, setViewDialog] = useState(false);
    const [createDialog, setCreateDialog] = useState(false);
    const [selectedUser, setSelectedUser] = useState(null);

    // fields for add/edit user
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [currencyId, setCurrencyId] = useState("");

    // inner join user table and app_user and app_user_role?? maybe
    const [role, setRole] = useState("USER");

    const url = "http://localhost:8080/api/";

    // MOCK DATA!!
    const mockUsers = [
        { 
            id: 1, 
            username: "john.doe@email.com", 
            firstName: "John", 
            lastName: "Doe", 
            currencyId: 1, 
            currencyName: "USD",
            role: "USER",
            createdDate: "2024-01-15"
        },
        { 
            id: 2, 
            username: "jane.smith@email.com", 
            firstName: "Jane", 
            lastName: "Smith", 
            currencyId: 2, 
            currencyName: "EUR",
            role: "ADMIN",
            createdDate: "2024-02-20"
        },
        { 
            id: 3, 
            username: "mike.wilson@email.com", 
            firstName: "Mike", 
            lastName: "Wilson", 
            currencyId: 1, 
            currencyName: "USD",
            role: "USER",
            createdDate: "2024-03-10"
        },
        { 
            id: 4, 
            username: "sarah.jones@email.com", 
            firstName: "Sarah", 
            lastName: "Jones", 
            currencyId: 3, 
            currencyName: "GBP",
            role: "USER",
            createdDate: "2024-03-25"
        }
    ];

    const currencies = [
        { id: 1, code: "USD", name: "US Dollar" },
        { id: 2, code: "EUR", name: "Euro" },
        { id: 3, code: "GBP", name: "British Pound" },
        { id: 4, code: "CAD", name: "Canadian Dollar" }
    ];

    useEffect(() => {
        loadUsers();
    }, []);

    const loadUsers = async () => {
        setError("");

        try {
            // get users api call
        } catch (error) {
            setError(error);
        }
    };

    // open modal with user's details
    const handleViewUser = (user) => {
        setSelectedUser(user);
        setViewDialog(true);
    }

    const handleAdd = () => {
        console.log("add clicked");

        setUsername("");
        setPassword("");
        setFirstName("");
        setCurrencyId("");
        setRole("USER");
        setCreateDialog(true);
    }

    const handleEdit = () => {
        console.log("edit clicked");
    }

    const handleDelete = async (currencyId) => {
        if (window.confirm("Are you sure that you would like to delete this user?")) {
            try {
                // delete api call
            } catch (error) {
                setError(error);
            }
        }
    }

    const handleSubmit = async () => {
        try {
            if (editing) {
                // edit
            } else {
                // add
            }

        } catch (error) {
            setError(error);
        }
    }

    return (
        <Container>
            <Paper>
                <Typography>
                    Manage Users
                </Typography>

                 {error && <Alert severity="error" sx={{ mb: 4 }}>
                    {error}
                </Alert>}

                {/* users list  */}
            </Paper>

            {/* view dialog if open  */}


            {/* create dialog if open  */}

            {/* floating action button for add  */}
        </Container>
    );
}