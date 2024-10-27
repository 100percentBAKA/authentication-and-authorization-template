import { useState } from "react";
import genericPostService from "../../service/genericPostService";
import { Link } from "react-router-dom";

export default function RegisterForm() {
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    retypePassword: "",
    userAuthority: "USER",
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

    if (!formData.firstName.trim())
      newErrors.firstName = "First name is required";
    if (!formData.lastName.trim()) newErrors.lastName = "Last name is required";

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

    if (formData.password !== formData.retypePassword) {
      newErrors.retypePassword = "Passwords do not match";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (validateForm()) {
      // exclude the retypePassword field
      // eslint-disable-next-line no-unused-vars
      const { retypePassword, ...restFormData } = formData;
      // console.log("Form Data:", restFormData);

      const response = await genericPostService("auth/register", restFormData);
      console.log(response);
    } else {
      console.log("Form has errors");
    }
  };

  return (
    <section className="w-screen h-screen flex flex-row">
      <div className="hidden md:flex flex-1 border-2"></div>
      <div className="flex-1 p-3 md:p-10">
        <div className="w-full px-4 bg-red-200 rounded-md flex flex-col space-y-2">
          <form className="flex flex-col gap-8" onSubmit={handleSubmit}>
            <div className="text-3xl pt-8 pb-6">Sign Up</div>

            <div className="flex flex-row space-x-2">
              <div className="flex-1">
                <input
                  type="text"
                  id="firstName"
                  name="firstName"
                  placeholder="First Name"
                  className={`custom-textbox ${
                    errors.firstName ? "border-red-500" : ""
                  }`}
                  value={formData.firstName}
                  onChange={handleChange}
                />
                {errors.firstName && (
                  <p className="text-red-500 text-xs mt-1">
                    {errors.firstName}
                  </p>
                )}
              </div>

              <div className="flex-1">
                <input
                  type="text"
                  id="lastName"
                  name="lastName"
                  placeholder="Last Name"
                  className={`custom-textbox ${
                    errors.lastName ? "border-red-500" : ""
                  }`}
                  value={formData.lastName}
                  onChange={handleChange}
                />
                {errors.lastName && (
                  <p className="text-red-500 text-xs mt-1">{errors.lastName}</p>
                )}
              </div>
            </div>

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

            <div>
              <input
                type="password"
                id="retypePassword"
                name="retypePassword"
                className={`custom-textbox ${
                  errors.retypePassword ? "border-red-500" : ""
                }`}
                placeholder="Retype Password"
                value={formData.retypePassword}
                onChange={handleChange}
              />
              {errors.retypePassword && (
                <p className="text-red-500 text-xs mt-1">
                  {errors.retypePassword}
                </p>
              )}
            </div>

            <div>
              <select
                name="userAuthority"
                id="userAuthority"
                className="custom-textbox text-sm"
                value={formData.userAuthority}
                onChange={handleChange}
              >
                <option value="ADMIN">ADMIN</option>
                <option value="USER">USER</option>
                <option value="PRO_USER">PRO_USER</option>
              </select>
            </div>

            <button
              className="px-4 py-2 bg-blue-400 rounded-md hover:bg-blue-500 my-6 transition-all duration-200"
              type="submit"
            >
              Submit
            </button>
          </form>

          <Link to="/auth/login" className="underline">
            Already Registered ?
          </Link>
        </div>
      </div>
    </section>
  );
}
