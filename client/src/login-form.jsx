import { useState } from "react";
import genericPostService from "./service/genericPostService";
import useAuth from "./context/useAuth";

export default function LoginForm() {
  const { login } = useAuth();

  const [formData, setFormData] = useState({
    email: "",
    password: "",
  });

  const [errors, setErrors] = useState({});

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const validateForm = () => {
    let newErrors = {};

    if (!formData.email.trim()) {
      newErrors.email = "Email is required";
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = "Email is invalid";
    }

    if (!formData.password) {
      newErrors.password = "Password is required";
    } else if (formData.password.length < 6) {
      newErrors.password = "Password must be at least 6 characters";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (validateForm()) {
      console.log("Form Data:", formData);

      const response = await genericPostService("auth/login", formData);
      console.log(response);
      login(response);
    } else {
      console.log("Form has errors");
    }
  };

  return (
    <section className="w-screen h-screen flex flex-row">
      <div className="hidden md:flex flex-1 border-2"></div>
      <div className="flex-1 p-3 md:p-10">
        <div className="w-full px-4 bg-red-200 rounded-md">
          <form className="flex flex-col gap-8" onSubmit={handleSubmit}>
            <div className="text-3xl pt-8 pb-6">Login</div>

            <div>
              <input
                type="email"
                id="email"
                name="email"
                className={`custom-textbox ${
                  errors.email ? "border-red-500" : ""
                }`}
                placeholder="Email"
                value={formData.email}
                onChange={handleChange}
              />
              {errors.email && (
                <p className="text-red-500 text-xs mt-1">{errors.email}</p>
              )}
            </div>

            <div>
              <input
                type="password"
                id="password"
                name="password"
                className={`custom-textbox ${
                  errors.password ? "border-red-500" : ""
                }`}
                placeholder="Password"
                value={formData.password}
                onChange={handleChange}
              />
              {errors.password && (
                <p className="text-red-500 text-xs mt-1">{errors.password}</p>
              )}
            </div>

            <button
              className="px-4 py-2 bg-blue-400 rounded-md hover:bg-blue-500 my-6 transition-all duration-200"
              type="submit"
            >
              Login
            </button>
          </form>
        </div>
      </div>
    </section>
  );
}
