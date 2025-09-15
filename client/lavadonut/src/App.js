import AuthPage from "./AuthPage";
import HomeScreen from "./HomeScreen";
import { Routes, BrowserRouter as Router, Route, Navigate } from "react-router-dom";
import React from "react";

function App() {
  return (
    <>
      <Router>
        <Routes>
          <Route path="/" element={<AuthPage />}/>
          <Route path="/home" element={<HomeScreen />}/>
        </Routes>
      </Router>
    </>
  );
}

export default App;
