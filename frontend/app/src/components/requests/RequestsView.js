import CalendarView from "../calendar/CalendarView";
import { useEffect, useState } from "react";
import { fetchEmployees } from "../../services/EmployeeService";
import { Table } from "react-bootstrap";
import BootstrapTable from "react-bootstrap-table-next";
import filterFactory, {
  textFilter,
  selectFilter,
  dateFilter,
  Comparator,
} from "react-bootstrap-table2-filter";
import { Loading } from "../loader/LoadingView";
import { fetchAllDelegations, fetchAllDaysOff, LeaveTypes } from "../../services/DocumentsService";

export default function RequestsView() {
    const [allDelegations, setAllDelegations] = useState([]);
    const [loading, setLoading] = useState(true);
    const [allDaysOff, setAllDaysOff] = useState([]);

    useEffect(() => {
      setLoading(true);
      fetchAllDaysOff().then((daysOffData) => {
        setAllDaysOff(daysOffData.filter(x => !x.signed));
      })
      fetchAllDelegations()
        .then((data) => {
          setAllDelegations(data.filter(x => !x.signed));
        })
        .then(() => setLoading(false));
    }, []);
  
    const delegationColumns = [
        {
          dataField: "name",
          text: "Imię i nazwisko",
          filter: textFilter({
            placeholder: "Podaj imie i nazwisko"
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
          dataField: "destination",
          text: "Miejsce",
          filter: textFilter({
            placeholder: "Podaj miejsce delegacji"
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

      const daysOffColumns = [
        {
          dataField: "name",
          text: "Imię i nazwisko",
          filter: textFilter({
            placeholder: "Podaj imie i nazwisko"
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
                Rekreacyjny: "Rekreacyjny"
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

    if(loading) {
      return <Loading/>;
    }

    return(
    <div>
        <div className="container">
        <h1 className="my-4">Pracownicy / wnioski</h1>
      <CalendarView />
      <br></br>
      
      {loading ? (
        <Loading />
      ) : (
        <div>
        <h5 className="my-4">Wnioski o delegacje</h5>
          {allDelegations.length ? (
            <BootstrapTable
              keyField="id"
              data={allDelegations.map((delegation) => {
                let x = {
                  name:
                    delegation.employee.firstName +
                    " " +
                    delegation.employee.lastName,
                  dateFrom: delegation.dateFrom,
                  dateTo: delegation.dateTo,
                  destination: delegation.destination,
                  status: delegation.signed ? "Zaakceptowany" : "Oczekujący",
                };
                return x; 
              })}
              columns={delegationColumns}
              filter={filterFactory()}
              filterPosition="top"
              striped
              hover
            />
          ) : (
            <div className="mt-3">
              <h6>Nie znaleziono żadnych wniosków o delegację.</h6>
            </div>
          )}
          <h5 className="my-4">Wnioski o urlop</h5>
            {allDaysOff.length && !loading ? (
              <div>
                
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
                    return x; 
                })}
                columns={daysOffColumns}
                filter={filterFactory()}
                filterPosition="top"
                striped
                hover
                />
              </div>
            ) : (
                
                <div className="mt-3">
                  <h6>Nie znaleziono żadnych wniosków o urlop.</h6>
                </div>
            )}
        </div>

      )}
      {/* <Table bordered hover size="sm">
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
      </Table> */}
      </div>
    </div>
  )
}