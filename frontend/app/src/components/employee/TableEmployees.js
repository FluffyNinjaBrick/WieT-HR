import { useEffect, useMemo, useState } from "react";
import { useQuery } from "react-query";
import { TableInstance } from "../tables/TableInstance";
import { LoadingComponent } from "../loader/LoadingView";
import { fetchEmployees } from "../../services/EmployeeService";
import { Button } from "react-bootstrap";
import { Link } from "react-router-dom";

export default function TableEmployees() {
  const [tableData, setTableData] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    setLoading(true);

    fetchEmployees()
      .then((data) => {
        setTableData(data);
        console.log(data);
      })
      .then(() => setLoading(false));
  }, []);
  // const [tableData, setTableData] = useState(null);

  // const {
  //   isLoading,
  //   error,
  //   data: apiResponse,
  //   refetch,
  //   isFetching,
  //   status,
  // } = useQuery("employeesBonusesForYear", () =>
  //   fetchEmployeesBonusesForYear(year)
  // );

  // useEffect(() => {
  //   setTableData(apiResponse?.data.bonuses);
  //   setBudgetId(apiResponse?.data.bonusBudgetId);
  //   setBudgetLeft(apiResponse?.data.budgetLeft);
  // }, [apiResponse]);

  const tableColumns = [
    {
      Header: "Id",
      accessor: "id",
    },
    {
      Header: "Imię i nazwisko",
      accessor: "fullName",
    },
    {
      Header: "Adres email",
      accessor: "email",
    },
    {
      Header: "Adres",
      accessor: "address",
    },
    {
      Header: "Numer telefonu",
      accessor: "phone",
    },
    {
      Header: "Status",
      accessor: "status",
      Cell: (props) => (
        <>{props.row.values.status === "WORKING" ? "Pracuje" : "Nie pracuje"}</>
      ),
    },
    {
      Header: "Edycja",
      accessor: "edycja",
      Cell: (props) => (
        <Link
          to={{
            pathname: "/employees/edit/" + props.row.values.id,
          }}
        >
          Edytuj
        </Link>
      ),
    },
    // {
    //   Header: "Umowa",
    //   accessor: "umowa",
    //   Cell: (props) => (
    //     <Link
    //       to={{
    //         pathname: "/contract/add",
    //         // state: {
    //         //   employee: employee,
    //         // },
    //       }}
    //       style={{ textDecoration: "none" }}
    //     >
    //       Dodaj umowę
    //     </Link>
    //   ),
    // },
  ];

  return (
    <>
      <TableInstance
        tableData={tableData || []}
        tableColumns={tableColumns}
        initialState={{
          hiddenColumns: ["id"],
        }}
        footer={false}
      />
    </>
  );
}
