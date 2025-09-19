import { useState, useEffect, useCallback } from "react";
import React from "react";
import {
    Container, Box, Typography, Paper, Button, TextField, Alert,
    Dialog, DialogContent, DialogActions, Chip, MenuItem, Fab,
    IconButton, DialogTitle
} from "@mui/material";
import { Add as AddIcon, Edit as EditIcon, Delete as DeleteIcon, Visibility as ViewIcon } from "@mui/icons-material";

// MOCK DATA
// const mockUsers = [
//     { userId: 1, username: "john.doe@email.com", firstName: "John", lastName: "Doe", currencyId: 1, appUserId: 1 },
//     { userId: 2, username: "jane.smith@email.com", firstName: "Jane", lastName: "Smith", currencyId: 2, appUserId: 2 },
//     { userId: 3, username: "mike.wilson@email.com", firstName: "Mike", lastName: "Wilson", currencyId: 1, appUserId: 1 },
//     { userId: 4, username: "sarah.jones@email.com", firstName: "Sarah", lastName: "Jones", currencyId: 3, appUserId: 1 }
// ];
const mockUsers = [
    { userId: 1, firstName: "John", lastName: "Doe", currencyId: 1, appUserId: 1 },
    { userId: 2, firstName: "Jane", lastName: "Smith", currencyId: 2, appUserId: 2 },
    { userId: 3, firstName: "Mike", lastName: "Wilson", currencyId: 1, appUserId: 1 },
    { userId: 4, firstName: "Sarah", lastName: "Jones", currencyId: 3, appUserId: 1 }
];

const currencies = [
    { id: 1, code: "USD", name: "US Dollar" },
    { id: 2, code: "EUR", name: "Euro" },
    { id: 3, code: "GBP", name: "British Pound" },
    { id: 4, code: "CAD", name: "Canadian Dollar" }
];

const mockRoles = { 1: "USER", 2: "ADMIN" };
const roleToAppUserId = { USER: 1, ADMIN: 2 };

