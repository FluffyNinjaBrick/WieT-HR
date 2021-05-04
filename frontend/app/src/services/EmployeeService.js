import { API_URL } from "../api/Api";
import { getCurrentUser } from "./AuthService";

export const EmployeeStatus = {
  WORKING: "Pracuje",
};

export const fetchEmployees = async () => {
  const user = JSON.parse(getCurrentUser());
  const token = user ? user.jwt : "";
  const auth = "Bearer " + token;
  const myHeaders = {
    "Content-Type": "application/json",
    Authorization: auth,
  };

  const response = await fetch(API_URL + "employees", {
    method: "GET",
    headers: myHeaders,
  });

  const employees = await response.json();
  return employees;
};

export const fetchCurrentEmployee = async () => {
  const user = JSON.parse(getCurrentUser());
  const token = user ? user.jwt : "";
  const auth = "Bearer " + token;
  const id = user.id;

  const myHeaders = {
    "Content-Type": "application/json",
    Authorization: auth,
  };

  const response = await fetch(API_URL + "employees/" + id, {
    method: "GET",
    headers: myHeaders,
  });

  const employees = await response.json();
  return employees;
};
