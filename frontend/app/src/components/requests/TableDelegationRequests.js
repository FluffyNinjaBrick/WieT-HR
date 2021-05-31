import { useEffect, useMemo, useState } from "react";
import { useQuery } from "react-query";
import { TableInstance } from "../tables/TableInstance";
import { LoadingComponent } from "../loader/LoadingView";
import { fetchEmployees } from "../../services/EmployeeService";
import { Button } from "react-bootstrap";
import { Link } from "react-router-dom";
import {
  fetchAllDelegations,
  fetchAllDaysOff,
  LeaveTypes,
} from "../../services/DocumentsService";

export default function TableDelegationRequests() {
  const [allDelegations, setAllDelegations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [allDaysOff, setAllDaysOff] = useState([]);
  const [modalShow, setModalShow] = useState(false);
  const [currentDocument, setCurrentDocument] = useState([]);

  useEffect(() => {
    setLoading(true);
    fetchAllDaysOff().then((daysOffData) => {
      setAllDaysOff(daysOffData.filter((x) => !x.signed));
    });
    fetchAllDelegations()
      .then((delegations) => {
        setAllDelegations(delegations.filter((x) => !x.signed));
      })
      .then(() => setLoading(false));
  }, []);

  function setupModal(document) {
    setModalShow(true);
    setCurrentDocument(document);
  }
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
      accessor: "nameAtSigning",
    },
    {
      Header: "Data od",
      accessor: "dateFrom",
    },
    {
      Header: "Data do",
      accessor: "dateTo",
    },
    {
      Header: "Miejsce",
      accessor: "destination",
    },
    {
      Header: "Status",
      accessor: "signed",
      Cell: (props) => (
        <>{props.row.values.signed ? "Zaakceptowany" : "Oczekujący"}</>
      ),
    },
    {
      Header: "Decyzja",
      accessor: "decision",
      // Cell: (props) => (
      //   <>
      //     <Button
      //       style={{ width: "100%", borderRadius: "0" }}
      //       onClick={() => setupModal(delegation)}
      //     >
      //       Rozpatrz
      //     </Button>
      //     <RequestSignModal
      //       show={modalShow && currentDocument == delegation}
      //       onHide={() => setModalShow(false)}
      //       leave={delegation}
      //       type={"delegation"}
      //     />
      //   </>
      // ),
    },
  ];

  return (
    <>
      <TableInstance
        tableData={allDelegations || []}
        tableColumns={tableColumns}
        initialState={{
          hiddenColumns: ["id"],
        }}
        footer={false}
      />
    </>
  );
}
