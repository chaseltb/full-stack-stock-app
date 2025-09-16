import AuthPage from "./AuthPage";
import OrderHistory from "./OrderHistory";
import { Routes, BrowserRouter as Router, Route, Navigate } from "react-router-dom";
import React from "react";

function App() {
  return (
    <>
      <Router>
        <Routes>
          <Route path="/" element={<AuthPage />}/>
          <Route path="/OrderHistory/:name/:id" element={<OrderHistory />}/>
        </Routes>
      </Router>
    </>
  );
}

export default App;
