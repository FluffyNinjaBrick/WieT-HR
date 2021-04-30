import { useEffect, useState } from "react";
import { Button, Table } from "react-bootstrap";
import SingleEmployeeRecord from "./SingleEmployeeRecord";
import { Link } from "react-router-dom";
import { fetchEmployees } from "../../services/EmployeeService";
import { Loading } from "../loader/LoadingView";

export default function EmployeeListViewAdmin() {
  const [employees, setEmployees] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(true);

    fetchEmployees()
      .then((data) => setEmployees(data))
      .then(setLoading(false));
  }, []);

  return (
    <div className="container">
      <h1 className="my-3">Pracownicy</h1>
      <Link to="/employees/create">
        <Button className="mb-3" variant="primary">
          Dodaj pracownika
        </Button>
      </Link>
      <div>
        {loading ? (
          <Loading />
        ) : (
          <Table bordered hover size="sm">
            <thead>
              <tr>
                <th>Imię</th>
                <th>Nazwisko</th>
                <th>Email</th>
                <th>Adres</th>
                <th>Numer telefonu</th>
                <th>Status</th>
                <th>Edycja</th>
              </tr>
            </thead>

            <tbody>
              {employees.length &&
                employees.map((employee) => (
                  <SingleEmployeeRecord key={employee.id} employee={employee} />
                ))}
            </tbody>
          </Table>
        )}
      </div>
    </div>
  );
}
