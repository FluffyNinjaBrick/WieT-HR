import { API_URL } from "../api/Api";
import { getCurrentUser } from "./AuthService";

const user = JSON.parse(getCurrentUser());
const token = user ? user.jwt : "";
const auth = "Bearer " + token;

const myHeaders = {
  "Content-Type": "application/json",
  Authorization: auth,
};

export const fetchEmployees = async () => {
  console.log(token);
  const response = await fetch(API_URL + "employees", {
    method: "GET",
    headers: myHeaders,
  });

  const employees = await response.json();
  return employees;
};
