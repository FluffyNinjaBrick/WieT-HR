import { useEffect, useState } from "react";
import CalendarView from "../calendar/CalendarView";
import { fetchEmployees } from "../../services/EmployeeService";
import { Table } from "react-bootstrap";
import SingleEmployeeLeavesView from "./SingleEmployeeLeavesView";

export default function LeavesViewAdmin() {
  const [employees, setEmployees] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(true);

    fetchEmployees()
      .then((data) => setEmployees(data))
      .then(setLoading(false));
  }, []);

  return (
    <div className="container justify-content-sm-center col-sm-8">
      <div>
        <h1 className="my-3">Pracownicy / urlopy</h1>
      </div>
      <CalendarView />
      <br></br>
      <Table bordered hover size="sm">
        <thead>
          <tr>
            <th>Imię</th>
            <th>Nazwisko</th>
            <th>Email</th>
            <th>Adres</th>
            <th>Numer telefonu</th>
            <th>Pozostałe dni wolne</th>
          </tr>
        </thead>

        {loading ? (
          <tbody></tbody>
        ) : (
          <tbody>
            {employees.length &&
              employees.map((employee) => (
                <SingleEmployeeLeavesView
                  key={employee.id}
                  employee={employee}
                />
              ))}
          </tbody>
        )}
      </Table>
    </div>
  );
}
