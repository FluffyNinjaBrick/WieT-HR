import { API_URL } from "../api/Api";
import { getCurrentUser } from "./AuthService";
import fileDownload from "js-file-download";

export const LeaveTypes = {
  SICK: "Chorobowy",
  MATERNITY: "Macierzyński",
  RECREATIONAL: "Rekreacyjny",
};

export const fetchUserDaysOffSummary = async () => {
  const user = JSON.parse(getCurrentUser());
  const token = user ? user.jwt : "";
  const auth = "Bearer " + token;
  const id = user.id;

  const myHeaders = {
    "Content-Type": "application/json",
    Authorization: auth,
  };

  const URL = `employees/getDaysOff/${id}`;

  const response = await fetch(API_URL + URL, {
    method: "GET",
    headers: myHeaders,
  });

  const daysOff = await response.json();
  return daysOff;
};

export const fetchUserDaysOff = async () => {
  const user = JSON.parse(getCurrentUser());
  const token = user ? user.jwt : "";
  const auth = "Bearer " + token;
  const id = user.id;

  const myHeaders = {
    "Content-Type": "application/json",
    Authorization: auth,
  };

  const URL = "employees/" + id + "/documents/daysoff/2021-01-01/2021-12-31"; // + currentDate;

  const response = await fetch(API_URL + URL, {
    method: "GET",
    headers: myHeaders,
  });

  const daysOff = await response.json();
  return daysOff;
};

export const fetchAllDaysOffRequests = async () => {
  const user = JSON.parse(getCurrentUser());
  const token = user ? user.jwt : "";
  const auth = "Bearer " + token;
  const id = user.id;

  const myHeaders = {
    "Content-Type": "application/json",
    Authorization: auth,
  };

  const URL = "/documents/delegation";

  const response = await fetch(API_URL + URL, {
    method: "GET",
    headers: myHeaders,
  });

  const allDelegationRequests = await response.json();
  return allDelegationRequests;
};

export const fetchAllDaysOff = async () => {
  const user = JSON.parse(getCurrentUser());
  const token = user ? user.jwt : "";
  const auth = "Bearer " + token;

  const myHeaders = {
    "Content-Type": "application/json",
    Authorization: auth,
  };

  const URL = "documents/daysoff"; // + currentDate;

  const response = await fetch(API_URL + URL, {
    method: "GET",
    headers: myHeaders,
  });

  const daysOffs = await response.json();
  return daysOffs;
};

export const fetchUserDelegationRequests = async () => {
  const user = JSON.parse(getCurrentUser());
  const token = user ? user.jwt : "";
  const auth = "Bearer " + token;
  const id = user.id;

  const myHeaders = {
    "Content-Type": "application/json",
    Authorization: auth,
  };

  const URL = "employees/" + id + "/documents/delegation/2021-01-01/2021-12-31";

  const response = await fetch(API_URL + URL, {
    method: "GET",
    headers: myHeaders,
  });

  const delegationRequests = await response.json();
  return delegationRequests;
};

export const fetchAllDelegations = async () => {
  const user = JSON.parse(getCurrentUser());
  const token = user ? user.jwt : "";
  const auth = "Bearer " + token;

  const myHeaders = {
    "Content-Type": "application/json",
    Authorization: auth,
  };

  const URL = "documents/delegation"; // + currentDate;

  const response = await fetch(API_URL + URL, {
    method: "GET",
    headers: myHeaders,
  });

  const delegations = await response.json();
  return delegations;
};

export const fetchDelegationDocumentPdf = async (delegationDocument) => {
  const user = JSON.parse(getCurrentUser());
  const token = user ? user.jwt : "";
  const auth = "Bearer " + token;

  const myHeaders = {
    "Content-Type": "application/json",
    Authorization: auth,
  };

  const URL = "documents/delegation/pdf/" + delegationDocument.id;

  const response = await fetch(API_URL + URL, {
    method: "GET",
    headers: myHeaders,
  });

  if (!response.ok) {
    return response.status;
  }

  const pdf = await response.blob();
  fileDownload(pdf, "wniosek_delegacja.pdf");
};

export const fetchDaysoffDocumentPdf = async (daysoffDocument) => {
  const user = JSON.parse(getCurrentUser());
  const token = user ? user.jwt : "";
  const auth = "Bearer " + token;

  const myHeaders = {
    "Content-Type": "application/json",
    Authorization: auth,
  };

  const URL = "documents/daysoff/pdf/" + daysoffDocument.id;

  const response = await fetch(API_URL + URL, {
    method: "GET",
    headers: myHeaders,
  });

  if (!response.ok) {
    return response.status;
  }

  const pdf = await response.blob();
  fileDownload(pdf, "wniosek_urlop.pdf");
};
