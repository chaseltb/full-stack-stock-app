import { Route, BrowserRouter as Router, Routes } from "react-router-dom";
import AuthPage from "./AuthPage";
import NotFound from "./NotFound";
import CountryList from "./CountryList";
import RequireAuth from "./RequireAuth";
import CountryForm from "./CountryForm";
import OrderList from "./OrderList";
import OrderForm from "./OrderForm";

function App() {
  return (
    <>
      <Router>
        <Routes>
          <Route path="/" element={<AuthPage />}/>
          <Route path="/countries" element={<CountryList />}/>
          <Route path="/country/add" element={<RequireAuth> <CountryForm /> </RequireAuth>}/>
          <Route path="/country/edit/:id" element={<RequireAuth> <CountryForm /> </RequireAuth>}/>
          <Route path="/orders" element={<RequireAuth> <OrderList /> </RequireAuth>}/>
          <Route path="/order/add" element={ <OrderForm /> }/>
          <Route path="/order/edit/:id" element={<RequireAuth> <OrderForm /> </RequireAuth>}/>
          <Route path="*" element={<NotFound />}/>
        </Routes>
      </Router>
    </>
  );
}

export default App;
