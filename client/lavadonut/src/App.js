import { Route, BrowserRouter as Router, Routes } from "react-router-dom";
import AuthPage from "./AuthPage";
import NotFound from "./NotFound";
import CountryList from "./CountryList";
import RequireAuth from "./RequireAuth";
import CountryForm from "./CountryForm";

function App() {
  return (
    <>
      <Router>
        <Routes>
          <Route path="/" element={<AuthPage />}/>
          <Route path="/countries" element={<CountryList />}/>
          <Route path="/country/add" element={<RequireAuth> <CountryForm /> </RequireAuth>}/>
          <Route path="/country/edit/:id" element={<RequireAuth> <CountryForm /> </RequireAuth>}/>
          <Route path="*" element={<NotFound />}/>
        </Routes>
      </Router>
    </>
  );
}

export default App;
