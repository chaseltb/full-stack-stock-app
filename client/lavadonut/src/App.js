import AuthPage from "./AuthPage";
import HomeScreen from "./HomeScreen";
import { Routes, BrowserRouter as Router, Route, Navigate } from "react-router-dom";
import React from "react";

import NotFound from "./NotFound";
import CountryList from "./CountryList";
import RequireAuth from "./RequireAuth";
import CountryForm from "./CountryForm";
import OrderForm from "./OrderForm";
import OrderList from "./OrderList";
import Stock from "./Stock";
import Portfolios from "./Portfolios";
import OrderHistory from "./OrderHistory";
import NavBar from "./NavBar";

function App() {
  return (
    <Router>
      {/* NabBar route */}
      <NavBar />
      <Routes>
        {/* Auth route */}
        <Route path="/auth" element={<AuthPage />} />

        {/* Home route */}
        <Route path="/" element={<HomeScreen />}/>

        {/* Protected routes */}
        <Route path="/countries" element={<RequireAuth> <CountryList /> </RequireAuth>}/>
        <Route path="/country/add" element={<RequireAuth> <CountryForm /> </RequireAuth>} />
        <Route path="/country/edit/:id" element={<RequireAuth> <CountryForm /> </RequireAuth>}/>
        <Route path="/orders" element={<RequireAuth> <OrderList /> </RequireAuth>}/>
        <Route path="/order/add" element={<RequireAuth> <OrderForm /> </RequireAuth>} />
        <Route path="/order/edit/:id" element={<RequireAuth> <OrderForm /> </RequireAuth>}/>
        <Route path="/stock" element={<RequireAuth> <Stock /> </RequireAuth>}/>
        <Route path="/portfolios" element={<RequireAuth> <Portfolios /> </RequireAuth>}/>
        <Route path="/order/history" element={<RequireAuth> <OrderHistory /> </RequireAuth>}/>

        {/* Catch-all */}
        <Route path="*" element={<NotFound />} />
      </Routes>
    </Router>
  );
}

export default App;