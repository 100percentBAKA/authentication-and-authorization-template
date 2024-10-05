import { useEffect, useState, createContext } from "react";

const AuthContext = createContext();

const AuthWrapper = ({ children }) => {
  const [isAuth, setIsAuth] = useState(false);
  const [roles, setRoles] = useState("ROLE_USER");
  const [userEmail, setUserEmail] = useState("");

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
      setRoles(rolesFromCookie); // Set roles from cookie
      setIsAuth(true); // Set auth status to true if roles exist
    }
  }, []);

  // const logout = () => {
  //   setRoles("ROLE_USER");
  //   setUserEmail("");
  //   setIsAuth(false);
  // };

  // const login = (responseBody) => {
  //   setRoles(responseBody.roles[0]);
  //   setIsAuth(true);
  // };

  // useEffect(() => {
  //   if (roles) {
  //     console.log("Roles:", roles);
  //   }
  // }, [roles]);

  // useEffect(() => {
  //   if (userEmail) {
  //     console.log("User Email:", userEmail);
  //   }
  // }, [userEmail]);

  return (
    <AuthContext.Provider
      value={{
        isAuth,
        roles,
        userEmail,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export { AuthWrapper, AuthContext };
