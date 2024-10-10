import { useEffect, useState, createContext } from "react";

const AuthContext = createContext();

const AuthWrapper = ({ children }) => {
  const [isAuth, setIsAuth] = useState(false);
  const [roles, setRoles] = useState("ROLE_USER");
  const [userEmail, setUserEmail] = useState("");
  const [loading, setLoading] = useState(true); // New loading state

  const getCookieValue = (name) => {
    const cookies = document.cookie.split("; ");
    for (let cookie of cookies) {
      const [key, value] = cookie.split("=");
      if (key === name) {
        return decodeURIComponent(value);
      }
    }
    return null;
  };

  useEffect(() => {
    const email = getCookieValue("userEmail");
    const rolesFromCookie = getCookieValue("userRoles");

    if (email) {
      setUserEmail(email);
    }
    if (rolesFromCookie) {
      setRoles(rolesFromCookie);
      setIsAuth(true);
    }

    setLoading(false);
  }, []);

  return (
    <AuthContext.Provider
      value={{
        isAuth,
        roles,
        userEmail,
        loading,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export { AuthWrapper, AuthContext };
