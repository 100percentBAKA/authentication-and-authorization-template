// import React from 'react'
import { Routes, Route } from "react-router-dom";
import RegisterForm from "./register-form";
import LoginForm from "./login-form";
import Home from "./pages/Home";
import RootLayout from "./pages/root-layout";

export default function App() {
  return (
    <>
      <Routes>
        <Route index path="/app/home" element={<Home />}></Route>
        <Route element={<LoginForm />} path="auth/login" />
        <Route element={<RegisterForm />} path="auth/register" />
      </Routes>
    </>
  );
}
