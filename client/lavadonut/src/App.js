import AuthPage from "./AuthPage";
import { Routes, BrowserRouter as Router, Route, Navigate } from "react-router-dom";
import React from "react";

function App() {
  return (
    <>
      <Router>
        <Routes>
          <Route path="/" element={<AuthPage />}/>
        </Routes>
      </Router>
    </>
  );
}

export default App;
