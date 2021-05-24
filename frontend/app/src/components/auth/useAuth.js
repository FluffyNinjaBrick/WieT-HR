import React, { useState, useEffect, useContext, createContext } from "react";
import { API_URL } from "../../api/Api";
import axios from "axios";

const authContext = createContext();

export function ProvideAuth({ children }) {
  const auth = useProvideAuth();
  return <authContext.Provider value={auth}>{children}</authContext.Provider>;
}

export const useAuth = () => {
  return useContext(authContext);
};

function useProvideAuth() {
  const [user, setUser] = useState(null);

  const login = async (email, password) => {
    const response = await fetch(API_URL + "authenticate", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        email: email,
        password: password,
      }),
    });

    const userData = await response.json();
    setUser(userData);
    localStorage.setItem("user", JSON.stringify(userData));
    return response;
  };

  const logout = () => {
    localStorage.removeItem("user");
    setUser(false);
  };

  const changePassword = (newPassword) => {
    axios({
      method: "put",
      url: `${API_URL}employees/password`,
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${user.jwt}`,
      },
      data: {
        id: user.id,
        newPassword: newPassword,
      },
    });
  };

  useEffect(() => {
    let u = getCurrentUser();
    setUser(JSON.parse(u));
  }, []);

  const getCurrentUser = () => {
    return localStorage.getItem("user");
  };

  return {
    user,
    login,
    logout,
    changePassword,
  };
}
