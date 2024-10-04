import { useContext } from "react";
import { AuthContext } from "./auth-context";

const useAuth = () => useContext(AuthContext)

export default useAuth