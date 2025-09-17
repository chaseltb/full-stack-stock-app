import React from "react";
import { Route, BrowserRouter as Router, Routes } from "react-router-dom";
import AuthPage from "./AuthPage";
import NotFound from "./NotFound";
import CountryList from "./CountryList";
import RequireAuth from "./RequireAuth";
import CountryForm from "./CountryForm";
import OrderForm from "./OrderForm";
import OrderList from "./OrderList";
import Stock from "./Stock";
import ManageUsers from "./ManageUsers";
import ManageCurrencies from "./ManageCurrencies";
import Portfolios from "./Portfolios";
import OrderHistory from "./OrderHistory";
import NavBar from "./NavBar";
import HomeScreen from "./HomeScreen";
import ManageStockExchanges from "./ManageStockExchanges";

function App() {
  return (
    <Router>
      {/* NavBar route */}
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
        <Route path="/portfolios/order-history/:id" element={<RequireAuth> <OrderHistory/> </RequireAuth>}/>

        <Route path="/users" element={<ManageUsers />}/>
        <Route path="/currencies" element={<ManageCurrencies />}/>
        <Route path="/exchanges" element={<ManageStockExchanges />}/>
        {/* Catch-all */}
        <Route path="*" element={<NotFound />} />
      </Routes>
    </Router>
  );
}

export default App;