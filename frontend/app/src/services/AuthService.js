import { API_URL } from "../api/Api";

export const logout = () => {
  localStorage.removeItem("user");
};

export const login = async (formData) => {
  const response = await fetch(API_URL + "authenticate", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(formData),
  });

  const userData = await response.json();
  localStorage.setItem("user", JSON.stringify(userData));
  return response;
};

export const getCurrentUser = () => {
  return localStorage.getItem("user");
};
