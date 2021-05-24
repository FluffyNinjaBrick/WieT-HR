import { API_URL } from "../api/Api";
import { getCurrentUser } from "./AuthService";
import axios from "axios";

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

  const response = await fetch(`${API_URL}employees`, {
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

  const response = await fetch(`${API_URL}employee?id=${id}`, {
    method: "GET",
    headers: myHeaders,
  });

  console.log(response);

  const employees = await response.json();
  return employees;
};

export const fetchEmployeesBonusesForYear = (year) => {
  const user = JSON.parse(getCurrentUser());
  const token = user ? user.jwt : "";
  const auth = "Bearer " + token;

  const myHeaders = {
    "Content-Type": "application/json",
    Authorization: auth,
  };

  return axios.get(`${API_URL}bonuses/${year}`, {
    method: "GET",
    headers: myHeaders,
  });
};

export const fetchBonusBudgetForYear = (year) => {
  const user = JSON.parse(getCurrentUser());
  const token = user ? user.jwt : "";
  const auth = "Bearer " + token;

  const myHeaders = {
    "Content-Type": "application/json",
    Authorization: auth,
  };

  return axios.get(`${API_URL}budget/${year}`, {
    method: "GET",
    headers: myHeaders,
  });
};

export const changeBonusBudgetForYear = (year, newBudget) => {
  const user = JSON.parse(getCurrentUser());
  const token = user ? user.jwt : "";
  const auth = "Bearer " + token;

  console.log(newBudget);

  const myHeaders = {
    "Content-Type": "application/json",
    Authorization: auth,
  };

  return axios.put(`${API_URL}budget`, {
    method: "PUT",
    headers: myHeaders,
    body: JSON.stringify(newBudget),
  });
};

export const addBonus = (bonus) => {
  const user = JSON.parse(getCurrentUser());
  const token = user ? user.jwt : "";
  const auth = "Bearer " + token;

  axios({
    method: "put",
    url: `${API_URL}bonuses`,
    headers: {
      "Content-Type": "application/json",
      Authorization: auth,
    },
    data: {
      employeeId: bonus.employeeId,
      yearMonth: `${bonus.year}-${bonus.month}`,
      dateGenerated: bonus.dateGenerated,
      value: Number(bonus.value),
      bonusBudgetId: bonus.bonusBudgetId,
    },
  });
};

export const getBudgetForYear = async (year) => {
  const user = JSON.parse(getCurrentUser());
  const token = user ? user.jwt : "";
  const auth = "Bearer " + token;

  return await axios({
    method: "get",
    url: `${API_URL}budget?year=${year}`,
    headers: {
      "Content-Type": "application/json",
      Authorization: auth,
    },
  });
};

export const getSalariesForYear = async (year) => {
  const user = JSON.parse(getCurrentUser());
  const token = user ? user.jwt : "";
  const auth = "Bearer " + token;

  return await axios({
    methos: "get",
    url: `${API_URL}employees/salaries/${year}`,
    headers: {
      "Content-Type": "application/json",
      Authorization: auth,
    },
  });
};
