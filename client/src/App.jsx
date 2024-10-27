import { Routes, Route } from "react-router-dom";
import Landing from "./pages/navbar";

import LoginForm from "./pages/auth/login-form";
import RegisterForm from "./pages/auth/register-form";
import ProtectedRoute from "./components/protected-route";

function App() {
  return (
    <>
      <Routes>
        <Route path="/auth/login" element={<LoginForm />} />
        <Route path="/auth/register" element={<RegisterForm />} />

        <Route
          path="/"
          element={
            <ProtectedRoute>
              <Landing />
            </ProtectedRoute>
          }
        />
      </Routes>
    </>
  );
}

export default App;
