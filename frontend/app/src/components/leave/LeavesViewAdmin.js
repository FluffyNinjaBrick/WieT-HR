import { useEffect, useState } from "react";
import { fetchEmployees } from "../../services/EmployeeService";
import { Table, ProgressBar } from "react-bootstrap";
import BootstrapTable from "react-bootstrap-table-next";
import filterFactory, {
  textFilter,
  selectFilter,
  dateFilter,
  Comparator,
} from "react-bootstrap-table2-filter";
import SingleEmployeeLeavesView from "./SingleEmployeeLeavesView";
import { Loading } from "../loader/LoadingView";
import { fetchAllDaysOff, LeaveTypes } from "../../services/DocumentsService";

export default function LeavesViewAdmin() {
  const [allDaysOff, setAllDaysOff] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(true);
    fetchAllDaysOff()
      .then((data) => {
        setAllDaysOff(data.filter((x) => x.signed));
      })
      .then(setLoading(false));
  }, []);

  const columns = [
    {
      dataField: "name",
      text: "Imię i nazwisko",
      filter: textFilter({
        placeholder: "Podaj imie i nazwisko",
      }),
      headerStyle: { textAlign: "center" },
    },
    {
      dataField: "dateFrom",
      text: "Data od",
      filter: dateFilter({
        withoutEmptyComparatorOption: true,
        comparators: [Comparator.GE],
        comparatorStyle: { display: "none" },
        dateStyle: { position: "relative" },
      }),
      headerStyle: { textAlign: "center" },
    },
    {
      dataField: "dateTo",
      text: "Data do",
      filter: dateFilter({
        withoutEmptyComparatorOption: true,
        comparators: [Comparator.LE],
        comparatorStyle: { display: "none" },
        dateStyle: { position: "relative" },
      }),
      headerStyle: { textAlign: "center" },
    },
    {
      dataField: "leaveType",
      text: "Typ urlopu",
      filter: selectFilter({
        options: {
          Macierzyński: "Macierzyński",
          Chorobowy: "Chorobowy",
          Rekreacyjny: "Rekreacyjny",
        },
        placeholder: "Wszystkie",
      }),
      headerStyle: { textAlign: "center" },
    },
    {
      dataField: "status",
      text: "Status",
      filter: selectFilter({
        options: {
          Zaakceptowany: "Zaakceptowany",
          Oczekujący: "Oczekujący",
        },
        placeholder: "Wszystkie",
      }),
      headerStyle: { textAlign: "center" },
    },
  ];

  function getColorVariant(daysLeft) {
    switch (parseInt((daysLeft * 3) / 30)) {
      case 0:
        return "danger";
      case 1:
        return "warning";
      case 2:
        return "info";
      default:
        return "info";
    }
  }

  return (
    <div className="container justify-content-sm-center">
      <div>
        <h1 className="my-4">Pracownicy / urlopy</h1>
      </div>
      <div>
        {loading ? (
          <Loading />
        ) : (
          <div>
            {allDaysOff.length ? (
              <BootstrapTable
                keyField="id"
                data={allDaysOff.map((daysOff) => {
                  let x = {
                    name:
                      daysOff.employee.firstName +
                      " " +
                      daysOff.employee.lastName,
                    dateFrom: daysOff.dateFrom,
                    dateTo: daysOff.dateTo,
                    leaveType: LeaveTypes[daysOff.leaveType],
                    status: daysOff.signed ? "Zaakceptowany" : "Oczekujący",
                  };
                  console.log(daysOff.leaveType);
                  return x;
                })}
                columns={columns}
                filter={filterFactory()}
                filterPosition="top"
                striped
                hover
              />
            ) : (
              <div className="mt-3">
                <h6>Nie znaleziono żadnych delegacji.</h6>
              </div>
            )}
          </div>
        )}
        {/* {employees.length ? (
          <BootstrapTable
            keyField="id"
            data={employees.map((employee) => {
              employee.daysOffLeft = (
              <ProgressBar
                className="my-1"
                now={employee.thisYearDaysOff * 100 / 30}
                label={employee.thisYearDaysOff + "/" + 30}
                variant={getColorVariant(employee.thisYearDaysOff)}
            />);
              employee.name = employee.firstName + " " + employee.lastName;
              return employee;
            })}
            columns={columns}
            filter={filterFactory()}
            filterPosition="top"
            striped
            hover
          />
        ) : (
          <div className="mt-3">
            <h6>Nie znaleziono żadnych pracowników.</h6>
          </div>
        )} */}
      </div>
    </div>
  );
}
