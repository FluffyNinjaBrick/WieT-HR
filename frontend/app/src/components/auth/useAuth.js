import React, { useState, useEffect, useContext, createContext } from "react";
import { API_URL } from "../../api/Api";

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

  useEffect(() => {
    let u = getCurrentUser();
    if (u) {
      setUser(u);
    } else {
      setUser(false);
    }
  }, []);

  const getCurrentUser = () => {
    return localStorage.getItem("user");
  };

  return {
    user,
    login,
    logout,
  };
}
