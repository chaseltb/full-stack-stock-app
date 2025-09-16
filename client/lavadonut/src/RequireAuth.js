import { Navigate, useLocation } from "react-router-dom";

function RequireAuth({ children }) {
    const token = localStorage.getItem('token');
    const location = useLocation();

    // Redirect to login if token is not valid
    if (!token) {
        return <Navigate to="/" state={{ from: location }} replace />;
    }

    return children;
}

export default RequireAuth;