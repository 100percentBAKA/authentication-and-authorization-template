import { useEffect, useState, createContext } from "react";

const AuthContext = createContext();

const AuthWrapper = ({ children }) => {
  const [accessToken, setAccessToken] = useState("");
  const [isAuth, setIsAuth] = useState(false);
  const [roles, setRoles] = useState("ROLE_USER");

  const logout = () => {
    setAccessToken(null);
    setRoles("ROLE_USER");
    setIsAuth(false);
  };

  const login = (responseBody) => {
    setAccessToken(responseBody.accessToken);
    setRoles(responseBody.roles[0]);
    setIsAuth(true);
  };

  useEffect(() => {
    if (accessToken) {
      console.log("Access Token:", accessToken);
    }
  }, [accessToken]); // This effect runs when accessToken changes

  useEffect(() => {
    if (roles) {
      console.log("Roles:", roles);
    }
  }, [roles]); // This effect runs when roles change

  return (
    <AuthContext.Provider
      value={{
        accessToken,
        logout,
        login,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export { AuthWrapper, AuthContext };
