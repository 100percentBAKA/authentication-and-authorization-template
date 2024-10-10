// import React from 'react'
import { Routes, Route, Navigate } from "react-router-dom";
import RegisterForm from "./register-form";
import LoginForm from "./login-form";
import RootLayout from "./pages/root-layout";
import Admin from "./pages/admin";
import ProUser from "./pages/pro-user";
import Home from "./pages/home";
import ProtectedRoute from "./components/protected-route";

export default function App() {
  return (
    <>
      <Routes>
        <Route path="app/" element={<RootLayout />}>
          <Route path="public/" element={<Home />} />
          <Route
            path="private/"
            element={
              <ProtectedRoute allowedRoles={["ROLE_PRO_USER", "ROLE_ADMIN"]}>
                <ProUser />
              </ProtectedRoute>
            }
          />
          <Route
            path="admin/"
            element={
              <ProtectedRoute allowedRoles={["ROLE_ADMIN"]}>
                <Admin />
              </ProtectedRoute>
            }
          />
        </Route>
        <Route element={<LoginForm />} path="auth/login" />
        <Route element={<RegisterForm />} path="auth/register" />
      </Routes>
    </>
  );
}
