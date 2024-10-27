import { Navigate } from "react-router-dom";
import useAuth from "../context/useAuth";
import PropTypes from "prop-types";
import { useLocation } from "react-router-dom";

function ProtectedRoute({ children, allowedRoles }) {
  const { roles, isAuth, loading } = useAuth();
  const location = useLocation();

  if (loading) {
    return <div>Loading...</div>;
  }

  if (!isAuth) {
    return (
      <Navigate to="/auth/login" replace={true} state={{ from: location }} />
    );
  }

  if (allowedRoles && !allowedRoles.includes(roles)) {
    return <div>UNAUTHORIZED: 403</div>;
  }

  return children;
}

ProtectedRoute.propTypes = {
  children: PropTypes.node.isRequired,
  allowedRoles: PropTypes.arrayOf(PropTypes.string),
};

export default ProtectedRoute;
