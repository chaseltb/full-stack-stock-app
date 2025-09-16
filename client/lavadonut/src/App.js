import React from "react";
import {Route, BrowserRouter as Router, Routes } from "react-router-dom";
import AuthPage from "./AuthPage";
import NotFound from "./NotFound";
import CountryList from "./CountryList";
import RequireAuth from "./RequireAuth";
import CountryForm from "./CountryForm";
import HomeScreen from "./HomeScreen";
import Portfolios from "./Portfolios";
import Stock from "./Stock";
import OrderList from "./OrderList";
import OrderForm from "./OrderForm";

function App() {
  return (
    <Router>
      <Routes>
        {/* Auth route */}
        <Route path="/" element={<AuthPage />} />

        {/* Protected routes */}
        <Route path="/countries" element={<RequireAuth> <CountryList /> </RequireAuth>}/>
        <Route path="/country/add" element={<RequireAuth> <CountryForm /> </RequireAuth>} />
        <Route path="/country/edit/:id" element={<RequireAuth> <CountryForm /> </RequireAuth>}/>
        <Route path="/orders" element={<RequireAuth> <OrderList /> </RequireAuth>}/>
        <Route path="/order/add" element={<RequireAuth> <OrderForm /> </RequireAuth>} />
        <Route path="/order/edit/:id" element={<RequireAuth> <OrderForm /> </RequireAuth>}/>
        <Route path="/home" element={<RequireAuth> <HomeScreen /> </RequireAuth>}/>
        <Route path="/stock" element={<RequireAuth> <Stock /> </RequireAuth>}/>
        <Route path="/portfolios" element={<RequireAuth> <Portfolios /> </RequireAuth>}/>

        {/* Catch-all */}
        <Route path="*" element={<NotFound />} />
      </Routes>
    </Router>
  );
}

export default App;