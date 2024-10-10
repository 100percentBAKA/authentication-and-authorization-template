import { Navigate } from "react-router-dom";
import useAuth from "../context/useAuth";
import PropTypes from "prop-types";

function ProtectedRoute({ children, allowedRoles }) {
  const { roles, isAuth, loading } = useAuth();

  if (loading) {
    return <div>Loading...</div>;
  }

  if (!isAuth) {
    return <Navigate to="/auth/login" replace={true} />;
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
