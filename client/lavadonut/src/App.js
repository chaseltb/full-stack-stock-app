import React from "react";
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";

import './App.css';

import AuthPage from "./AuthPage";
import NotFound from "./NotFound";
import CountryList from "./CountryList";
import RequireAuth from "./RequireAuth";
import CountryForm from "./CountryForm";

function App() {
  return (
    <Router>
      <Routes>
        {/* Auth route */}
        <Route path="/" element={<AuthPage />} />

        {/* Protected routes */}
        <Route path="/countries"
          element={<RequireAuth> <CountryList /> </RequireAuth>}/>
        <Route path="/country/add" element={<RequireAuth> <CountryForm /> </RequireAuth>} />
        <Route path="/country/edit/:id" element={<RequireAuth> <CountryForm /> </RequireAuth>}/>

        {/* Catch-all */}
        <Route path="*" element={<NotFound />} />
      </Routes>
    </Router>
  );
}

export default App;