function ManageUsers() {
    const [users, setUsers] = useState([]);
    const [error, setError] = useState("");
    const [editing, setEditing] = useState(false);
    const [viewDialog, setViewDialog] = useState(false);
    const [createDialog, setCreateDialog] = useState(false);
    const [selectedUser, setSelectedUser] = useState(null);

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [currencyId, setCurrencyId] = useState("");
    const [role, setRole] = useState("USER");

    const token = localStorage.getItem("token") || sessionStorage.getItem("token");
    const url = "http://localhost:8080/api/user/";

    const loadUsers = useCallback(async () => {
        setError("");
        try {
            const response = await fetch(url, { headers: { "Content-Type": "application/json", "Authorization": `Bearer ${token}` } });
            if (!response.ok) throw new Error(`Unexpected Status Code ${response.status}`);
            const rawUsers = await response.json();
            const enrichedUsers = rawUsers.map(user => {
                const currency = currencies.find(c => c.id === user.currencyId);
                return {
                    ...user,
                    currencyName: currency ? currency.name : "Unknown",
                    role: mockRoles[user.appUserId] || "USER"
                };
            });
            setUsers(enrichedUsers);
        } catch (error) {
            setError(error.message || "Failed to load users");
            const enrichedMockUsers = mockUsers.map(user => {
                const currency = currencies.find(c => c.id === user.currencyId);
                return { ...user, currencyName: currency ? currency.name : "Unknown", role: mockRoles[user.appUserId] || "USER" };
            });
            setUsers(enrichedMockUsers);
        }
    }, [token]);

    useEffect(() => { loadUsers(); }, [loadUsers]);

    const resetForm = () => {
        setEditing(false);
        setSelectedUser(null);
        setUsername("");
        setPassword("");
        setFirstName("");
        setLastName("");
        setCurrencyId("");
        setRole("USER");
    };

    const handleViewUser = (user) => { setSelectedUser(user); setViewDialog(true); };

    const handleAdd = () => { resetForm(); setCreateDialog(true); };

    const handleEdit = (user) => {
        setEditing(true);
        setSelectedUser(user);
        setUsername(user.username);
        setPassword("********");
        setFirstName(user.firstName);
        setLastName(user.lastName);
        setCurrencyId(Number(user.currencyId));
        setRole(mockRoles[Number(user.appUserId)]);
        setCreateDialog(true);
    };

    // const handleDelete = async (userId) => {
    //     if (window.confirm("Are you sure that you would like to delete this user?")) {
    //         try {
    //             const response = await fetch(`${url}${userId}`, { method: "DELETE", headers: { "Authorization": `Bearer ${token}` } });
    //             if (!response.ok) throw new Error(`Unexpected Status Code ${response.status}`);
    //             await loadUsers();
    //         } catch (error) { setError(error.message || "Failed to delete user"); }
    //     }
    // };
    const handleDelete = async (userId) => {
    if (window.confirm("Are you sure that you would like to delete this user?")) {
        try {
            const response = await fetch(`${url}${userId}`, {
                method: "DELETE",
                headers: { "Authorization": `Bearer ${token}` }
            });

            const data = await response.text(); // Could be JSON or plain error message
            console.log("Delete response:", response.status, data);

            if (!response.ok) {
                throw new Error(`Delete failed: ${response.status} - ${data}`);
            }

            await loadUsers();
        } catch (error) {
            setError(error.message || "Failed to delete user");
        }
    }
};

    const handleSubmit = async () => {
        setCreateDialog(false);
        try {
            const userPayload = {
                firstName,
                lastName,
                currencyId: Number(currencyId),
                appUserId: roleToAppUserId[role]
            };
            if (password && password !== "********") { userPayload.password = password; }

            if (editing && selectedUser) {
                userPayload.userId = selectedUser.userId;
                console.log("Edit payload", userPayload);
                await fetch(`${url}${selectedUser.userId}`, { method: "PUT", headers: { "Content-Type": "application/json", "Authorization": `Bearer ${token}` }, body: JSON.stringify(userPayload) });
            } else {
                userPayload.userId = 0;
                await fetch(url, { method: "POST", headers: { "Content-Type": "application/json", "Authorization": `Bearer ${token}` }, body: JSON.stringify(userPayload) });
            }

            await loadUsers();
        } catch (error) { setError(error.message || "Failed to create or edit user"); }

        resetForm();
    };

    return (
        <Container maxWidth="lg">
            <Paper elevation={4} sx={{ mt: 4, p: 4, borderRadius: 10 }}>
                <Typography sx={{ mb: 2 }}>Manage Users</Typography>
                {/* {error && <Alert severity="error" sx={{ mb: 4 }}>{error}</Alert>} */}

                {users.map(user => (
                    <Paper key={user.userId} elevation={2} sx={{ mb: 2, p: 1, borderRadius: 4 }}>
                        <Box sx={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                            <Box sx={{ ml: 2 }}>
                                <Typography>ID: {user.userId} - {user.firstName} {user.lastName}</Typography>
                                <Typography variant="body1" color="textSecondary">Username: {user.username}</Typography>
                                <Box sx={{ mt: 1, display: "flex", gap: 2 }}>
                                    <Chip label={user.role} size="small" />
                                    <Chip label={currencies.find(c => c.id === Number(user.currencyId))?.code || "Unknown"} size="small" />
                                </Box>
                            </Box>
                            <Box>
                                <Button variant="outlined" startIcon={<ViewIcon />} onClick={() => handleViewUser(user)} sx={{ mr: 2, color: "blue", borderRadius: 4 }}>View User</Button>
                                <Button variant="outlined" color="secondary" startIcon={<EditIcon />} onClick={() => handleEdit(user)} sx={{ mr: 2, borderRadius: 4 }}>Edit</Button>
                                <Button variant="outlined" color="error" startIcon={<DeleteIcon />} onClick={() => handleDelete(user.userId)} sx={{ borderRadius: 4 }}>Delete</Button>
                            </Box>
                        </Box>
                    </Paper>
                ))}

                {users.length === 0 && (
                    <Box sx={{ textAlign: "center" }}>
                        <Typography variant="h5">No users were found</Typography>
                        <IconButton color="primary" onClick={handleAdd} sx={{ p: 2, borderRadius: 2 }}><AddIcon /></IconButton>
                    </Box>
                )}
            </Paper>

            {/* View Dialog */}
            <Dialog open={viewDialog} onClose={() => setViewDialog(false)}>
                <DialogTitle>View User</DialogTitle>
                <DialogContent>
                    {selectedUser && (
                        <Box sx={{ display: "flex", flexDirection: "column", gap: 2 }}>
                            <Typography>User ID: {selectedUser.userId}</Typography>
                            <Typography>Username: {selectedUser.username}</Typography>
                            <Typography>Full Name: {selectedUser.firstName} {selectedUser.lastName}</Typography>
                            <Box sx={{ display: "flex", gap: 2 }}>
                                <Chip label={selectedUser.role} size="small" />
                                <Chip label={currencies.find(c => c.id === Number(selectedUser.currencyId))?.code || "Unknown"} size="small" />
                            </Box>
                        </Box>
                    )}
                </DialogContent>
            </Dialog>

            {/* Create/Edit Dialog */}
            <Dialog open={createDialog} onClose={() => setCreateDialog(false)} maxWidth="sm">
                <DialogTitle>{editing ? "Edit User" : "Create New User"}</DialogTitle>
                <DialogContent>
                    <Box sx={{ display: "flex", flexDirection: "column", gap: 3 }}>
                        <TextField label="Username" type="email" value={username} onChange={(e) => setUsername(e.target.value)} fullWidth margin="dense" placeholder="name@stockapp.com" />
                        <TextField label="Password" value={password} onChange={(e) => setPassword(e.target.value)} fullWidth margin="dense" placeholder={editing ? "Leave blank to keep unchanged" : ""} />
                        <Box sx={{ display: "flex", gap: 1 }}>
                            <TextField label="First Name" value={firstName} onChange={(e) => setFirstName(e.target.value)} fullWidth margin="dense" />
                            <TextField label="Last Name" value={lastName} onChange={(e) => setLastName(e.target.value)} fullWidth margin="dense" />
                        </Box>
                        <TextField label="Currency" select value={currencyId} onChange={(e) => setCurrencyId(e.target.value)} fullWidth margin="dense">
                            {currencies.map(currency => <MenuItem key={currency.id} value={currency.id}>{currency.name} ({currency.code})</MenuItem>)}
                        </TextField>
                        <TextField label="Role" select value={role} onChange={(e) => setRole(e.target.value)} fullWidth margin="dense">
                            <MenuItem value="USER">USER</MenuItem>
                            <MenuItem value="ADMIN">ADMIN</MenuItem>
                        </TextField>
                    </Box>
                </DialogContent>
                <DialogActions sx={{ p: 2 }}>
                    <Button onClick={() => setCreateDialog(false)} sx={{ borderRadius: 4 }}>Cancel</Button>
                    <Button onClick={handleSubmit} variant="contained" sx={{ borderRadius: 4 }}>{editing ? "Update User" : "Create User"}</Button>
                </DialogActions>
            </Dialog>

            {/* Floating Add Button */}
            <Fab color="primary" sx={{ position: 'fixed', bottom: 36, right: 36 }} onClick={handleAdd}>
                <AddIcon />
            </Fab>
        </Container>
    );
}

export default ManageUsers;
