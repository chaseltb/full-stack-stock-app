import { Navigate, useLocation } from "react-router-dom";

function RequireAuth({ children }) {
    const token = localStorage.getItem('token') || sessionStorage.getItem('token');
    const location = useLocation();

    // Redirect to register if token is not valid
    if (!token) {
        return <Navigate to="/auth" state={{ from: location }} replace />;
    }

    return children;
}

export default RequireAuth;