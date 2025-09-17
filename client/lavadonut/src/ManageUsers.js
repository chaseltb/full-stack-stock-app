import { useState, useEffect } from "react";
import React from "react";
import { Container, Box, Typography, Paper, Button, TextField, Alert,
    Dialog, DialogContent, DialogActions, Chip, MenuItem, Fab,
    IconButton, DialogTitle} from "@mui/material";
import { Add as AddIcon, Edit as EditIcon, Delete as DeleteIcon} from "@mui/icons-material";

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
                {users.map((user) => {
                    <Paper key={user.id} elevation={2} sx={{ mb: 2, p: 2, borderRadius: 4 }}>
                        <Box>
                            <Box>
                                <Typography>
                                    {user.firstName} {user.lastName}
                                </Typography>
                                <Typography variant="body1" color="textSecondary">
                                    {user.username}
                                </Typography>
                                <Box sx={{ p: 2}}>
                                    <Chip lable={user.role} size="small">
                                    </Chip>
                                    <Chip lable={user.currencyName} size="small">
                                    </Chip>
                                </Box>
                            </Box>
                        </Box>
                        <Button variant="outlined" onClick={() => handleViewUser(user)} sx={{ p: 2, borderRadius: 2 }}>
                            View User
                        </Button>
                    </Paper>
                })}

                {users.length === 0 && (
                    <Box sx={{ textAlign: "center" }}>
                        <Typography variant="h5">
                            No users were found
                        </Typography>
                        {/* maybe an add button? */}
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
                        <Box>
                            <Typography>
                                User id: {selectedUser.id}
                            </Typography>
                            <Typography>
                                Username: {selectedUser.username}
                            </Typography>
                            <Typography>
                                Full name: {selectedUser.firstName} {selectedUser.lastName}
                            </Typography>
                            
                            <Chip lable={selectedUser.role} size="small">
                            </Chip>
                            <Chip lable={selectedUser.currencyName} size="small">
                            </Chip>

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
                    <TextField
                        lable="Username"
                        type="email"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        fullWidth
                        margin="dense"
                        placeholder="name@stockapp.com" 
                    />
                    <TextField
                        lable="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        fullWidth
                        margin="dense" 
                    />
                    <Box sx={{ display: "flex", gap: 2 }}>
                        <TextField
                            lable="First Name"
                            value={firstName}
                            onChange={(e) => setFirstName(e.target.value)}
                            fullWidth
                            margin="dense"
                        />
                        <TextField
                            lable="Last Name"
                            value={lastName}
                            onChange={(e) => setLastName(e.target.value)}
                            fullWidth
                            margin="dense"
                        />
                    </Box>
                    <TextField
                        lable="Currency"
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
                        lable="Role"
                        select
                        value={role}
                        onChange={(e) => setCurrencyId(e.target.value)}
                        fullWidth
                        margin="dense"
                    >
                        <MenuItem value="USER">USER</MenuItem>
                        <MenuItem value="ADMIN">ADMIN</MenuItem>
                    </TextField>
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
                sx={{ position: 'fixed', bottom: 16, right: 16 }}
                onClick={handleAdd}
            >
                <AddIcon />
            </Fab>
        </Container>
    );
}

export default ManageUsers;