import { useEffect, useState } from "react";
import { Button, Table, ThemeProvider } from "react-bootstrap";
import SingleEmployeeRecord from "./SingleEmployeeRecord";
import { API_URL } from "../../api/Api";
import { Chart } from "react-google-charts";
import CalendarComponent from "../calendar/CalendarComponent";
import { Link } from "react-router-dom";

export default function EmployeeListViewAdmin() {
  const [employees, setEmployees] = useState([]);

  const fetchEmployees = async () => {
    const emp = await fetch(API_URL + "employees", {
      method: "GET",
      headers: { "Content-Type": "application/json" },
    });
    return emp.json();
  };

  useEffect(() => {
    fetchEmployees().then((data) => setEmployees(data));
  }, []);

  const employeesTableView = employees.map((employee) => {
    return <SingleEmployeeRecord key={employee.id} employee={employee} />;
  });

  const editEmployee = () => {};

  return (
    <div className="container mt-3">
      <h2>Pracownicy</h2>
      <Link to="/employees/create">
        <Button className="my-4" variant="primary">
          Dodaj pracownika
        </Button>
      </Link>
      <Table bordered hover size="sm">
        <thead>
          <tr>
            <th>Id</th>
            <th>Imię</th>
            <th>Nazwisko</th>
            <th>Email</th>
            <th>Adres</th>
            <th>Numer telefonu</th>
            <th>Status</th>
            <th>Edycja</th>
          </tr>
        </thead>
        <tbody>{employeesTableView}</tbody>
      </Table>
    </div>
  );
}