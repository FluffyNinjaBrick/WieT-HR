import { API_URL } from "../api/Api";
import { getCurrentUser } from "./AuthService";

export const fetchUserDaysOff = async () => {
  const user = JSON.parse(getCurrentUser());
  const token = user ? user.jwt : "";
  const auth = "Bearer " + token;
  const id = user.id;
  const currentDate = new Date();

  const myHeaders = {
    "Content-Type": "application/json",
    Authorization: auth,
  };

  const URL = "employees/"+ id + "/documents/daysoff/2021-01-01/2021-12-31"// + currentDate;

  const response = await fetch(API_URL + URL , {
    method: "GET",
    headers: myHeaders,
  });

  const daysOff = await response.json();
  return daysOff;
};

export const LeaveTypes = {
    "SICK" : "Chorobowy",
    "MATERNITY" : "Macierzyński",
    "RECREATIONAL" : "Rekreacyjny",
}



