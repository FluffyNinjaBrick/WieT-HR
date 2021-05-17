import { API_URL } from "../api/Api";
import { getCurrentUser } from "./AuthService";


export const signLeave = async (daysoffDocument) => {
    const user = JSON.parse(getCurrentUser());
    const token = user ? user.jwt : "";
    const auth = "Bearer " + token;
  
    const myHeaders = {
      "Content-Type": "application/json",
      Authorization: auth,
    };

    const URL = "documents/daysoff/sign";
    const response = await fetch(API_URL + URL, {
      method: "PUT",
      headers: myHeaders,
      body: JSON.stringify(daysoffDocument.id),
    });
  
    if (!response.ok) {
      return response.status;
    }
};
  

export const signDelegation = async (delegationDocument) => {
  const user = JSON.parse(getCurrentUser());
  const token = user ? user.jwt : "";
  const auth = "Bearer " + token;

  const myHeaders = {
    "Content-Type": "application/json",
    Authorization: auth,
  };

  const URL = "documents/delegation/sign";
  const response = await fetch(API_URL + URL, {
    method: "PUT",
    headers: myHeaders,
    body: JSON.stringify(delegationDocument.id),
  });

  if (!response.ok) {
    return response.status;
  }
};