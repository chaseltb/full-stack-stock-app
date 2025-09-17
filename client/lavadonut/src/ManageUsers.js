import { useState, useEffect } from "react";
import React from "react";
import {
    Container, Box, Typography, Paper, Button, TextField, Alert,
    Dialog, DialogContent, DialogActions, Chip, MenuItem, Fab,
    IconButton, DialogTitle
} from "@mui/material";
import { Add as AddIcon, Edit as EditIcon, Delete as DeleteIcon, Visibility as ViewIcon } from "@mui/icons-material";

function ManageUsers() {
    const [users, setUsers] = useState([]);
    const [error, setError] = useState("");
    const [editing, setEditing] = useState(false);

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

    const url = "http://localhost:8080/api/users";

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

    const loadUsers = () => {
        setError("");

        try {
            // get users api call
            setUsers(mockUsers);
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
        setLastName("");
        setCurrencyId("");
        setRole("USER");
        setCreateDialog(true);
    }

    const handleEdit = (user) => {
        console.log("edit clicked");
        setEditing(true);
        setSelectedUser(user);

        // user data in dialog window
        setUsername(user.username);
        setPassword("********");
        setFirstName(user.firstName);
        setLastName(user.lastName);
        setCurrencyId(user.currencyId);
        setRole(user.role);

        setCreateDialog(true);
    }

    const handleDelete = async (userId) => {
        if (window.confirm("Are you sure that you would like to delete this user?")) {
            try {
                // delete api call
                setUsers(users.filter((u) => u.id !== userId));
            } catch (error) {
                setError(error);
            }
        }
    }

    const handleSubmit = async () => {
        const newUser = {
            id: users.length + 1,
            username,
            password,
            firstName,
            lastName,
            currencyId,
            currencyName: currencies.find(c => c.id === Number(currencyId))?.code || "",
            role
        };
        setUsers([...users, newUser]);
        setCreateDialog(false);

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
        <Container maxWidth="lg">
            <Paper elevation={4} sx={{ mt: 4, p: 4, borderRadius: 10 }}>
                <Typography sx={{ mb: 4 }}>
                    Manage Users
                </Typography>

                {error && <Alert severity="error" sx={{ mb: 4 }}>
                    {error}
                </Alert>}

                {/* users list  */}
                {users.map((user) => (
                    <Paper key={user.id} elevation={2} sx={{ mb: 2, p: 1, borderRadius: 4 }}>
                        <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                            <Box sx={{ ml: 2 }}>
                                <Typography sx={{ mb: 1 }}>
                                    ID: {user.id} - {user.firstName} {user.lastName}
                                </Typography>
                                <Typography variant="body1" color="textSecondary">
                                    Username: {user.username}
                                </Typography>
                                <Box sx={{ mb: 1, mt: 2, display: "flex", gap: 2 }}>
                                    <Chip label={user.role} size="small">
                                    </Chip>
                                    <Chip label={user.currencyName} size="small">
                                    </Chip>
                                </Box>
                            </Box>
                            <Box>
                                <Button variant="outlined" startIcon={<ViewIcon />} onClick={() => handleViewUser(user)} sx={{ mr: 2, color: "light-blue", borderRadius: 4 }}>
                                    View User
                                </Button>
                                <Button variant="outlined" color="secondary" startIcon={<EditIcon />} onClick={() => handleEdit(user)} sx={{ mr: 2, borderRadius: 4 }}>
                                    Edit
                                </Button>
                                <Button variant="outlined" color="error" startIcon={<DeleteIcon />} onClick={() => handleDelete(user.id)} sx={{ borderRadius: 4 }}>
                                    Delete
                                </Button>
                            </Box>
                        </Box>

                    </Paper>
                ))}

                {users.length === 0 && (
                    <Box sx={{ textAlign: "center" }}>
                        <Typography variant="h5">
                            No users were found
                        </Typography>
                        <IconButton
                            label="Add a currency"
                            color="primary"
                            onClick={() => handleAdd()}
                            sx={{ p: 2, borderRadius: 2 }}
                        >
                            <AddIcon />
                        </IconButton>
                    </Box>
                )}
            </Paper>

            {/* view dialog if open  */}
            <Dialog open={viewDialog} onClose={() => setViewDialog(false)}>
                <DialogTitle>
                    View User:
                </DialogTitle>
                <DialogContent>
                    {selectedUser && (
                        <Box sx={{ display: "flex", flexDirection: "column", gap: 5 }}>
                            <Typography>
                                User id: {selectedUser.id}
                            </Typography>
                            <Typography>
                                Username: {selectedUser.username}
                            </Typography>
                            <Typography>
                                Full name: {selectedUser.firstName} {selectedUser.lastName}
                            </Typography>
                            <Box sx={{ display: "flex", gap: 2 }}>
                                <Chip label={selectedUser.role} size="small">
                                </Chip>
                                <Chip label={selectedUser.currencyName} size="small">
                                </Chip>
                            </Box>
                        </Box>
                    )}
                </DialogContent>
            </Dialog>


            {/* create dialog if open  */}
            <Dialog open={createDialog} onClose={() => setCreateDialog(false)} maxWidth="sm">
                <DialogTitle>
                    Create New User
                </DialogTitle>
                <DialogContent>
                    <Box sx={{ display: "flex", flexDirection: "column", gap: 3 }}>
                        <TextField
                            label="Username"
                            type="email"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            fullWidth
                            margin="dense"
                            placeholder="name@stockapp.com"
                        />
                        <TextField
                            label="Password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            fullWidth
                            margin="dense"
                        />
                        <Box sx={{ display: "flex", gap: 1 }}>
                            <TextField
                                label="First Name"
                                value={firstName}
                                onChange={(e) => setFirstName(e.target.value)}
                                fullWidth
                                margin="dense"
                            />
                            <TextField
                                label="Last Name"
                                value={lastName}
                                onChange={(e) => setLastName(e.target.value)}
                                fullWidth
                                margin="dense"
                            />
                        </Box>
                        <TextField
                            label="Currency"
                            select
                            value={currencyId}
                            onChange={(e) => setCurrencyId(e.target.value)}
                            fullWidth
                            margin="dense"
                        >
                            {currencies.map((currency) => (
                                <MenuItem key={currency.id} value={currency.id}>
                                    {currency.name} ({currency.code})
                                </MenuItem>
                            ))}
                        </TextField>
                        <TextField
                            label="Role"
                            select
                            value={role}
                            onChange={(e) => setRole(e.target.value)}
                            fullWidth
                            margin="dense"
                        >
                            <MenuItem value="USER">USER</MenuItem>
                            <MenuItem value="ADMIN">ADMIN</MenuItem>
                        </TextField>
                    </Box>
                </DialogContent>
                <DialogActions sx={{ p: 2 }}>
                    <Button onClick={() => setCreateDialog(false)} sx={{ borderRadius: 4 }}>
                        Cancel
                    </Button>
                    <Button onClick={handleSubmit} variant="contained" sx={{ borderRadius: 4 }}>
                        Create User
                    </Button>
                </DialogActions>
            </Dialog>

            {/* floating action button for add  */}
            <Fab
                color="primary"
                sx={{ position: 'fixed', bottom: 36, right: 36 }}
                onClick={handleAdd}
            >
                <AddIcon />
            </Fab>
        </Container>
    );
}

export default ManageUsers;