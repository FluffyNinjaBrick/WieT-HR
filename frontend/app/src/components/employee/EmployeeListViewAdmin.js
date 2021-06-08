import { useEffect, useState } from "react";
import { Button, Table } from "react-bootstrap";
import SingleEmployeeRecord from "./SingleEmployeeRecord";
import { Link, useHistory } from "react-router-dom";
import { fetchEmployees } from "../../services/EmployeeService";
import { Loading } from "../loader/LoadingView";
import filterFactory, {
  textFilter,
  selectFilter,
  dateFilter,
  Comparator,
} from "react-bootstrap-table2-filter";
import { ProgressBar } from "react-bootstrap";
import BootstrapTable from "react-bootstrap-table-next";
import { AddContractView } from "../contract/AddContractView";
import TableEmployees from "./TableEmployees";

export default function EmployeeListViewAdmin() {
  const [employees, setEmployees] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);

    fetchEmployees()
      .then((data) => setEmployees(data))
      .then(() => setLoading(false));
  }, []);

  const columns = [
    {
      dataField: "name",
      text: "Imię i nazwisko",
      filter: textFilter({
        placeholder: "Podaj imię i nazwisko",
      }),
      headerStyle: { textAlign: "center" },
    },
    {
      dataField: "email",
      text: "Email",
      filter: textFilter({
        placeholder: "Podaj email",
      }),
      headerStyle: { textAlign: "center" },
    },
    {
      dataField: "address",
      text: "Adres",
      filter: textFilter({
        placeholder: "Podaj adres",
      }),
      headerStyle: { textAlign: "center" },
    },
    {
      dataField: "phone",
      text: "Nr telefonu",
      filter: textFilter({
        placeholder: "Podaj nr telefonu",
      }),
      headerStyle: { textAlign: "center" },
    },
    {
      dataField: "statusTranslated",
      text: "Status",
      filter: selectFilter({
        options: {
          Pracuje: "Pracuje",
          "Nie pracuje": "Nie pracuje",
        },
        placeholder: "Wszystkie",
      }),
      headerStyle: { textAlign: "center" },
    },
    // {
    //   dataField: "editButton",
    //   text: "Edycja",
    //   headerStyle: { textAlign: "center" },
    // },
    {
      dataField: "addContract",
      text: "Dodaj umowę",
      headerStyle: { textAlign: "center" },
    },
  ];

  if (loading) {
    return <Loading />;
  }

  return (
    <div className="container">
      <h1 className="my-3">Pracownicy</h1>
      <Link to="/employees/create">
        <Button className="mb-3" variant="primary">
          Dodaj pracownika
        </Button>
      </Link>
      <h4 className="my-3">Wszyscy pracownicy</h4>

      <div>
        {loading ? (
          <Loading />
        ) : employees.length ? (
          // <TableEmployees />
          <div className="mb-5">
            <BootstrapTable
              keyField="id"
              data={employees.map((employee) => {
                let x = { ...employee };
                x.editButton = (
                  <Link
                    to={{
                      pathname: "/employees/edit/" + employee.id,
                      state: {
                        employee: employee,
                      },
                    }}
                    style={{ textDecoration: "none" }}
                  >
                    Edytuj
                  </Link>
                );
                x.addContract = (
                  <Link
                    to={{
                      pathname: `/employees/${employee.id}/contract/add`,
                      state: {
                        employee: employee,
                      },
                    }}
                    style={{ textDecoration: "none" }}
                  >
                    Dodaj umowę
                  </Link>
                );
                x.statusTranslated =
                  employee.status === "WORKING" ? "Pracuje" : "Nie pracuje";
                x.name = employee.firstName + " " + employee.lastName;
                return x;
              })}
              columns={columns}
              filter={filterFactory()}
              filterPosition="top"
              striped
              hover
            />
          </div>
        ) : (
          <div className="mt-3">
            <h6>Nie znaleziono żadnych pracowników.</h6>
          </div>
        )}
      </div>
    </div>
  );
}
