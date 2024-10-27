import { useEffect, useState, createContext } from "react";

const AuthContext = createContext();

const AuthWrapper = ({ children }) => {
  const [isAuth, setIsAuth] = useState(false);
  const [roles, setRoles] = useState("ROLE_ANONYMOUS");
  const [userEmail, setUserEmail] = useState("");
  const [loading, setLoading] = useState(true);
  const [cookies, setCookies] = useState({
    email: getCookieValue("userEmail") || null,
    role: getCookieValue("userRoles") || null,
  });

  function getCookieValue(name) {
    const cookies = document.cookie.split("; ");
    for (let cookie of cookies) {
      const [key, value] = cookie.split("=");
      if (key === name) {
        return decodeURIComponent(value);
      }
    }
    return null;
  }

  const logout = () => {
    setIsAuth(false);
    setUserEmail("");
    setRoles("ROLE_ANONYMOUS");
  };

  useEffect(() => {
    const interval = setInterval(() => {
      const email = getCookieValue("userEmail");
      const role = getCookieValue("userRoles");

      // ! if both email and userRole cookies are null, then logout
      if (!email && !role) {
        logout();
      }

      // ! compare with previous cookies (for debug purpose)
      // if (cookies.email !== email) {
      //   if (email) {
      //     console.log("Cookie added: userEmail");
      //   } else {
      //     console.log("Cookie deleted: userEmail");
      //   }
      // }

      // if (cookies.role !== role) {
      //   if (role) {
      //     console.log("Cookie added: userRoles");
      //   } else {
      //     console.log("Cookie deleted: userRoles");
      //   }
      // }

      setCookies({ email, role });
    }, 1000);

    return () => clearInterval(interval);
  }, [cookies]);

  useEffect(() => {
    const email = getCookieValue("userEmail");
    const role = getCookieValue("userRoles");

    if (email) {
      setUserEmail(email);
    } else {
      setUserEmail("");
    }

    if (role) {
      setRoles(role);
      setIsAuth(true);
    } else {
      setRoles("ROLE_ANONYMOUS");
      setIsAuth(false);
    }

    setLoading(false);
  }, [isAuth, userEmail, roles]);

  return (
    <AuthContext.Provider
      value={{
        isAuth,
        setIsAuth,
        roles,
        userEmail,
        loading,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export { AuthWrapper, AuthContext };
