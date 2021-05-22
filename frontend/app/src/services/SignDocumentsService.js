import { API_URL } from "../api/Api";
import { getCurrentUser } from "./AuthService";
import axios from "axios";

export const signLeave = async (daysoffDocument) => {
  const user = JSON.parse(getCurrentUser());
  const token = user ? user.jwt : "";
  const auth = "Bearer " + token;

  const myHeaders = {
    "Content-Type": "application/json",
    Authorization: auth,
  };

  console.log(myHeaders);
  console.log(daysoffDocument.id);
  console.log(`${API_URL}daysoff/sign?id=${daysoffDocument.id}`);

  const response = await fetch(
    `${API_URL}daysoff/sign?id=${daysoffDocument.id}`,
    {
      method: "PUT",
      headers: myHeaders,
    }
  );
};

export const signDelegation = async (delegationDocument) => {
  const user = JSON.parse(getCurrentUser());
  const token = user ? user.jwt : "";
  const auth = "Bearer " + token;

  const myHeaders = {
    "Content-Type": "application/json",
    Authorization: auth,
  };

  const response = await fetch(
    `${API_URL}delegation/sign?id=${delegationDocument.id}`,
    {
      method: "PUT",
      headers: myHeaders,
    }
  );
};
