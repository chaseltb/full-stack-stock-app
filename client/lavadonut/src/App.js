import { Route, BrowserRouter as Router, Routes } from "react-router-dom";
import './App.css';
import NotFound from "./NotFound";
import CountryList from "./CountryList";

function App() {
  return (
    <>
      <Router>
        <Routes>
          <Route path="/countries" element={<CountryList />}/>
          <Route path="*" element={<NotFound />}/>
        </Routes>
      </Router>
    </>
  );
}

export default App;
