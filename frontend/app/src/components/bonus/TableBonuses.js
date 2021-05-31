import { useEffect, useMemo, useState } from "react";
import { useQuery } from "react-query";
import { TableInstance } from "../tables/TableInstance";
import { LoadingComponent } from "../loader/LoadingView";
import { fetchEmployeesBonusesForYear } from "../../services/EmployeeService";
import { Button } from "react-bootstrap";
import AddBonusModal from "./addBonus/AddBonusModal";

const TableBonuses = ({ year }) => {
  const [tableData, setTableData] = useState(null);
  const [modalShow, setModalShow] = useState(false);
  const [modalEmployeeId, setModalEmployeeId] = useState(null);
  const [modalEmployeeName, setModalEmployeeName] = useState(null);

  const [budgetLeft, setBudgetLeft] = useState(null);
  const [budgetId, setBudgetId] = useState(null);

  const {
    isLoading,
    error,
    data: apiResponse,
    refetch,
    isFetching,
    status,
  } = useQuery("employeesBonusesForYear", () =>
    fetchEmployeesBonusesForYear(year)
  );

  useEffect(() => {
    setTableData(apiResponse?.data.bonuses);
    setBudgetId(apiResponse?.data.bonusBudgetId);
    setBudgetLeft(apiResponse?.data.budgetLeft);
  }, [apiResponse]);

  useEffect(() => {
    refetch();
  }, [year]);

  const tableColumns = [
    {
      Header: "Id",
      accessor: "employeeId",
      Footer: "",
    },
    {
      Header: "Imię i nazwisko",
      accessor: "employeeName",
      Footer: "SUMA",
    },
    {
      Header: "Styczeń",
      accessor: "employeeBonuses[0]",
      Footer: (summary) => {
        const total = useMemo(
          () =>
            summary.rows.reduce(
              (sum, row) => row.values["employeeBonuses[0]"] + sum,
              0
            ),
          [summary.rows]
        );
        return <>{total}</>;
      },
    },
    {
      Header: "Luty",
      accessor: "employeeBonuses[1]",
      Footer: (summary) => {
        const total = useMemo(
          () =>
            summary.rows.reduce(
              (sum, row) => row.values["employeeBonuses[1]"] + sum,
              0
            ),
          [summary.rows]
        );
        return <>{total}</>;
      },
    },
    {
      Header: "Marzec",
      accessor: "employeeBonuses[2]",
      Footer: (summary) => {
        const total = useMemo(
          () =>
            summary.rows.reduce(
              (sum, row) => row.values["employeeBonuses[2]"] + sum,
              0
            ),
          [summary.rows]
        );
        return <>{total}</>;
      },
    },
    {
      Header: "Kwiecień",
      accessor: "employeeBonuses[3]",
      Footer: (summary) => {
        const total = useMemo(
          () =>
            summary.rows.reduce(
              (sum, row) => row.values["employeeBonuses[3]"] + sum,
              0
            ),
          [summary.rows]
        );
        return <>{total}</>;
      },
    },
    {
      Header: "Maj",
      accessor: "employeeBonuses[4]",
      Footer: (summary) => {
        const total = useMemo(
          () =>
            summary.rows.reduce(
              (sum, row) => row.values["employeeBonuses[4]"] + sum,
              0
            ),
          [summary.rows]
        );
        return <>{total}</>;
      },
    },
    {
      Header: "Czerwiec",
      accessor: "employeeBonuses[5]",
      Footer: (summary) => {
        const total = useMemo(
          () =>
            summary.rows.reduce(
              (sum, row) => row.values["employeeBonuses[5]"] + sum,
              0
            ),
          [summary.rows]
        );
        return <>{total}</>;
      },
    },
    {
      Header: "Lipiec",
      accessor: "employeeBonuses[6]",
      Footer: (summary) => {
        const total = useMemo(
          () =>
            summary.rows.reduce(
              (sum, row) => row.values["employeeBonuses[6]"] + sum,
              0
            ),
          [summary.rows]
        );
        return <>{total}</>;
      },
    },
    {
      Header: "Sierpień",
      accessor: "employeeBonuses[7]",
      Footer: (summary) => {
        const total = useMemo(
          () =>
            summary.rows.reduce(
              (sum, row) => row.values["employeeBonuses[7]"] + sum,
              0
            ),
          [summary.rows]
        );
        return <>{total}</>;
      },
    },
    {
      Header: "Wrzesień",
      accessor: "employeeBonuses[8]",
      Footer: (summary) => {
        const total = useMemo(
          () =>
            summary.rows.reduce(
              (sum, row) => row.values["employeeBonuses[8]"] + sum,
              0
            ),
          [summary.rows]
        );
        return <>{total}</>;
      },
    },
    {
      Header: "Październik",
      accessor: "employeeBonuses[9]",
      Footer: (summary) => {
        const total = useMemo(
          () =>
            summary.rows.reduce(
              (sum, row) => row.values["employeeBonuses[9]"] + sum,
              0
            ),
          [summary.rows]
        );
        return <>{total}</>;
      },
    },
    {
      Header: "Listopad",
      accessor: "employeeBonuses[10]",
      Footer: (summary) => {
        const total = useMemo(
          () =>
            summary.rows.reduce(
              (sum, row) => row.values["employeeBonuses[10]"] + sum,
              0
            ),
          [summary.rows]
        );
        return <>{total}</>;
      },
    },
    {
      Header: "Grudzień",
      accessor: "employeeBonuses[11]",
      Footer: (summary) => {
        const total = useMemo(
          () =>
            summary.rows.reduce(
              (sum, row) => row.values["employeeBonuses[11]"] + sum,
              0
            ),
          [summary.rows]
        );
        return <>{total}</>;
      },
    },
    {
      Header: "SUMA",
      accessor: "employeeBonusesTotal",
    },
    {
      Header: "Przydziel premię",
      accessor: "bonus",
      Cell: (props) => (
        <Button
          onClick={() => {
            setModalEmployeeId(props.row.values.employeeId);
            setModalEmployeeName(props.row.values.employeeName);
            setModalShow(true);
          }}
          variant="primary"
          className="w-100 h-100"
          disabled={budgetLeft <= 0 ? true : false}
        >
          Przydziel premię
        </Button>
      ),
    },
  ];

  if (isLoading || !tableData) {
    return <LoadingComponent />;
  }

  if (error) {
    return <div>Wystąpił błąd podczas ładowania danych.</div>;
  }

  if (!tableData) {
    return <div>Nie znaleziono żadnych danych.</div>;
  }

  return (
    <>
      <TableInstance
        tableData={tableData || []}
        tableColumns={tableColumns}
        initialState={{
          hiddenColumns: ["employeeId"],
        }}
        footer={true}
      />
      <AddBonusModal
        show={modalShow}
        onHide={() => setModalShow(false)}
        year={year}
        employeeId={modalEmployeeId}
        employeeName={modalEmployeeName}
        bonusBudgetId={budgetId}
        budgetLeft={budgetLeft}
        update={refetch}
      />
    </>
  );
};

export default TableBonuses;
